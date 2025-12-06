package me.doozyz.resonance.content.block;

import me.doozyz.resonance.content.CrystalTier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.NotNull;

public class CrystalBlock extends Block {
    private final CrystalTier tier;

    public CrystalBlock(CrystalTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CrystalTier getTier() {
        return tier;
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return switch (tier) {
            case RAW -> 3;
            case REFINED -> 7;
            case SYNTHETIC -> 11;
            case LEY_INFUSED -> 15;
        };
    }
}
