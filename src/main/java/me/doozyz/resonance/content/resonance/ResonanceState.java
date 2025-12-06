package me.doozyz.resonance.content.resonance;

public record ResonanceState(
        float frequency,    // Hz, 0 = no signal
        float amplitude,    // A, power level
        float phase         // radians, 0-2π
) {
    public static final ResonanceState EMPTY = new ResonanceState(0, 0, 0);

    public boolean isActive() {
        return amplitude > 0.01f;
    }

    /**
     * Combine two waves using superposition (constructive/destructive interference)
     */
    public ResonanceState combine(ResonanceState other) {
        if (!this.isActive()) return other;
        if (!other.isActive()) return this;

        // Same frequency: phasor addition
        if (Math.abs(this.frequency - other.frequency) < 0.1f) {
            // Convert to rectangular form
            float ax = this.amplitude * (float) Math.cos(this.phase);
            float ay = this.amplitude * (float) Math.sin(this.phase);
            float bx = other.amplitude * (float) Math.cos(other.phase);
            float by = other.amplitude * (float) Math.sin(other.phase);

            // Add vectors
            float rx = ax + bx;
            float ry = ay + by;

            // Convert back to polar form
            float resultAmp = (float) Math.sqrt(rx * rx + ry * ry);
            float resultPhase = (float) Math.atan2(ry, rx);

            return new ResonanceState(this.frequency, resultAmp, resultPhase);
        }

        // Different frequencies: beat frequency (complex case)
        // For now, return the stronger signal
        return this.amplitude > other.amplitude ? this : other;
    }

    /**
     * Apply attenuation over distance
     */
    public ResonanceState attenuate(float attenuationPerBlock, float distance) {
        if (!isActive()) return EMPTY;

        float remaining = amplitude * (float) Math.pow(1 - attenuationPerBlock, distance);
        if (remaining < 0.01f) return EMPTY;

        return new ResonanceState(frequency, remaining, phase);
    }

    /**
     * Advance phase based on time (for animation/visualization)
     */
    public ResonanceState tick(float deltaTime) {
        if (!isActive()) return this;

        // Phase advances by 2π * frequency per second
        float phaseAdvance = 2 * (float) Math.PI * frequency * deltaTime;
        float newPhase = (phase + phaseAdvance) % (2 * (float) Math.PI);

        return new ResonanceState(frequency, amplitude, newPhase);
    }

    /**
     * Create a new state with a different phase
     */
    public ResonanceState withPhase(float newPhase) {
        return new ResonanceState(frequency, amplitude, newPhase);
    }

    /**
     * Create a new state with a different amplitude
     */
    public ResonanceState withAmplitude(float newAmplitude) {
        return new ResonanceState(frequency, newAmplitude, phase);
    }

    /**
     * Create a new state with a different frequency
     */
    public ResonanceState withFrequency(float newFrequency) {
        return new ResonanceState(newFrequency, amplitude, phase);
    }
}
