package me.doozyz.resonance.registry;

import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    private static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ModRef.MODID);

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
