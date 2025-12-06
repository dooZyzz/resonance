package me.doozyz.resonance.registry;

import me.doozyz.resonance.content.item.DiviningRodItem;
import me.doozyz.resonance.content.item.ResonanceMeterItem;
import me.doozyz.resonance.content.item.TuningForkItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static me.doozyz.resonance.support.ModRef.MODID;

public class ModItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Crystal Items
    public static final DeferredItem<Item> RAW_CRYSTAL_SHARD = ITEMS.registerItem("raw_crystal_shard", Item::new, Item.Properties::new);
    public static final DeferredItem<Item> REFINED_CRYSTAL = ITEMS.registerItem("refined_crystal", Item::new, Item.Properties::new);
    public static final DeferredItem<Item> SYNTHETIC_CRYSTAL = ITEMS.registerItem("synthetic_crystal", Item::new, Item.Properties::new);
    public static final DeferredItem<Item> LEY_INFUSED_CRYSTAL = ITEMS.registerItem("ley_infused_crystal", Item::new, Item.Properties::new);

    // Tools
    public static final DeferredItem<Item> TUNING_FORK = ITEMS.registerItem("tuning_fork", TuningForkItem::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> RESONANCE_METER = ITEMS.registerItem("resonance_meter", ResonanceMeterItem::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> DIVINING_ROD = ITEMS.registerItem("divining_rod", DiviningRodItem::new, () -> new Item.Properties().stacksTo(1));

    // Block Items - Ores
    public static final DeferredItem<BlockItem> RAW_CRYSTAL_ORE = ITEMS.registerSimpleBlockItem("raw_crystal_ore", ModBlocks.RAW_CRYSTAL_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_CRYSTAL_ORE = ITEMS.registerSimpleBlockItem("deepslate_crystal_ore", ModBlocks.DEEPSLATE_CRYSTAL_ORE);

    // Block Items - Storage Blocks
    public static final DeferredItem<BlockItem> RAW_CRYSTAL_BLOCK = ITEMS.registerSimpleBlockItem("raw_crystal_block", ModBlocks.RAW_CRYSTAL_BLOCK);
    public static final DeferredItem<BlockItem> REFINED_CRYSTAL_BLOCK = ITEMS.registerSimpleBlockItem("refined_crystal_block", ModBlocks.REFINED_CRYSTAL_BLOCK);
    public static final DeferredItem<BlockItem> SYNTHETIC_CRYSTAL_BLOCK = ITEMS.registerSimpleBlockItem("synthetic_crystal_block", ModBlocks.SYNTHETIC_CRYSTAL_BLOCK);
    public static final DeferredItem<BlockItem> LEY_INFUSED_CRYSTAL_BLOCK = ITEMS.registerSimpleBlockItem("ley_infused_crystal_block", ModBlocks.LEY_INFUSED_CRYSTAL_BLOCK);

    // Block Items - Crystal Cluster
    public static final DeferredItem<BlockItem> CRYSTAL_CLUSTER = ITEMS.registerSimpleBlockItem("crystal_cluster", ModBlocks.CRYSTAL_CLUSTER);

    // Block Items - Geode Shell
    public static final DeferredItem<BlockItem> GEODE_SHELL = ITEMS.registerSimpleBlockItem("geode_shell", ModBlocks.GEODE_SHELL);

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
