package me.doozyz.resonance.registry;

import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.block.CrystalBlock;
import me.doozyz.resonance.content.block.CrystalOreBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static me.doozyz.resonance.support.ModRef.MODID;

public class ModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    // Crystal Ores
    public static final DeferredBlock<CrystalOreBlock> RAW_CRYSTAL_ORE = BLOCKS.registerBlock("raw_crystal_ore",
            CrystalOreBlock::new, () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0f, 3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    public static final DeferredBlock<CrystalOreBlock> DEEPSLATE_CRYSTAL_ORE = BLOCKS.registerBlock("deepslate_crystal_ore",
            CrystalOreBlock::new, () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5f, 3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE));

    // Crystal Storage Blocks
    public static final DeferredBlock<CrystalBlock> RAW_CRYSTAL_BLOCK = BLOCKS.registerBlock("raw_crystal_block",
            properties -> new CrystalBlock(CrystalTier.RAW, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(3.0f, 3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST));

    public static final DeferredBlock<CrystalBlock> REFINED_CRYSTAL_BLOCK = BLOCKS.registerBlock("refined_crystal_block",
            properties -> new CrystalBlock(CrystalTier.REFINED, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(4.0f, 4.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST));

    public static final DeferredBlock<CrystalBlock> SYNTHETIC_CRYSTAL_BLOCK = BLOCKS.registerBlock("synthetic_crystal_block",
            properties -> new CrystalBlock(CrystalTier.SYNTHETIC, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(5.0f, 5.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST));

    public static final DeferredBlock<CrystalBlock> LEY_INFUSED_CRYSTAL_BLOCK = BLOCKS.registerBlock("ley_infused_crystal_block",
            properties -> new CrystalBlock(CrystalTier.LEY_INFUSED, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_MAGENTA)
                    .strength(6.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST));

    // Crystal Cluster
    public static final DeferredBlock<AmethystClusterBlock> CRYSTAL_CLUSTER = BLOCKS.registerBlock("crystal_cluster",
            properties -> new AmethystClusterBlock(7, 3, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noOcclusion()
                    .randomTicks()
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .strength(1.5f)
                    .lightLevel(state -> 5));

    // Geode Shell
    public static final DeferredBlock<Block> GEODE_SHELL = BLOCKS.registerBlock("geode_shell",
            Block::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(1.5f, 1.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
