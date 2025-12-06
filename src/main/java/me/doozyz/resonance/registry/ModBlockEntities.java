package me.doozyz.resonance.registry;

import me.doozyz.resonance.content.blockentity.CrystalNodeBlockEntity;
import me.doozyz.resonance.content.blockentity.CrystalPrismBlockEntity;
import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModRef.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrystalPrismBlockEntity>> CRYSTAL_PRISM = REGISTRY.register(
            "crystal_prism",
            () -> new BlockEntityType<>(
                    (pos, state) -> {
                        if (state.getBlock() instanceof me.doozyz.resonance.content.block.CrystalPrismBlock prism) {
                            return new CrystalPrismBlockEntity(pos, state, prism.getTier());
                        }
                        return null;
                    },
                    java.util.Set.of(
                            ModBlocks.RAW_PRISM.get(),
                            ModBlocks.REFINED_PRISM.get(),
                            ModBlocks.SYNTHETIC_PRISM.get(),
                            ModBlocks.LEY_INFUSED_PRISM.get()
                    )
            )
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrystalNodeBlockEntity>> CRYSTAL_NODE = REGISTRY.register(
            "crystal_node",
            () -> new BlockEntityType<>(
                    (pos, state) -> {
                        if (state.getBlock() instanceof me.doozyz.resonance.content.block.CrystalNodeBlock node) {
                            return new CrystalNodeBlockEntity(pos, state, node.getTier());
                        }
                        return null;
                    },
                    java.util.Set.of(
                            ModBlocks.RAW_NODE.get(),
                            ModBlocks.REFINED_NODE.get(),
                            ModBlocks.SYNTHETIC_NODE.get(),
                            ModBlocks.LEY_INFUSED_NODE.get()
                    )
            )
    );

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
