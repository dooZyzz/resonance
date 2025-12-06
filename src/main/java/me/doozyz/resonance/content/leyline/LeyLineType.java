package me.doozyz.resonance.content.leyline;

public enum LeyLineType {
    MINOR(30, 50),
    MAJOR(60, 200),
    NEXUS(100, 500);

    private final int frequency;
    private final int amplitude;

    LeyLineType(int frequency, int amplitude) {
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getAmplitude() {
        return amplitude;
    }
}
