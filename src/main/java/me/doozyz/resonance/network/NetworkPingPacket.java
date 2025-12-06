package me.doozyz.resonance.network;

import io.netty.buffer.ByteBuf;
import me.doozyz.resonance.support.ModRef;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.UUID;

/**
 * Packet to visualize network ping on client (Tuning Fork feature)
 */
public record NetworkPingPacket(
        BlockPos origin,
        List<BlockPos> networkNodes
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NetworkPingPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModRef.MODID, "network_ping"));

    public static final StreamCodec<ByteBuf, NetworkPingPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, NetworkPingPacket::origin,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), NetworkPingPacket::networkNodes,
            NetworkPingPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(NetworkPingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();

            // Spawn particles at origin
            for (int i = 0; i < 20; i++) {
                double offsetX = (Math.random() - 0.5) * 0.5;
                double offsetY = (Math.random() - 0.5) * 0.5;
                double offsetZ = (Math.random() - 0.5) * 0.5;

                level.addParticle(
                        ParticleTypes.END_ROD,
                        packet.origin().getX() + 0.5 + offsetX,
                        packet.origin().getY() + 0.5 + offsetY,
                        packet.origin().getZ() + 0.5 + offsetZ,
                        0, 0.05, 0
                );
            }

            // Spawn particles along network path
            for (BlockPos pos : packet.networkNodes()) {
                if (pos.equals(packet.origin())) continue;

                // Spawn fewer particles at each node
                for (int i = 0; i < 5; i++) {
                    double offsetX = (Math.random() - 0.5) * 0.3;
                    double offsetY = (Math.random() - 0.5) * 0.3;
                    double offsetZ = (Math.random() - 0.5) * 0.3;

                    level.addParticle(
                            ParticleTypes.ELECTRIC_SPARK,
                            pos.getX() + 0.5 + offsetX,
                            pos.getY() + 0.5 + offsetY,
                            pos.getZ() + 0.5 + offsetZ,
                            0, 0, 0
                    );
                }
            }
        });
    }
}
