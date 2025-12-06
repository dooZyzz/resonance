package me.doozyz.resonance.content.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.blockentity.CrystalNodeBlockEntity;
import me.doozyz.resonance.content.resonance.IResonanceNode;
import me.doozyz.resonance.content.resonance.ResonanceNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Crystal junction node - connects all 6 directions
 */
public class CrystalNodeBlock extends BaseEntityBlock {
    public static final MapCodec<CrystalNodeBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("tier").forGetter(b -> b.tier.name()),
                    propertiesCodec()
            ).apply(instance, (tierName, props) -> new CrystalNodeBlock(CrystalTier.valueOf(tierName), props))
    );

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    private final CrystalTier tier;

    private static final VoxelShape CORE_SHAPE = Block.box(5, 5, 5, 11, 11, 11);
    private static final VoxelShape NORTH_SHAPE = Block.box(5, 5, 0, 11, 11, 6);
    private static final VoxelShape SOUTH_SHAPE = Block.box(5, 5, 10, 11, 11, 16);
    private static final VoxelShape EAST_SHAPE = Block.box(10, 5, 5, 16, 11, 11);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 5, 5, 6, 11, 11);
    private static final VoxelShape UP_SHAPE = Block.box(5, 10, 5, 11, 16, 11);
    private static final VoxelShape DOWN_SHAPE = Block.box(5, 0, 5, 11, 6, 11);

    public CrystalNodeBlock(CrystalTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
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
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    protected @NotNull BlockState updateShape(
            @NotNull BlockState state,
            @NotNull net.minecraft.world.level.LevelReader level,
            @NotNull net.minecraft.world.level.ScheduledTickAccess scheduledTickAccess,
            @NotNull BlockPos pos,
            @NotNull Direction direction,
            @NotNull BlockPos neighborPos,
            @NotNull BlockState neighborState,
            @NotNull net.minecraft.util.RandomSource random) {
        if (level instanceof LevelAccessor accessor) {
            boolean connected = canConnectTo(accessor, neighborPos, direction.getOpposite());
            return state.setValue(getPropertyForDirection(direction), connected);
        }
        return state;
    }

    private boolean canConnectTo(LevelAccessor level, BlockPos pos, Direction fromDir) {
        if (level.getBlockEntity(pos) instanceof IResonanceNode node) {
            return node.getConnectionDirections().contains(fromDir);
        }
        return false;
    }

    private static BooleanProperty getPropertyForDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        VoxelShape shape = CORE_SHAPE;

        if (state.getValue(NORTH)) shape = Shapes.or(shape, NORTH_SHAPE);
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, SOUTH_SHAPE);
        if (state.getValue(EAST)) shape = Shapes.or(shape, EAST_SHAPE);
        if (state.getValue(WEST)) shape = Shapes.or(shape, WEST_SHAPE);
        if (state.getValue(UP)) shape = Shapes.or(shape, UP_SHAPE);
        if (state.getValue(DOWN)) shape = Shapes.or(shape, DOWN_SHAPE);

        return shape;
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
        return new CrystalNodeBlockEntity(pos, state, tier);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
}
