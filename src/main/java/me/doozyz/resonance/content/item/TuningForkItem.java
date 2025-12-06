package me.doozyz.resonance.content.item;

import me.doozyz.resonance.content.resonance.IResonanceNode;
import me.doozyz.resonance.content.resonance.ResonanceNetwork;
import me.doozyz.resonance.content.resonance.ResonanceNetworkManager;
import me.doozyz.resonance.network.NetworkPingPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TuningForkItem extends Item {

    public TuningForkItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!level.isClientSide() && context.getPlayer() != null) {
            // Check if clicking on a resonance node
            if (level.getBlockEntity(pos) instanceof IResonanceNode) {
                ResonanceNetworkManager manager = ResonanceNetworkManager.get(level);
                ResonanceNetwork network = manager.getNetworkAt(pos);

                if (context.getPlayer().isShiftKeyDown()) {
                    // Debug mode - show network info
                    if (network != null) {
                        context.getPlayer().displayClientMessage(
                                Component.literal("§6=== Network Debug ==="),
                                false
                        );
                        context.getPlayer().displayClientMessage(
                                Component.literal("§eNodes: §f" + network.size()),
                                false
                        );
                        context.getPlayer().displayClientMessage(
                                Component.literal("§eNetwork ID: §f" + network.getId().toString().substring(0, 8) + "..."),
                                false
                        );
                    } else {
                        context.getPlayer().displayClientMessage(
                                Component.literal("§cNo network found"),
                                false
                        );
                    }
                } else {
                    // Ping mode - send visual pulse
                    if (network != null && context.getPlayer() instanceof ServerPlayer serverPlayer) {
                        // Collect network node positions (limit to 50 for packet size)
                        List<BlockPos> nodes = new ArrayList<>(network.getAllPositions());
                        if (nodes.size() > 50) {
                            nodes = nodes.subList(0, 50);
                        }

                        // Send ping packet to nearby players
                        NetworkPingPacket packet = new NetworkPingPacket(pos, nodes);
                        PacketDistributor.sendToPlayersTrackingChunk((net.minecraft.server.level.ServerLevel) level, level.getChunkAt(pos).getPos(), packet);

                        context.getPlayer().displayClientMessage(
                                Component.literal("§aNetwork Ping Sent §7(")
                                        .append(Component.literal(network.size() + " nodes").withStyle(ChatFormatting.WHITE))
                                        .append(Component.literal(")")),
                                true
                        );
                    } else {
                        context.getPlayer().displayClientMessage(
                                Component.literal("§cNo network found"),
                                true
                        );
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}

