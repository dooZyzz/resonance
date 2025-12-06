package me.doozyz.resonance.content;

import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModTags {
    public static class Biomes {
        public static final TagKey<Biome> HIGH_MAGIC = create("high_magic");

        private static TagKey<Biome> create(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(ModRef.MODID, name));
        }
    }
}
