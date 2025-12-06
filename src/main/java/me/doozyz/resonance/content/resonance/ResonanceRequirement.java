package me.doozyz.resonance.content.resonance;

/**
 * Defines the resonance requirements for a machine to operate
 */
public record ResonanceRequirement(
        float minFrequency,
        float maxFrequency,
        float minAmplitude,
        boolean requiresExactFrequency  // false = range, true = exact match
) {

    /**
     * Machine accepts any frequency, just needs minimum amplitude
     */
    public static ResonanceRequirement any(float minAmplitude) {
        return new ResonanceRequirement(0, Float.MAX_VALUE, minAmplitude, false);
    }

    /**
     * Machine accepts a range of frequencies with minimum amplitude
     */
    public static ResonanceRequirement range(float minFreq, float maxFreq, float minAmp) {
        return new ResonanceRequirement(minFreq, maxFreq, minAmp, false);
    }

    /**
     * Machine requires an exact frequency (within tolerance) with minimum amplitude
     */
    public static ResonanceRequirement exact(float frequency, float minAmp, float tolerance) {
        return new ResonanceRequirement(
                frequency - tolerance,
                frequency + tolerance,
                minAmp,
                true
        );
    }

    /**
     * Check if a resonance state satisfies this requirement
     */
    public boolean isSatisfiedBy(ResonanceState state) {
        if (state.amplitude() < minAmplitude) return false;
        if (state.frequency() < minFrequency) return false;
        return !(state.frequency() > maxFrequency);
    }

    /**
     * Calculate efficiency based on how well the state matches the requirement
     * @return 0.0 to 1.0, where 1.0 is perfect match
     */
    public float getEfficiency(ResonanceState state) {
        if (!isSatisfiedBy(state)) return 0;

        if (requiresExactFrequency) {
            // Exact frequency requirement: efficiency drops with deviation
            float center = (minFrequency + maxFrequency) / 2;
            float deviation = Math.abs(state.frequency() - center);
            float tolerance = (maxFrequency - minFrequency) / 2;

            if (tolerance < 0.01f) return 1.0f; // Perfect match

            // 50-100% efficiency based on how close to center frequency
            return 1.0f - (deviation / tolerance) * 0.5f;
        }

        // Range requirement: as long as it's in range, full efficiency
        return 1.0f;
    }

    /**
     * Get the optimal frequency for this requirement
     */
    public float getOptimalFrequency() {
        return (minFrequency + maxFrequency) / 2;
    }
}
