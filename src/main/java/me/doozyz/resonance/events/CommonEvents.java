package me.doozyz.resonance.events;

import me.doozyz.resonance.registry.*;
import me.doozyz.resonance.support.ModConfig;
import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
    public CommonEvents(ModContainer container, IEventBus modEventBus, IEventBus forgeBus) {
        registerRegistries(modEventBus);
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);
        modEventBus.addListener(CommonEvents::commonSetup);
    }

    private static void registerRegistries(IEventBus modEventBus) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
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
