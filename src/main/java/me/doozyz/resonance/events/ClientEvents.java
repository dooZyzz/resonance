package me.doozyz.resonance.events;

import me.doozyz.resonance.support.ModRef;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClientEvents {
    public ClientEvents(ModContainer container, IEventBus modEventBus, IEventBus forgeBus) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        // Register the clientSetup method for modloading
        modEventBus.addListener(ClientEvents::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        ModRef.info("HELLO FROM CLIENT SETUP");
        ModRef.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
