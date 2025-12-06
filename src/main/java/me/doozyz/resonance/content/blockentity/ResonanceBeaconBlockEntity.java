package me.doozyz.resonance.content.blockentity;

import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.resonance.ResonanceRequirement;
import me.doozyz.resonance.content.resonance.ResonanceState;
import me.doozyz.resonance.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.Set;

/**
 * Simple test consumer - emits particles and light when powered
 */
public class ResonanceBeaconBlockEntity extends ResonanceConsumerBlockEntity {

    private static final int EFFECT_INTERVAL = 20;  // Once per second

    public ResonanceBeaconBlockEntity(BlockPos pos, BlockState state) {
        super(
                ModBlockEntities.RESONANCE_BEACON.get(),
                pos,
                state,
                CrystalTier.REFINED,
                ResonanceRequirement.any(50)  // Just needs 50+ amplitude
        );
        this.maxProgress = EFFECT_INTERVAL;
        me.doozyz.resonance.support.ModRef.info("Created ResonanceBeaconBlockEntity at {}", pos);
    }

    @Override
    protected boolean canProcess() {
        return true;  // Always "processing" when powered
    }

    @Override
    protected void updatePowerState() {
        boolean wasPowered = powered;
        super.updatePowerState();

        // Debug logging every 5 seconds
        if (level != null && level.getGameTime() % 100 == 0) {
            me.doozyz.resonance.support.ModRef.info("Beacon at {} - Powered: {}, Efficiency: {}, Received: {} Hz / {} A",
                    worldPosition, powered, efficiency, receivedState.frequency(), receivedState.amplitude());
        }

        // Update block state and sync to client when power state changes
        if (level != null && !level.isClientSide() && wasPowered != powered) {
            me.doozyz.resonance.content.block.ResonanceBeaconBlock.updatePoweredState(level, worldPosition, powered);

            // Force sync to client
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(),
                    net.minecraft.world.level.block.Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void completeProcess() {
        // Visual effect every second
        if (level instanceof ServerLevel serverLevel) {
            // Upward particle burst
            serverLevel.sendParticles(
                    ParticleTypes.END_ROD,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 1.5,
                    worldPosition.getZ() + 0.5,
                    5,  // count
                    0.2, 0.5, 0.2,  // spread X, Y, Z
                    0.02  // speed
            );

            // Additional sparkle based on efficiency
            if (efficiency > 0.8f) {
                // High efficiency = more particles
                serverLevel.sendParticles(
                        ParticleTypes.ENCHANT,
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5,
                        10,
                        0.3, 0.3, 0.3,
                        0.05
                );
            }
        }
    }

    @Override
    protected int getBaseProgressRate() {
        return 1;  // One tick progress per tick = 20 tick cycle
    }

    @Override
    protected float getBaseConsumption() {
        return 5f;  // Consumes 5 amplitude per tick
    }

    @Override
    protected Direction getInputFace() {
        return Direction.DOWN;  // Input from below
    }

    @Override
    public Set<Direction> getConnectionDirections() {
        return EnumSet.of(Direction.DOWN);
    }

    @Override
    protected void spawnProcessingParticles() {
        if (level == null || !level.isClientSide()) return;

        // Continuous ambient sparkles when powered
        double x = worldPosition.getX() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.5;
        double y = worldPosition.getY() + 0.2;
        double z = worldPosition.getZ() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.5;

        // Color based on efficiency
        net.minecraft.core.particles.ParticleOptions particle =
                efficiency > 0.8f ? ParticleTypes.ELECTRIC_SPARK :
                        efficiency > 0.5f ? ParticleTypes.PORTAL :
                                ParticleTypes.SMOKE;

        level.addParticle(particle, x, y, z, 0, 0.05, 0);
    }
}
