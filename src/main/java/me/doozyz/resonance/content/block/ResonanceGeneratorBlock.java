package me.doozyz.resonance.content.block;

import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.blockentity.ResonanceGeneratorBlockEntity;
import me.doozyz.resonance.content.resonance.IResonanceNode;
import me.doozyz.resonance.content.resonance.ResonanceNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for all resonance generator blocks
 */
public abstract class ResonanceGeneratorBlock extends BaseEntityBlock {

    protected final CrystalTier tier;

    public ResonanceGeneratorBlock(CrystalTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CrystalTier getTier() {
        return tier;
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof IResonanceNode node) {
            ResonanceNetworkManager.get(level).onNodePlaced(pos, node);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull ItemStack toolStack, boolean willHarvest, @NotNull FluidState fluid) {
        if (!level.isClientSide()) {
            ResonanceNetworkManager.get(level).onNodeRemoved(pos);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    /**
     * Create ticker for generator block entities
     */
    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createGeneratorTicker(
            Level level, BlockEntityType<T> actualType, BlockEntityType<? extends ResonanceGeneratorBlockEntity> expectedType) {

        if (actualType != expectedType) return null;

        return (lvl, pos, state, blockEntity) -> {
            if (blockEntity instanceof ResonanceGeneratorBlockEntity generator) {
                if (lvl.isClientSide()) {
                    generator.clientTick();
                } else {
                    generator.serverTick();
                }
            }
        };
    }

    /**
     * Override to provide ticker for your specific generator type
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return null; // Subclasses override to provide specific ticker
    }
}
