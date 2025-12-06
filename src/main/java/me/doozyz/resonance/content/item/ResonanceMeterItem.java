package me.doozyz.resonance.content.item;

import me.doozyz.resonance.content.blockentity.ResonanceBeaconBlockEntity;
import me.doozyz.resonance.content.blockentity.AmbientCollectorBlockEntity;
import me.doozyz.resonance.content.leyline.LeyLineData;
import me.doozyz.resonance.content.leyline.LeyLineHelper;
import me.doozyz.resonance.content.resonance.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
        BlockPos pos = context.getClickedPos();

        if (!level.isClientSide() && context.getPlayer() != null) {
            var player = context.getPlayer();
            var blockEntity = level.getBlockEntity(pos);

            // Check if it's a resonance node
            if (blockEntity instanceof IResonanceNode node) {
                player.displayClientMessage(Component.literal("§6=== Resonance Reading ==="), false);

                // Basic info
                ResonanceState state = node.getResonanceState();
                player.displayClientMessage(
                        Component.literal("§eFrequency: §f" + String.format("%.1f", state.frequency()) + " Hz"),
                        false
                );
                player.displayClientMessage(
                        Component.literal("§eAmplitude: §f" + String.format("%.1f", state.amplitude()) + " A"),
                        false
                );
                player.displayClientMessage(
                        Component.literal("§ePhase: §f" + String.format("%.1f", Math.toDegrees(state.phase())) + "°"),
                        false
                );

                // Tier info
                player.displayClientMessage(
                        Component.literal("§eTier: §f" + node.getTier().name() +
                                " §7(Max: " + node.getTier().getMaxFrequency() + " Hz, " +
                                node.getTier().getMaxAmplitude() + " A)"),
                        false
                );

                // Capacity usage
                if (state.amplitude() > 0) {
                    float usage = (state.amplitude() / node.getTier().getMaxAmplitude()) * 100f;
                    String color = usage > 90 ? "§c" : usage > 70 ? "§e" : "§a";
                    player.displayClientMessage(
                            Component.literal("§eCapacity: " + color + String.format("%.1f%%", usage)),
                            false
                    );
                }

                // Node type
                if (node.isSource()) {
                    player.displayClientMessage(Component.literal("§aType: GENERATOR"), false);

                    if (blockEntity instanceof IResonanceGenerator generator) {
                        player.displayClientMessage(
                                Component.literal("§aCan Generate: §f" + generator.canGenerate()),
                                false
                        );

                        if (blockEntity instanceof AmbientCollectorBlockEntity collector) {
                            player.displayClientMessage(
                                    Component.literal("§aEnergy: §f" + String.format("%.1f", collector.getCollectedEnergy()) +
                                            " / 100 §7(" + String.format("%.1f%%", collector.getStoragePercent() * 100) + ")"),
                                    false
                            );
                        }
                    }
                } else if (node.isSink()) {
                    player.displayClientMessage(Component.literal("§bType: CONSUMER"), false);

                    if (blockEntity instanceof IResonanceConsumer consumer) {
                        ResonanceRequirement req = consumer.getRequirement();
                        player.displayClientMessage(
                                Component.literal("§bPowered: " + (consumer.isPowered() ? "§a✓" : "§c✗")),
                                false
                        );

                        if (blockEntity instanceof ResonanceBeaconBlockEntity beacon) {
                            player.displayClientMessage(
                                    Component.literal("§bEfficiency: §f" + String.format("%.1f%%", beacon.getEfficiency() * 100)),
                                    false
                            );
                            player.displayClientMessage(
                                    Component.literal("§bConsumption: §f" + consumer.getAmplitudeConsumption() + " A/tick"),
                                    false
                            );
                            player.displayClientMessage(
                                    Component.literal("§bProgress: §f" + beacon.getProgress() + " / " + beacon.getMaxProgress()),
                                    false
                            );
                        }

                        player.displayClientMessage(
                                Component.literal("§bRequirement: §f" + String.format("%.0f-%.0f Hz, %.0f+ A",
                                        req.minFrequency(), req.maxFrequency(), req.minAmplitude())),
                                false
                        );
                    }
                } else {
                    player.displayClientMessage(Component.literal("§3Type: CONDUIT"), false);
                }

                // Connection info
                player.displayClientMessage(
                        Component.literal("§7Connections: " + node.getConnectionDirections().size() + " faces"),
                        false
                );

                // Network info
                ResonanceNetworkManager manager = ResonanceNetworkManager.get(level);
                ResonanceNetwork network = manager.getNetworkAt(pos);
                if (network != null) {
                    player.displayClientMessage(
                            Component.literal("§7Network: " + network.getId().toString().substring(0, 8) +
                                    "... §7(" + network.size() + " nodes)"),
                            false
                    );
                } else {
                    player.displayClientMessage(Component.literal("§7Network: §cNone"), false);
                }

                return InteractionResult.SUCCESS;
            }

            // Check for ley lines if not a resonance node
            LeyLineData leyData = LeyLineHelper.getLeyLineData(level, pos);

            if (leyData.hasLeyLine()) {
                player.displayClientMessage(Component.literal("§d=== Ley Line Detected ==="), false);

                leyData.getStrongestLine().ifPresent(line -> {
                    player.displayClientMessage(Component.literal("§5Type: §f" + line.type().name()), false);
                    player.displayClientMessage(Component.literal("§5Frequency: §f" + line.type().getFrequency() + " Hz"), false);
                    player.displayClientMessage(Component.literal("§5Amplitude: §f" + line.type().getAmplitude() + " A"), false);
                    player.displayClientMessage(Component.literal("§5Strength: §f" + line.strength() + " A"), false);
                });

                if (leyData.isNexus()) {
                    player.displayClientMessage(Component.literal("§d§l★ NEXUS POINT DETECTED ★"), false);
                    player.displayClientMessage(Component.literal("§5Lines: §f" + leyData.getLeyLines().size()), false);
                    player.displayClientMessage(Component.literal("§5Total Strength: §f" + leyData.getTotalStrength() + " A"), false);
                }

                return InteractionResult.SUCCESS;
            }

            // Nothing detected
            player.displayClientMessage(
                    Component.literal("No resonance or ley lines detected")
                            .withStyle(ChatFormatting.GRAY),
                    false
            );
        }

        return InteractionResult.PASS;
    }
}
