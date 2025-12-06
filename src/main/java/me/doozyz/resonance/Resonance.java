package me.doozyz.resonance;

import me.doozyz.resonance.events.ClientEvents;
import me.doozyz.resonance.events.CommonEvents;
import me.doozyz.resonance.support.ModConfig;
import me.doozyz.resonance.support.ModRef;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ModRef.MODID)
public class Resonance {
    public Resonance(IEventBus modEventBus, ModContainer modContainer) {
        new ClientEvents(modContainer, modEventBus, NeoForge.EVENT_BUS);
        new CommonEvents(modContainer, modEventBus, NeoForge.EVENT_BUS);
    }
}
