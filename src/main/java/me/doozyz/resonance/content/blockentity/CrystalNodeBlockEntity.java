package me.doozyz.resonance.content.blockentity;

import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.Set;

public class CrystalNodeBlockEntity extends CrystalBlockEntity {

    public CrystalNodeBlockEntity(BlockPos pos, BlockState state, CrystalTier tier) {
        super(ModBlockEntities.CRYSTAL_NODE.get(), pos, state, tier);
    }

    @Override
    public Set<Direction> getConnectionDirections() {
        return EnumSet.allOf(Direction.class);
    }
}
