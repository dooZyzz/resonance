package me.doozyz.resonance.content.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.blockentity.AmbientCollectorBlockEntity;
import me.doozyz.resonance.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Block for the Ambient Resonance Collector
 */
public class AmbientCollectorBlock extends ResonanceGeneratorBlock {

    public static final MapCodec<AmbientCollectorBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec())
                    .apply(instance, AmbientCollectorBlock::new)
    );

    public AmbientCollectorBlock(Properties properties) {
        super(CrystalTier.RAW, properties);
    }

    @Override
    protected @NotNull MapCodec<? extends ResonanceGeneratorBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AmbientCollectorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.AMBIENT_COLLECTOR.get(),
                (lvl, pos, st, blockEntity) -> {
                    if (blockEntity instanceof me.doozyz.resonance.content.blockentity.AmbientCollectorBlockEntity collector) {
                        collector.serverTick();
                    }
                });
    }
}
