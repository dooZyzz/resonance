package me.doozyz.resonance.events;

import me.doozyz.resonance.registry.BlockRegistry;
import me.doozyz.resonance.registry.ItemRegistry;
import me.doozyz.resonance.registry.TabsRegistry;
import me.doozyz.resonance.support.ModConfig;
import me.doozyz.resonance.support.ModRef;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class CommonEvents {
    public CommonEvents(ModContainer container, IEventBus modEventBus, IEventBus forgeBus) {
        registerRegistries(modEventBus);
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);
        modEventBus.addListener(CommonEvents::commonSetup);
    }

    private static void registerRegistries(IEventBus modEventBus) {
        BlockRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        TabsRegistry.register(modEventBus);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        ModRef.info("HELLO FROM COMMON SETUP");

        if (ModConfig.LOG_DIRT_BLOCK.getAsBoolean()) {
            ModRef.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        ModRef.info("{}{}", ModConfig.MAGIC_NUMBER_INTRODUCTION.get(), ModConfig.MAGIC_NUMBER.getAsInt());

        ModConfig.ITEM_STRINGS.get().forEach((item) -> ModRef.info("ITEM >> {}", item));
    }
}
