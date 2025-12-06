package me.doozyz.resonance.content.resonance;

/**
 * Interface for blocks that generate resonance (power sources)
 */
public interface IResonanceGenerator extends IResonanceNode {

    @Override
    default boolean isSource() {
        return true;
    }

    /**
     * What resonance does this generator produce?
     * @return The generated resonance state
     */
    ResonanceState getGeneratedResonance();

    /**
     * Can this generator currently produce power?
     * @return true if generation is possible
     */
    boolean canGenerate();
}
