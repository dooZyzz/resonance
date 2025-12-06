package me.doozyz.resonance.content.resonance;

/**
 * Interface for blocks that consume resonance (machines)
 */
public interface IResonanceConsumer extends IResonanceNode {

    @Override
    default boolean isSink() {
        return true;
    }

    /**
     * What does this machine need to operate?
     * @return The resonance requirement
     */
    ResonanceRequirement getRequirement();

    /**
     * How much amplitude does this consume per tick?
     * @return Amplitude consumption rate
     */
    float getAmplitudeConsumption();

    /**
     * Is the current resonance sufficient?
     * @return true if requirements are met
     */
    default boolean isPowered() {
        return getRequirement().isSatisfiedBy(getResonanceState());
    }
}
