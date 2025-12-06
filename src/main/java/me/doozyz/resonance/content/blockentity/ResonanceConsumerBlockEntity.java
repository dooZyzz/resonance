package me.doozyz.resonance.content.blockentity;

import com.mojang.serialization.Codec;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.resonance.IResonanceConsumer;
import me.doozyz.resonance.content.resonance.ResonanceRequirement;
import me.doozyz.resonance.content.resonance.ResonanceState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.EnumSet;
import java.util.Set;

/**
 * Abstract base class for all resonance consumers (machines)
 */
public abstract class ResonanceConsumerBlockEntity extends BlockEntity implements IResonanceConsumer {

    protected final CrystalTier tier;
    protected ResonanceState receivedState = ResonanceState.EMPTY;
    protected final ResonanceRequirement requirement;

    // Power state
    protected boolean powered = false;
    protected float efficiency = 0;

    // Progress tracking for recipes/processes
    protected int progress = 0;
    protected int maxProgress = 100;

    // For client-side rendering
    protected float renderEfficiency = 0;

    public ResonanceConsumerBlockEntity(BlockEntityType<?> type, BlockPos pos,
                                        BlockState state, CrystalTier tier,
                                        ResonanceRequirement requirement) {
        super(type, pos, state);
        this.tier = tier;
        this.requirement = requirement;
    }

    @Override
    public CrystalTier getTier() {
        return tier;
    }

    @Override
    public ResonanceRequirement getRequirement() {
        return requirement;
    }

    @Override
    public ResonanceState getResonanceState() {
        return receivedState;
    }

    @Override
    public void receiveResonance(Direction from, ResonanceState state) {
        this.receivedState = state;
        updatePowerState();
    }

    @Override
    public void onNetworkChanged() {
        setChanged();
    }

    /**
     * Update power state based on received resonance
     */
    protected void updatePowerState() {
        boolean wasPowered = powered;
        float oldEfficiency = efficiency;

        powered = requirement.isSatisfiedBy(receivedState);
        efficiency = requirement.getEfficiency(receivedState);

        // Sync to client if state changed significantly
        if (wasPowered != powered || Math.abs(oldEfficiency - efficiency) > 0.05f) {
            setChanged();
        }
    }

    /**
     * Server-side tick - process items/recipes
     */
    public void serverTick() {
        if (powered && canProcess()) {
            // Progress based on efficiency (50-100% of base rate)
            int progressRate = (int) Math.ceil(efficiency * getBaseProgressRate());
            progress += progressRate;

            if (progress >= maxProgress) {
                completeProcess();
                progress = 0;
            }

            setChanged();
        } else if (progress > 0 && shouldDecayProgress()) {
            // Optional: decay progress if not powered
            progress = Math.max(0, progress - 1);
            setChanged();
        }
    }

    /**
     * Client-side tick - smooth animations
     */
    public void clientTick() {
        // Smooth efficiency for visual effects
        renderEfficiency = net.minecraft.util.Mth.lerp(0.1f, renderEfficiency, efficiency);

        // Spawn visual effects when powered
        // Check if actually receiving resonance (not just powered flag)
        if (receivedState.isActive() && receivedState.amplitude() >= requirement.minAmplitude()
                && level != null && level.getRandom().nextFloat() < 0.05f) {
            spawnProcessingParticles();
        }
    }

    /**
     * Spawn particles when machine is processing
     * Subclasses can override for custom effects
     */
    protected void spawnProcessingParticles() {
        if (level == null || !level.isClientSide()) return;

        // Color based on efficiency
        net.minecraft.core.particles.ParticleOptions particle =
                efficiency > 0.8f ? net.minecraft.core.particles.ParticleTypes.ENCHANT :
                        efficiency > 0.5f ? net.minecraft.core.particles.ParticleTypes.PORTAL :
                                net.minecraft.core.particles.ParticleTypes.SMOKE;

        double x = worldPosition.getX() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.5;
        double y = worldPosition.getY() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.5;
        double z = worldPosition.getZ() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.5;

        level.addParticle(particle, x, y, z, 0, 0.02, 0);
    }

    // Abstract methods for subclasses

    /**
     * Check if this machine can currently process
     */
    protected abstract boolean canProcess();

    /**
     * Called when processing completes
     */
    protected abstract void completeProcess();

    /**
     * Base progress rate (in ticks to complete)
     * Actual rate is multiplied by efficiency
     */
    protected int getBaseProgressRate() {
        return 1;
    }

    /**
     * Should progress decay when unpowered?
     */
    protected boolean shouldDecayProgress() {
        return false; // Default: progress is preserved
    }

    /**
     * Base amplitude consumption per tick
     * Actual consumption is multiplied by efficiency
     */
    @Override
    public float getAmplitudeConsumption() {
        if (!powered || !canProcess()) return 0;
        return getBaseConsumption() * efficiency;
    }

    protected abstract float getBaseConsumption();

    /**
     * Get input face(s) for this consumer
     */
    @Override
    public Set<Direction> getConnectionDirections() {
        return EnumSet.of(getInputFace());
    }

    /**
     * Get the primary input face
     */
    protected Direction getInputFace() {
        return Direction.DOWN; // Default: input from bottom
    }

    // Helper methods for UI

    /**
     * Get progress as a scaled value for progress bars
     */
    public int getScaledProgress(int pixels) {
        if (maxProgress == 0) return 0;
        return (progress * pixels) / maxProgress;
    }

    public boolean isPowered() {
        return powered;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public float getRenderEfficiency() {
        return renderEfficiency;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    // NBT Serialization (1.21 API)

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("powered", Codec.BOOL, powered);
        output.store("efficiency", Codec.FLOAT, efficiency);
        output.store("progress", Codec.INT, progress);
        output.store("maxProgress", Codec.INT, maxProgress);
        output.store("receivedFrequency", Codec.FLOAT, receivedState.frequency());
        output.store("receivedAmplitude", Codec.FLOAT, receivedState.amplitude());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        powered = input.read("powered", Codec.BOOL).orElse(false);
        efficiency = input.read("efficiency", Codec.FLOAT).orElse(0f);
        progress = input.read("progress", Codec.INT).orElse(0);
        maxProgress = input.read("maxProgress", Codec.INT).orElse(100);

        float freq = input.read("receivedFrequency", Codec.FLOAT).orElse(0f);
        float amp = input.read("receivedAmplitude", Codec.FLOAT).orElse(0f);
        receivedState = new ResonanceState(freq, amp, 0);

        // Initialize render values
        renderEfficiency = efficiency;
    }
}
