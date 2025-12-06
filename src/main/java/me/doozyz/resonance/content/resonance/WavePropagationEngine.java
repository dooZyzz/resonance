package me.doozyz.resonance.content.resonance;

import me.doozyz.resonance.content.CrystalTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

/**
 * Handles wave propagation physics simulation for resonance networks
 */
public class WavePropagationEngine {

    private final ResonanceNetwork network;
    private final Map<BlockPos, Float> stressAccumulator = new HashMap<>();
    private static final float STRESS_THRESHOLD = 10f; // Seconds of overload

    public WavePropagationEngine(ResonanceNetwork network) {
        this.network = network;
    }

    /**
     * Propagate resonance from a source through the network using BFS
     */
    public void propagateFrom(BlockPos source, ResonanceState initialState, Map<BlockPos, ResonanceState> computedStates) {
        if (!initialState.isActive()) return;

        // BFS propagation with attenuation
        Map<BlockPos, Float> distances = new HashMap<>();
        PriorityQueue<PropagationStep> queue = new PriorityQueue<>(
                Comparator.comparingDouble(s -> s.distance)
        );

        queue.add(new PropagationStep(source, 0, initialState));
        distances.put(source, 0f);

        while (!queue.isEmpty()) {
            PropagationStep step = queue.poll();

            // Skip if we've found a shorter path
            Float knownDist = distances.get(step.pos);
            if (knownDist != null && knownDist < step.distance) continue;

            // Apply this state (combining with existing)
            ResonanceState existing = computedStates.getOrDefault(step.pos, ResonanceState.EMPTY);
            ResonanceState combined = existing.combine(step.state);
            computedStates.put(step.pos, combined);

            // Get node for tier info
            IResonanceNode node = network.getNode(step.pos);
            if (node == null) continue;

            // Stop if amplitude too low (optimization)
            if (step.state.amplitude() < 0.1f) continue;

            // Propagate to neighbors
            for (BlockPos neighbor : network.getNeighbors(step.pos)) {
                float edgeLength = 1.0f; // Could vary based on crystal type
                float newDist = step.distance + edgeLength;

                // Get neighbor node for attenuation
                IResonanceNode neighborNode = network.getNode(neighbor);
                if (neighborNode == null) continue;

                // Attenuate based on crystal tier
                ResonanceState attenuated = step.state.attenuate(
                        neighborNode.getTier().getAttenuationPerBlock(),
                        edgeLength
                );

                // Check capacity limits
                CrystalTier tier = neighborNode.getTier();
                if (attenuated.amplitude() > tier.getMaxAmplitude()) {
                    // Over capacity - handle stress
                    handleOverCapacity(neighbor, attenuated.amplitude(), tier.getMaxAmplitude());

                    // Clamp to max amplitude
                    attenuated = attenuated.withAmplitude(tier.getMaxAmplitude());
                }

                // Update distance and continue propagation
                Float neighborDist = distances.get(neighbor);
                if (neighborDist == null || newDist < neighborDist) {
                    distances.put(neighbor, newDist);
                    queue.add(new PropagationStep(neighbor, newDist, attenuated));
                }
            }
        }
    }

    /**
     * Handle over-capacity situations
     */
    private void handleOverCapacity(BlockPos pos, float amplitude, float maxAmplitude) {
        float ratio = amplitude / maxAmplitude;

        if (ratio > 1.5f) {
            // Severely overloaded - immediate cascade
            triggerCascade(List.of(pos));
        } else if (ratio > 1.0f) {
            // Moderately overloaded - accumulate stress
            accumulateStress(pos, ratio - 1.0f);
        } else {
            // Normal - relieve stress
            relieveStress(pos);
        }
    }

    /**
     * Accumulate stress over time
     */
    private void accumulateStress(BlockPos pos, float amount) {
        float current = stressAccumulator.getOrDefault(pos, 0f);
        float newStress = current + amount / 20f; // Per tick

        if (newStress >= STRESS_THRESHOLD) {
            triggerCascade(List.of(pos));
            stressAccumulator.remove(pos);
        } else {
            stressAccumulator.put(pos, newStress);
        }
    }

    /**
     * Relieve accumulated stress
     */
    private void relieveStress(BlockPos pos) {
        float current = stressAccumulator.getOrDefault(pos, 0f);
        if (current > 0) {
            float relieved = Math.max(0, current - 0.1f);
            if (relieved == 0) {
                stressAccumulator.remove(pos);
            } else {
                stressAccumulator.put(pos, relieved);
            }
        }
    }

    /**
     * Trigger cascade failure
     */
    private void triggerCascade(List<BlockPos> epicenters) {
        Level level = network.getLevel();
        if (level == null) return;

        Set<BlockPos> destroyed = new HashSet<>();
        Queue<BlockPos> toProcess = new LinkedList<>(epicenters);

        while (!toProcess.isEmpty()) {
            BlockPos pos = toProcess.poll();
            if (destroyed.contains(pos)) continue;

            // Destroy this crystal
            destroyed.add(pos);
            level.destroyBlock(pos, false);

            // Explosion effect
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    1.0f, Level.ExplosionInteraction.NONE);

            // Chance to propagate to neighbors
            float propagationChance = 0.3f; // Base 30% chance

            for (BlockPos neighbor : network.getNeighbors(pos)) {
                if (!destroyed.contains(neighbor) &&
                        level.getRandom().nextFloat() < propagationChance) {
                    toProcess.add(neighbor);
                }
            }
        }

        // Notify network manager to handle the destroyed nodes
        // Will be called from the main network tick
    }

    /**
     * Check for interference at junction points
     */
    public void detectInterference(Map<BlockPos, ResonanceState> computedStates) {
        for (BlockPos pos : network.getAllPositions()) {
            Set<BlockPos> neighbors = network.getNeighbors(pos);
            if (neighbors.size() < 2) continue; // Not a junction

            // Check if multiple waves are meeting here
            int activeNeighbors = 0;
            for (BlockPos neighbor : neighbors) {
                ResonanceState state = computedStates.get(neighbor);
                if (state != null && state.isActive()) {
                    activeNeighbors++;
                }
            }

            if (activeNeighbors >= 2) {
                // Interference is happening - already handled in combine()
                // Could add visual effects here
            }
        }
    }

    /**
     * Represents a step in the BFS propagation
     */
    private record PropagationStep(BlockPos pos, float distance, ResonanceState state) {
    }
}
