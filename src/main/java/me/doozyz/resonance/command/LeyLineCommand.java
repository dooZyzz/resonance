package me.doozyz.resonance.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.doozyz.resonance.content.leyline.LeyLineData;
import me.doozyz.resonance.content.leyline.LeyLineHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class LeyLineCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("leyline")
                .requires(source -> source.hasPermission(2))
                .executes(LeyLineCommand::showLeyLineInfo));
    }

    private static int showLeyLineInfo(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = BlockPos.containing(source.getPosition());

        ServerLevel serverLevel = source.getLevel();

        LeyLineData data = LeyLineHelper.getLeyLineData(serverLevel.getLevel(), pos);

        if (!data.hasLeyLine()) {
            source.sendSuccess(() -> Component.literal("No ley lines detected at this location")
                    .withStyle(ChatFormatting.RED), false);
            return 0;
        }

        source.sendSuccess(() -> Component.literal("=== Ley Line Detection ===")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), false);

        source.sendSuccess(() -> Component.literal("Position: " + pos.toShortString())
                .withStyle(ChatFormatting.GRAY), false);

        source.sendSuccess(() -> Component.literal("Lines detected: " + data.getLeyLines().size())
                .withStyle(ChatFormatting.YELLOW), false);

        if (data.isNexus()) {
            source.sendSuccess(() -> Component.literal("★ NEXUS POINT ★")
                    .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD), false);
        }

        for (LeyLineData.LeyLine line : data.getLeyLines()) {
            source.sendSuccess(() -> Component.literal("")
                    .append(Component.literal("  • Type: ").withStyle(ChatFormatting.AQUA))
                    .append(Component.literal(line.type().name()).withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(" | Strength: ").withStyle(ChatFormatting.AQUA))
                    .append(Component.literal(String.valueOf(line.strength())).withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(" | Angle: ").withStyle(ChatFormatting.AQUA))
                    .append(Component.literal(String.format("%.1f°", Math.toDegrees(line.angle()))).withStyle(ChatFormatting.WHITE)),
                    false);
        }

        source.sendSuccess(() -> Component.literal("Total Strength: " + data.getTotalStrength() + " A")
                .withStyle(ChatFormatting.GREEN), false);

        return 1;
    }
}
