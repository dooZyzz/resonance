package me.doozyz.resonance.content.item;

import me.doozyz.resonance.content.leyline.LeyLineData;
import me.doozyz.resonance.content.leyline.LeyLineHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DiviningRodItem extends Item {

    public DiviningRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide()) {
            LeyLineData data = LeyLineHelper.getLeyLineData(level, player.blockPosition());

            if (!data.hasLeyLine()) {
                player.displayClientMessage(
                        Component.literal("No ley lines detected nearby")
                                .withStyle(ChatFormatting.GRAY),
                        true
                );
            } else {
                // Calculate intensity for display
                int lineCount = data.getLeyLines().size();
                int totalStrength = data.getTotalStrength();
                int intensity = Math.min(10, totalStrength / 50);

                String bar = ChatFormatting.LIGHT_PURPLE + "▮".repeat(intensity) +
                        ChatFormatting.DARK_GRAY + "▯".repeat(10 - intensity);

                Component message = Component.literal("Ley Energy: ")
                        .withStyle(ChatFormatting.AQUA)
                        .append(Component.literal(bar));

                if (data.isNexus()) {
                    message = Component.literal("★ NEXUS ★ ")
                            .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                            .append(message);
                }

                player.displayClientMessage(message, true);

                // Send detailed info to chat
                player.displayClientMessage(Component.literal("=== Ley Line Detected ===")
                        .withStyle(ChatFormatting.LIGHT_PURPLE), false);

                data.getStrongestLine().ifPresent(line -> {
                    player.displayClientMessage(Component.literal("Type: ")
                            .withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(line.type().name())
                                    .withStyle(ChatFormatting.WHITE)), false);

                    player.displayClientMessage(Component.literal("Strength: ")
                            .withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(line.strength() + " A")
                                    .withStyle(ChatFormatting.YELLOW)), false);
                });

                if (lineCount > 1) {
                    player.displayClientMessage(Component.literal("Lines: " + lineCount + " (Nexus Point)")
                            .withStyle(ChatFormatting.GOLD), false);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }
}
