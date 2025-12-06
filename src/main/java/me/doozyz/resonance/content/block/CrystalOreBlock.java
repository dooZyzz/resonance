package me.doozyz.resonance.content.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;

public class CrystalOreBlock extends DropExperienceBlock {
    public CrystalOreBlock(Properties properties) {
        super(UniformInt.of(3, 7), properties);
    }
}
