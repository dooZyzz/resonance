package me.doozyz.resonance.content.resonance;

import me.doozyz.resonance.content.CrystalTier;
import net.minecraft.core.Direction;

import java.util.Set;

/**
 * Interface for blocks that participate in resonance networks
 */
public interface IResonanceNode {

    /**
     * What tier is this node? Determines max frequency, amplitude, and attenuation.
     */
    CrystalTier getTier();

    /**
     * Current resonance state at this node
     */
    ResonanceState getResonanceState();

    /**
     * Receive resonance from adjacent node
     * @param from The direction the resonance is coming from
     * @param state The resonance state being received
     */
    void receiveResonance(Direction from, ResonanceState state);

    /**
     * Which directions can this node connect to?
     * @return Set of valid connection directions
     */
    Set<Direction> getConnectionDirections();

    /**
     * Is this node a source (generator)?
     */
    default boolean isSource() {
        return false;
    }

    /**
     * Is this node a sink (consumer)?
     */
    default boolean isSink() {
        return false;
    }

    /**
     * Called when network topology changes (node added/removed)
     */
    void onNetworkChanged();
}
