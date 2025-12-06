package me.doozyz.resonance.content.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.blockentity.CrystalPrismBlockEntity;
import me.doozyz.resonance.content.resonance.IResonanceNode;
import me.doozyz.resonance.content.resonance.ResonanceNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Straight crystal conduit - connects 2 opposite faces
 */
public class CrystalPrismBlock extends BaseEntityBlock {
    public static final MapCodec<CrystalPrismBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("tier").forGetter(b -> b.tier.name()),
                    propertiesCodec()
            ).apply(instance, (tierName, props) -> new CrystalPrismBlock(CrystalTier.valueOf(tierName), props))
    );

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private final CrystalTier tier;

    private static final VoxelShape SHAPE_X = Block.box(0, 6, 6, 16, 10, 10);
    private static final VoxelShape SHAPE_Y = Block.box(6, 0, 6, 10, 16, 10);
    private static final VoxelShape SHAPE_Z = Block.box(6, 6, 0, 10, 10, 16);

    public CrystalPrismBlock(CrystalTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public CrystalTier getTier() {
        return tier;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getNearestLookingDirection().getOpposite();
        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING).getAxis()) {
            case X -> SHAPE_X;
            case Y -> SHAPE_Y;
            case Z -> SHAPE_Z;
        };
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof IResonanceNode node) {
            ResonanceNetworkManager.get(level).onNodePlaced(pos, node);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull net.minecraft.world.entity.player.Player player, @NotNull net.minecraft.world.item.ItemStack toolStack, boolean willHarvest, @NotNull net.minecraft.world.level.material.FluidState fluid) {
        if (!level.isClientSide()) {
            ResonanceNetworkManager.get(level).onNodeRemoved(pos);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CrystalPrismBlockEntity(pos, state, tier);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
}
