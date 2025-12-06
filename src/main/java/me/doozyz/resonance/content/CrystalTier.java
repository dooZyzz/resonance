package me.doozyz.resonance.content;

public enum CrystalTier {
    RAW(50, 100, 0.05f),
    REFINED(200, 500, 0.02f),
    SYNTHETIC(500, 2000, 0.005f),
    LEY_INFUSED(1000, 10000, 0.001f);

    private final int maxFrequency;
    private final int maxAmplitude;
    private final float attenuationPerBlock;

    CrystalTier(int maxFrequency, int maxAmplitude, float attenuationPerBlock) {
        this.maxFrequency = maxFrequency;
        this.maxAmplitude = maxAmplitude;
        this.attenuationPerBlock = attenuationPerBlock;
    }

    public int getMaxFrequency() {
        return maxFrequency;
    }

    public int getMaxAmplitude() {
        return maxAmplitude;
    }

    public float getAttenuationPerBlock() {
        return attenuationPerBlock;
    }
}
