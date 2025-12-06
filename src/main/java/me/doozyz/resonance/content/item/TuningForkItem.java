package me.doozyz.resonance.content.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TuningForkItem extends Item {

    public TuningForkItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide() && context.getPlayer() != null) {
            // Placeholder for future resonance network functionality
            if (context.getPlayer().isShiftKeyDown()) {
                // Debug mode - will show network info in Phase 2
                context.getPlayer().displayClientMessage(
                        Component.literal("Network Debug Mode")
                                .withStyle(ChatFormatting.YELLOW)
                                .append(Component.literal(" (Not yet implemented)")
                                        .withStyle(ChatFormatting.GRAY)),
                        false
                );
            } else {
                // Ping mode - will trigger network pulse in Phase 2
                context.getPlayer().displayClientMessage(
                        Component.literal("Network Ping")
                                .withStyle(ChatFormatting.AQUA)
                                .append(Component.literal(" (Resonance networks not yet implemented)")
                                        .withStyle(ChatFormatting.GRAY)),
                        false
                );
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
