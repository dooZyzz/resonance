package me.doozyz.resonance.content.item;

import me.doozyz.resonance.content.leyline.LeyLineData;
import me.doozyz.resonance.content.leyline.LeyLineHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ResonanceMeterItem extends Item {

    public ResonanceMeterItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide() && context.getPlayer() != null) {
            var player = context.getPlayer();
            var pos = context.getClickedPos();

            // Check for ley lines at this position
            LeyLineData leyData = LeyLineHelper.getLeyLineData(level, pos);

            if (leyData.hasLeyLine()) {
                player.displayClientMessage(
                        Component.literal("§d=== Ley Line Detected ==="),
                        false
                );

                leyData.getStrongestLine().ifPresent(line -> {
                    player.displayClientMessage(
                            Component.literal("§5Type: §f" + line.type().name()),
                            false
                    );
                    player.displayClientMessage(
                            Component.literal("§5Frequency: §f" + line.type().getFrequency() + " Hz"),
                            false
                    );
                    player.displayClientMessage(
                            Component.literal("§5Amplitude: §f" + line.type().getAmplitude() + " A"),
                            false
                    );
                    player.displayClientMessage(
                            Component.literal("§5Strength: §f" + line.strength() + " A"),
                            false
                    );
                });

                if (leyData.isNexus()) {
                    player.displayClientMessage(
                            Component.literal("§d§l★ NEXUS POINT DETECTED ★"),
                            false
                    );
                    player.displayClientMessage(
                            Component.literal("§5Lines: §f" + leyData.getLeyLines().size()),
                            false
                    );
                    player.displayClientMessage(
                            Component.literal("§5Total Strength: §f" + leyData.getTotalStrength() + " A"),
                            false
                    );
                }

                return InteractionResult.SUCCESS;
            } else {
                // Check if block is a resonance node (placeholder for Phase 2)
                player.displayClientMessage(
                        Component.literal("No resonance detected")
                                .withStyle(ChatFormatting.GRAY)
                                .append(Component.literal(" (Resonance nodes not yet implemented)")
                                        .withStyle(ChatFormatting.DARK_GRAY)),
                        false
                );
            }
        }

        return InteractionResult.PASS;
    }
}
