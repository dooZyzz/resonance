package me.doozyz.resonance.content.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.blockentity.ResonanceBeaconBlockEntity;
import me.doozyz.resonance.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Resonance Beacon - simple test consumer that emits light and particles
 */
public class ResonanceBeaconBlock extends ResonanceConsumerBlock {

    public static final MapCodec<ResonanceBeaconBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec())
                    .apply(instance, ResonanceBeaconBlock::new)
    );

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ResonanceBeaconBlock(Properties properties) {
        super(CrystalTier.REFINED, properties);
        registerDefaultState(stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected @NotNull MapCodec<? extends ResonanceConsumerBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ResonanceBeaconBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return createTickerHelper(type, ModBlockEntities.RESONANCE_BEACON.get(),
                    (lvl, pos, st, beacon) -> {
                        beacon.clientTick();
                    });
        } else {
            return createTickerHelper(type, ModBlockEntities.RESONANCE_BEACON.get(),
                    (lvl, pos, st, beacon) -> {
                        beacon.serverTick();
                    });
        }
    }

    /**
     * Update powered state based on block entity
     */
    public static void updatePoweredState(Level level, BlockPos pos, boolean powered) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof ResonanceBeaconBlock && state.getValue(POWERED) != powered) {
            level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_ALL);
        }
    }
}
