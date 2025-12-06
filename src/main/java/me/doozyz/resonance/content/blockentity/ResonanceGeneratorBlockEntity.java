package me.doozyz.resonance.content.blockentity;

import com.mojang.serialization.Codec;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.resonance.IResonanceGenerator;
import me.doozyz.resonance.content.resonance.ResonanceNetworkManager;
import me.doozyz.resonance.content.resonance.ResonanceState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.EnumSet;
import java.util.Set;

/**
 * Abstract base class for all resonance generators
 */
public abstract class ResonanceGeneratorBlockEntity extends BlockEntity implements IResonanceGenerator {

    protected final CrystalTier tier;
    protected float generatedFrequency;
    protected float generatedAmplitude;
    protected boolean active = false;

    // For smooth client-side animation
    protected float targetAmplitude = 0;
    protected float currentAnimatedAmplitude = 0;

    // Track last state to detect changes
    private float lastFrequency = 0;
    private float lastAmplitude = 0;

    public ResonanceGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, CrystalTier tier) {
        super(type, pos, state);
        this.tier = tier;
    }

    @Override
    public CrystalTier getTier() {
        return tier;
    }

    @Override
    public ResonanceState getResonanceState() {
        return getGeneratedResonance();
    }

    @Override
    public ResonanceState getGeneratedResonance() {
        if (!canGenerate()) return ResonanceState.EMPTY;
        return new ResonanceState(generatedFrequency, generatedAmplitude, 0);
    }

    @Override
    public boolean canGenerate() {
        return active && checkCanGenerate();
    }

    @Override
    public boolean isSource() {
        return true;
    }

    @Override
    public void receiveResonance(Direction from, ResonanceState state) {
        // Generators don't receive resonance, they produce it
    }

    @Override
    public void onNetworkChanged() {
        // Generator state doesn't change based on network
        setChanged();
    }

    /**
     * Server-side tick - manages generation state
     */
    public void serverTick() {
        boolean wasActive = active;
        float oldFrequency = generatedFrequency;
        float oldAmplitude = generatedAmplitude;

        // Subclass determines if generation is possible
        active = checkCanGenerate();

        if (active) {
            // Calculate output based on subclass logic
            updateGeneratedResonance();

            // Clamp to tier limits
            generatedFrequency = Math.min(generatedFrequency, tier.getMaxFrequency());
            generatedAmplitude = Math.min(generatedAmplitude, tier.getMaxAmplitude());
        } else {
            generatedAmplitude = 0;
        }

        // Notify network if state changed significantly
        if (wasActive != active || hasSignificantChange(oldFrequency, oldAmplitude)) {
            notifyNetworkChanged();
            lastFrequency = generatedFrequency;
            lastAmplitude = generatedAmplitude;
        }

        targetAmplitude = active ? generatedAmplitude : 0;
    }

    /**
     * Client-side tick - smooth animations and visual effects
     */
    public void clientTick() {
        // Smooth amplitude animation
        currentAnimatedAmplitude = Mth.lerp(0.1f, currentAnimatedAmplitude, targetAmplitude);

        // Visual effects when active
        if (active && level != null && level.getRandom().nextFloat() < 0.1f) {
            spawnGeneratorParticles();
        }
    }

    /**
     * Spawn common generator particles
     * Subclasses can override for custom effects
     */
    protected void spawnGeneratorParticles() {
        if (level == null || !level.isClientSide()) return;

        // Spawn ambient particles around active generator
        double x = worldPosition.getX() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.5;
        double y = worldPosition.getY() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.5;
        double z = worldPosition.getZ() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.5;

        level.addParticle(
                net.minecraft.core.particles.ParticleTypes.END_ROD,
                x, y, z,
                0, 0.02, 0
        );
    }

    /**
     * Check if state change is significant enough to notify network
     */
    private boolean hasSignificantChange(float oldFreq, float oldAmp) {
        // Frequency change > 1 Hz
        if (Math.abs(generatedFrequency - oldFreq) > 1.0f) return true;

        // Amplitude change > 5%
        if (oldAmp > 0.01f) {
            float change = Math.abs(generatedAmplitude - oldAmp) / oldAmp;
            return change > 0.05f;
        } else if (generatedAmplitude > 0.01f) {
            return true; // Going from 0 to active
        }

        return false;
    }

    /**
     * Notify the network manager that this generator's state changed
     */
    protected void notifyNetworkChanged() {
        if (level instanceof ServerLevel serverLevel) {
            ResonanceNetworkManager.get(serverLevel).markNetworkDirty(worldPosition);
            setChanged();
        }
    }

    // Abstract methods for subclasses to implement

    /**
     * Check if this generator can currently produce power
     * Called every server tick
     */
    protected abstract boolean checkCanGenerate();

    /**
     * Update the generated frequency and amplitude
     * Called when checkCanGenerate() returns true
     */
    protected abstract void updateGeneratedResonance();

    /**
     * Get the output face(s) for this generator
     * Most generators output from a single face
     */
    @Override
    public Set<Direction> getConnectionDirections() {
        return EnumSet.of(getOutputFace());
    }

    /**
     * Get the primary output face
     * Override for specific generator orientation
     */
    protected Direction getOutputFace() {
        return Direction.UP; // Default: output from top
    }

    // NBT Serialization

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("generatedFrequency", Codec.FLOAT, generatedFrequency);
        output.store("generatedAmplitude", Codec.FLOAT, generatedAmplitude);
        output.store("active", Codec.BOOL, active);
        output.store("targetAmplitude", Codec.FLOAT, targetAmplitude);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        generatedFrequency = input.read("generatedFrequency", Codec.FLOAT).orElse(0f);
        generatedAmplitude = input.read("generatedAmplitude", Codec.FLOAT).orElse(0f);
        active = input.read("active", Codec.BOOL).orElse(false);
        targetAmplitude = input.read("targetAmplitude", Codec.FLOAT).orElse(0f);

        // Initialize animation values
        currentAnimatedAmplitude = targetAmplitude;
    }

    // Getters for rendering/UI

    public boolean isActive() {
        return active;
    }

    public float getGeneratedFrequency() {
        return generatedFrequency;
    }

    public float getGeneratedAmplitude() {
        return generatedAmplitude;
    }

    public float getAnimatedAmplitude() {
        return currentAnimatedAmplitude;
    }
}
