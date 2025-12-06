package me.doozyz.resonance.content.blockentity;

import com.mojang.serialization.Codec;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.EnumSet;
import java.util.Set;

/**
 * Passive generator that slowly collects ambient magical energy
 */
public class AmbientCollectorBlockEntity extends ResonanceGeneratorBlockEntity {

    // Energy buffer
    private float collectedEnergy = 0;

    // Constants
    private static final float COLLECTION_RATE = 0.5f;  // Per tick base rate
    private static final float MAX_STORAGE = 100f;
    private static final float MIN_OUTPUT = 10f;  // Minimum energy to start outputting

    public AmbientCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AMBIENT_COLLECTOR.get(), pos, state, CrystalTier.RAW);
    }

    @Override
    protected boolean checkCanGenerate() {
        return collectedEnergy > MIN_OUTPUT;
    }

    @Override
    protected void updateGeneratedResonance() {
        if (level == null) return;

        // Debug logging every 5 seconds
        if (level.getGameTime() % 100 == 0) {
            me.doozyz.resonance.support.ModRef.info("Ambient Collector at {} - Energy: {}, Active: {}, Freq: {}, Amp: {}",
                    worldPosition, collectedEnergy, active, generatedFrequency, generatedAmplitude);
        }

        // Base collection rate
        float rate = COLLECTION_RATE;

        // Environmental modifiers
        rate *= getBiomeModifier();
        rate *= getTimeModifier();
        rate *= getMoonPhaseModifier();
        rate *= getDimensionModifier();

        // Collect energy
        collectedEnergy = Math.min(MAX_STORAGE, collectedEnergy + rate);

        // Set output
        generatedFrequency = 15f;  // Low, stable frequency
        generatedAmplitude = collectedEnergy * 0.5f;  // Max 50 A when full
    }

    /**
     * Get biome-based collection modifier
     */
    private float getBiomeModifier() {
        if (level.getBiome(worldPosition).is(me.doozyz.resonance.content.ModTags.Biomes.HIGH_MAGIC)) {
            return 2.0f;  // 2x bonus in high magic biomes
        }
        return 1.0f;
    }

    /**
     * Get time-based collection modifier
     */
    private float getTimeModifier() {
        // Night is from 12600 to 23400 ticks
        assert level != null;
        long dayTime = level.getDayTime() % 24000L;
        if (dayTime >= 12600 && dayTime <= 23400) {
            return 1.5f;  // 50% bonus at night
        }
        return 1.0f;
    }

    /**
     * Get moon phase collection modifier
     */
    private float getMoonPhaseModifier() {
        float moonBrightness = level.getMoonBrightness();
        return 1.0f + moonBrightness * 0.5f;  // 0-50% bonus based on moon
    }

    /**
     * Get dimension-based collection modifier
     */
    private float getDimensionModifier() {
        if (level.dimension() == Level.NETHER) {
            return 0.5f;  // Less ambient magic in Nether
        } else if (level.dimension() == Level.END) {
            return 0.3f;  // Even less in End
        }
        return 1.0f;  // Normal in Overworld
    }

    @Override
    protected Direction getOutputFace() {
        return Direction.DOWN;  // Output from bottom
    }

    @Override
    public Set<Direction> getConnectionDirections() {
        return EnumSet.of(Direction.DOWN);
    }

    @Override
    protected void spawnGeneratorParticles() {
        if (level == null || !level.isClientSide()) return;

        // Sparkles falling into collector
        if (collectedEnergy < MAX_STORAGE) {
            double x = worldPosition.getX() + 0.3 + level.getRandom().nextDouble() * 0.4;
            double y = worldPosition.getY() + 0.8;
            double z = worldPosition.getZ() + 0.3 + level.getRandom().nextDouble() * 0.4;

            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.GLOW,
                    x, y, z,
                    0, -0.05, 0  // Fall downward
            );
        }

    }

    // NBT Serialization

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("collectedEnergy", Codec.FLOAT, collectedEnergy);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        collectedEnergy = input.read("collectedEnergy", Codec.FLOAT).orElse(0f);
    }

    // Getters for UI/rendering

    public float getCollectedEnergy() {
        return collectedEnergy;
    }

    public float getStoragePercent() {
        return collectedEnergy / MAX_STORAGE;
    }
}
