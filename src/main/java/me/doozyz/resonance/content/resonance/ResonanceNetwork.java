package me.doozyz.resonance.content.resonance;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.*;

/**
 * Represents a single connected resonance network
 */
public class ResonanceNetwork {
    private final UUID id;
    private final Level level;

    // Graph structure
    private final Map<BlockPos, IResonanceNode> nodes = new HashMap<>();
    private final Map<BlockPos, Set<BlockPos>> adjacency = new HashMap<>();

    // Computed state after propagation
    private final Map<BlockPos, ResonanceState> computedStates = new HashMap<>();
    private boolean needsRecalculation = true;

    // Sources and sinks for efficient iteration
    private final Set<BlockPos> sources = new HashSet<>();
    private final Set<BlockPos> sinks = new HashSet<>();

    public ResonanceNetwork(UUID id, Level level) {
        this.id = id;
        this.level = level;
    }

    public UUID getId() {
        return id;
    }

    public Level getLevel() {
        return level;
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public Set<BlockPos> getAllPositions() {
        return new HashSet<>(nodes.keySet());
    }

    public IResonanceNode getNode(BlockPos pos) {
        return nodes.get(pos);
    }

    public Set<BlockPos> getNeighbors(BlockPos pos) {
        return adjacency.getOrDefault(pos, Set.of());
    }

    /**
     * Add a node to this network
     */
    public void addNode(BlockPos pos, IResonanceNode node) {
        nodes.put(pos, node);
        adjacency.put(pos, new HashSet<>());

        // Update adjacency for this node and neighbors
        for (Direction dir : node.getConnectionDirections()) {
            BlockPos neighbor = pos.relative(dir);
            if (nodes.containsKey(neighbor)) {
                IResonanceNode neighborNode = nodes.get(neighbor);
                if (neighborNode.getConnectionDirections().contains(dir.getOpposite())) {
                    adjacency.get(pos).add(neighbor);
                    adjacency.get(neighbor).add(pos);
                }
            }
        }

        // Track sources and sinks
        if (node.isSource()) sources.add(pos);
        if (node.isSink()) sinks.add(pos);

        needsRecalculation = true;
    }

    /**
     * Remove a node from this network
     */
    public void removeNode(BlockPos pos) {
        nodes.remove(pos);

        // Remove from adjacency
        Set<BlockPos> neighbors = adjacency.remove(pos);
        if (neighbors != null) {
            for (BlockPos neighbor : neighbors) {
                Set<BlockPos> neighborAdj = adjacency.get(neighbor);
                if (neighborAdj != null) {
                    neighborAdj.remove(pos);
                }
            }
        }

        sources.remove(pos);
        sinks.remove(pos);
        computedStates.remove(pos);

        needsRecalculation = true;
    }

    /**
     * Check if network needs split check after node removal
     */
    public boolean needsSplitCheck() {
        return nodes.size() > 1;
    }

    /**
     * Clear all nodes (used during splitting)
     */
    public void clear() {
        nodes.clear();
        adjacency.clear();
        computedStates.clear();
        sources.clear();
        sinks.clear();
        needsRecalculation = true;
    }

    /**
     * Recalculate resonance distribution across network
     */
    public void recalculate() {
        if (!needsRecalculation) return;
        needsRecalculation = false;

        computedStates.clear();

        // Propagate from all sources (will be implemented in Phase 2.3)
        // For now, just mark as needing implementation
    }

    /**
     * Tick the network - advance phases and apply consumption
     */
    public void tick() {
        // Phase advancement
        float deltaTime = 1.0f / 20.0f; // One tick = 1/20 second

        for (Map.Entry<BlockPos, ResonanceState> entry : computedStates.entrySet()) {
            ResonanceState ticked = entry.getValue().tick(deltaTime);
            entry.setValue(ticked);
        }

        // Apply consumption from sinks
        for (BlockPos sinkPos : sinks) {
            IResonanceConsumer consumer = (IResonanceConsumer) nodes.get(sinkPos);
            if (consumer != null && consumer.isPowered()) {
                ResonanceState current = computedStates.get(sinkPos);
                if (current != null) {
                    float consumption = consumer.getAmplitudeConsumption();
                    float newAmp = Math.max(0, current.amplitude() - consumption);
                    computedStates.put(sinkPos, current.withAmplitude(newAmp));
                }
            }
        }
    }

    /**
     * Mark network as needing recalculation
     */
    public void markDirty() {
        needsRecalculation = true;
    }
}
