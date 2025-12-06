package me.doozyz.resonance.registry;

import me.doozyz.resonance.support.ModRef;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ModRef.MODID);

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
