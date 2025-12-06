package me.doozyz.resonance.registry;

import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    private static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, ModRef.MODID);

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
