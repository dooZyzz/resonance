package me.doozyz.resonance.registry;

import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, ModRef.MODID);

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}

