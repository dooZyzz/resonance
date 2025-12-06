package me.doozyz.resonance.content.resonance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

/**
 * Manages all resonance networks in a level
 */
public class ResonanceNetworkManager extends SavedData {

    private static final Codec<ResonanceNetworkManager> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    // For now, just save empty data - networks will be rebuilt
                    Codec.BOOL.optionalFieldOf("initialized", true).forGetter(m -> true)
            ).apply(instance, initialized -> new ResonanceNetworkManager())
    );

    private static final SavedDataType<ResonanceNetworkManager> TYPE = new SavedDataType<>(
            "resonance_networks",
            context -> new ResonanceNetworkManager(context.levelOrThrow()),
            context -> CODEC
    );

    private Level level;
    private final Map<UUID, ResonanceNetwork> networks = new HashMap<>();
    private final Map<BlockPos, UUID> positionToNetwork = new HashMap<>();
    private final Set<UUID> networksNeedingUpdate = new HashSet<>();

    private ResonanceNetworkManager() {
        // For codec deserialization
    }

    public ResonanceNetworkManager(Level level) {
        this.level = level;
    }

    /**
     * Get the network manager for a level
     */
    public static ResonanceNetworkManager get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            ResonanceNetworkManager manager = serverLevel.getDataStorage().computeIfAbsent(TYPE);
            manager.level = serverLevel;
            return manager;
        }
        throw new IllegalArgumentException("Cannot get manager for client level");
    }

    /**
     * Called when a resonance node is placed
     */
    public void onNodePlaced(BlockPos pos, IResonanceNode node) {
        // Check for adjacent networks
        Set<UUID> adjacentNetworks = new HashSet<>();

        for (Direction dir : node.getConnectionDirections()) {
            BlockPos adjacent = pos.relative(dir);
            UUID adjacentNetwork = positionToNetwork.get(adjacent);
            if (adjacentNetwork != null) {
                // Verify the adjacent node can connect back to us
                if (canConnect(adjacent, dir.getOpposite())) {
                    adjacentNetworks.add(adjacentNetwork);
                }
            }
        }

        if (adjacentNetworks.isEmpty()) {
            // Create new network with just this node
            createNetwork(pos, node);
        } else if (adjacentNetworks.size() == 1) {
            // Add to existing network
            UUID networkId = adjacentNetworks.iterator().next();
            addToNetwork(networkId, pos, node);
        } else {
            // Merge multiple networks
            mergeNetworks(adjacentNetworks, pos, node);
        }

        setDirty();
    }

    /**
     * Called when a resonance node is removed
     */
    public void onNodeRemoved(BlockPos pos) {
        UUID networkId = positionToNetwork.remove(pos);
        if (networkId == null) return;

        ResonanceNetwork network = networks.get(networkId);
        if (network == null) return;

        network.removeNode(pos);

        // Check if network needs to be split
        if (network.needsSplitCheck()) {
            splitNetworkIfNeeded(network);
        }

        // Remove empty networks
        if (network.isEmpty()) {
            networks.remove(networkId);
        }

        setDirty();
    }

    /**
     * Get network at position
     */
    public ResonanceNetwork getNetworkAt(BlockPos pos) {
        UUID networkId = positionToNetwork.get(pos);
        return networkId != null ? networks.get(networkId) : null;
    }

    /**
     * Tick all networks
     */
    public void tick() {
        // Update networks that need recalculation
        for (UUID networkId : networksNeedingUpdate) {
            ResonanceNetwork network = networks.get(networkId);
            if (network != null) {
                network.recalculate();
            }
        }
        networksNeedingUpdate.clear();

        // Tick all networks
        for (ResonanceNetwork network : networks.values()) {
            network.tick();
        }
    }

    /**
     * Mark a network as needing update
     */
    public void markNetworkDirty(BlockPos pos) {
        UUID networkId = positionToNetwork.get(pos);
        if (networkId != null) {
            networksNeedingUpdate.add(networkId);
        }
    }

    private void createNetwork(BlockPos pos, IResonanceNode node) {
        UUID id = UUID.randomUUID();
        ResonanceNetwork network = new ResonanceNetwork(id, level);
        network.addNode(pos, node);
        networks.put(id, network);
        positionToNetwork.put(pos, id);
    }

    private void addToNetwork(UUID networkId, BlockPos pos, IResonanceNode node) {
        ResonanceNetwork network = networks.get(networkId);
        if (network != null) {
            network.addNode(pos, node);
            positionToNetwork.put(pos, networkId);
            networksNeedingUpdate.add(networkId);
        }
    }

    private void mergeNetworks(Set<UUID> toMerge, BlockPos newPos, IResonanceNode newNode) {
        // Pick the largest network as the base
        UUID baseId = toMerge.stream()
                .max(Comparator.comparingInt(id -> networks.get(id).size()))
                .orElseThrow();

        ResonanceNetwork base = networks.get(baseId);

        // Merge others into base
        for (UUID otherId : toMerge) {
            if (otherId.equals(baseId)) continue;

            ResonanceNetwork other = networks.remove(otherId);
            for (BlockPos pos : other.getAllPositions()) {
                base.addNode(pos, other.getNode(pos));
                positionToNetwork.put(pos, baseId);
            }
        }

        // Add the new node
        base.addNode(newPos, newNode);
        positionToNetwork.put(newPos, baseId);

        // Mark for recalculation
        networksNeedingUpdate.add(baseId);
    }

    private void splitNetworkIfNeeded(ResonanceNetwork network) {
        // Use BFS to find connected components
        Set<BlockPos> allPositions = new HashSet<>(network.getAllPositions());
        List<Set<BlockPos>> components = new ArrayList<>();

        while (!allPositions.isEmpty()) {
            BlockPos start = allPositions.iterator().next();
            Set<BlockPos> component = new HashSet<>();
            Queue<BlockPos> queue = new LinkedList<>();
            queue.add(start);

            while (!queue.isEmpty()) {
                BlockPos current = queue.poll();
                if (!allPositions.remove(current)) continue;
                component.add(current);

                IResonanceNode node = network.getNode(current);
                if (node != null) {
                    for (Direction dir : node.getConnectionDirections()) {
                        BlockPos neighbor = current.relative(dir);
                        if (allPositions.contains(neighbor) && canConnect(neighbor, dir.getOpposite())) {
                            queue.add(neighbor);
                        }
                    }
                }
            }

            components.add(component);
        }

        // If only one component, no split needed
        if (components.size() <= 1) return;

        // Keep the largest component in the original network
        components.sort((a, b) -> b.size() - a.size());
        Set<BlockPos> keepInOriginal = components.remove(0);

        // Clear the original network and re-add kept positions
        UUID originalId = network.getId();
        network.clear();
        for (BlockPos pos : keepInOriginal) {
            IResonanceNode node = getNodeFromLevel(pos);
            if (node != null) {
                network.addNode(pos, node);
            }
        }

        // Create new networks for remaining components
        for (Set<BlockPos> component : components) {
            UUID newId = UUID.randomUUID();
            ResonanceNetwork newNetwork = new ResonanceNetwork(newId, level);

            for (BlockPos pos : component) {
                IResonanceNode node = getNodeFromLevel(pos);
                if (node != null) {
                    newNetwork.addNode(pos, node);
                    positionToNetwork.put(pos, newId);
                }
            }

            networks.put(newId, newNetwork);
        }
    }

    private boolean canConnect(BlockPos pos, Direction fromDir) {
        IResonanceNode node = getNodeFromLevel(pos);
        return node != null && node.getConnectionDirections().contains(fromDir);
    }

    private IResonanceNode getNodeFromLevel(BlockPos pos) {
        // Get node from block entity at position
        // TODO: Implement when block entities are created
        return null;
    }
}
