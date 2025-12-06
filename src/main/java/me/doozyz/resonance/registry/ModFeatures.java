package me.doozyz.resonance.registry;

import me.doozyz.resonance.content.worldgen.CrystalGeodeFeature;
import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    private static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, ModRef.MODID);

    public static final DeferredHolder<Feature<?>, CrystalGeodeFeature> CRYSTAL_GEODE = REGISTRY.register(
            "crystal_geode",
            () -> new CrystalGeodeFeature(NoneFeatureConfiguration.CODEC)
    );

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
