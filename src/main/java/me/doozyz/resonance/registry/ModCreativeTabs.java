package me.doozyz.resonance.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static me.doozyz.resonance.support.ModRef.MODID;

public class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESONANCE_TAB = CREATIVE_MODE_TABS.register("resonance_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.resonance"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.RAW_CRYSTAL_SHARD.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // Crystal Items
                output.accept(ModItems.RAW_CRYSTAL_SHARD.get());
                output.accept(ModItems.REFINED_CRYSTAL.get());
                output.accept(ModItems.SYNTHETIC_CRYSTAL.get());
                output.accept(ModItems.LEY_INFUSED_CRYSTAL.get());

                // Ores
                output.accept(ModItems.RAW_CRYSTAL_ORE.get());
                output.accept(ModItems.DEEPSLATE_CRYSTAL_ORE.get());

                // Storage Blocks
                output.accept(ModItems.RAW_CRYSTAL_BLOCK.get());
                output.accept(ModItems.REFINED_CRYSTAL_BLOCK.get());
                output.accept(ModItems.SYNTHETIC_CRYSTAL_BLOCK.get());
                output.accept(ModItems.LEY_INFUSED_CRYSTAL_BLOCK.get());

                // Crystal Cluster
                output.accept(ModItems.CRYSTAL_CLUSTER.get());

                // Geode Shell
                output.accept(ModItems.GEODE_SHELL.get());

                // Crystal Prisms
                output.accept(ModItems.RAW_PRISM.get());
                output.accept(ModItems.REFINED_PRISM.get());
                output.accept(ModItems.SYNTHETIC_PRISM.get());
                output.accept(ModItems.LEY_INFUSED_PRISM.get());

                // Crystal Nodes
                output.accept(ModItems.RAW_NODE.get());
                output.accept(ModItems.REFINED_NODE.get());
                output.accept(ModItems.SYNTHETIC_NODE.get());
                output.accept(ModItems.LEY_INFUSED_NODE.get());

                // Generators
                output.accept(ModItems.AMBIENT_COLLECTOR.get());

                // Tools
                output.accept(ModItems.TUNING_FORK.get());
                output.accept(ModItems.RESONANCE_METER.get());
                output.accept(ModItems.DIVINING_ROD.get());
            }).build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
