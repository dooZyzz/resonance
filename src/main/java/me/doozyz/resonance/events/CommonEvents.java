package me.doozyz.resonance.events;

import me.doozyz.resonance.command.LeyLineCommand;
import me.doozyz.resonance.content.leyline.LeyLineHelper;
import me.doozyz.resonance.content.resonance.ResonanceNetworkManager;
import me.doozyz.resonance.registry.*;
import me.doozyz.resonance.support.ModConfig;
import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class CommonEvents {
    public CommonEvents(ModContainer container, IEventBus modEventBus, IEventBus forgeBus) {
        registerRegistries(modEventBus);
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);
        modEventBus.addListener(CommonEvents::commonSetup);
        forgeBus.addListener(CommonEvents::onChunkLoad);
        forgeBus.addListener(CommonEvents::onRegisterCommands);
        forgeBus.addListener(CommonEvents::onServerTick);
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
        ModPackets.register(modEventBus);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        ModRef.info("HELLO FROM COMMON SETUP");

        if (ModConfig.LOG_DIRT_BLOCK.getAsBoolean()) {
            ModRef.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        ModRef.info("{}{}", ModConfig.MAGIC_NUMBER_INTRODUCTION.get(), ModConfig.MAGIC_NUMBER.getAsInt());

        ModConfig.ITEM_STRINGS.get().forEach((item) -> ModRef.info("ITEM >> {}", item));
    }

    private static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && event.getChunk() instanceof LevelChunk chunk) {
            // Initialize ley line data if not already present
            if (!chunk.hasData(ModAttachments.LEY_LINE_DATA)) {
                LeyLineHelper.initializeChunkData(chunk, serverLevel.getSeed());
            }
        }
    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        LeyLineCommand.register(event.getDispatcher());
    }

    private static void onServerTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            // Tick the resonance network manager for this level
            ResonanceNetworkManager manager = ResonanceNetworkManager.get(serverLevel);

            // Debug logging every 5 seconds
            if (serverLevel.getGameTime() % 100 == 0 && !manager.toString().isEmpty()) {
                ModRef.info("Ticking network manager - Networks active");
            }

            manager.tick();
        }
    }
}
