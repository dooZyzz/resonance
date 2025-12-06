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

    // Crystal Prisms (Straight Conduits)
    public static final DeferredBlock<me.doozyz.resonance.content.block.CrystalPrismBlock> RAW_PRISM = BLOCKS.registerBlock("raw_prism",
            properties -> new me.doozyz.resonance.content.block.CrystalPrismBlock(CrystalTier.RAW, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(2.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .lightLevel(state -> 3));

    public static final DeferredBlock<me.doozyz.resonance.content.block.CrystalPrismBlock> REFINED_PRISM = BLOCKS.registerBlock("refined_prism",
            properties -> new me.doozyz.resonance.content.block.CrystalPrismBlock(CrystalTier.REFINED, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(2.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .lightLevel(state -> 7));

    public static final DeferredBlock<me.doozyz.resonance.content.block.CrystalPrismBlock> SYNTHETIC_PRISM = BLOCKS.registerBlock("synthetic_prism",
            properties -> new me.doozyz.resonance.content.block.CrystalPrismBlock(CrystalTier.SYNTHETIC, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .lightLevel(state -> 11));

    public static final DeferredBlock<me.doozyz.resonance.content.block.CrystalPrismBlock> LEY_INFUSED_PRISM = BLOCKS.registerBlock("ley_infused_prism",
            properties -> new me.doozyz.resonance.content.block.CrystalPrismBlock(CrystalTier.LEY_INFUSED, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_MAGENTA)
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .lightLevel(state -> 15));

    // Crystal Nodes (Junctions)
    public static final DeferredBlock<me.doozyz.resonance.content.block.CrystalNodeBlock> RAW_NODE = BLOCKS.registerBlock("raw_node",
            properties -> new me.doozyz.resonance.content.block.CrystalNodeBlock(CrystalTier.RAW, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(2.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .lightLevel(state -> 3));

    public static final DeferredBlock<me.doozyz.resonance.content.block.CrystalNodeBlock> REFINED_NODE = BLOCKS.registerBlock("refined_node",
            properties -> new me.doozyz.resonance.content.block.CrystalNodeBlock(CrystalTier.REFINED, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(2.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .lightLevel(state -> 7));

    public static final DeferredBlock<me.doozyz.resonance.content.block.CrystalNodeBlock> SYNTHETIC_NODE = BLOCKS.registerBlock("synthetic_node",
            properties -> new me.doozyz.resonance.content.block.CrystalNodeBlock(CrystalTier.SYNTHETIC, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .lightLevel(state -> 11));

    public static final DeferredBlock<me.doozyz.resonance.content.block.CrystalNodeBlock> LEY_INFUSED_NODE = BLOCKS.registerBlock("ley_infused_node",
            properties -> new me.doozyz.resonance.content.block.CrystalNodeBlock(CrystalTier.LEY_INFUSED, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_MAGENTA)
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .lightLevel(state -> 15));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
