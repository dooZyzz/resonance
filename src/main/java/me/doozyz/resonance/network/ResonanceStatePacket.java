package me.doozyz.resonance.network;

import io.netty.buffer.ByteBuf;
import me.doozyz.resonance.content.blockentity.CrystalBlockEntity;
import me.doozyz.resonance.support.ModRef;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Packet to sync resonance state to clients
 */
public record ResonanceStatePacket(
        BlockPos pos,
        float frequency,
        float amplitude,
        float phase
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ResonanceStatePacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModRef.MODID, "resonance_state"));

    public static final StreamCodec<ByteBuf, ResonanceStatePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ResonanceStatePacket::pos,
            ByteBufCodecs.FLOAT, ResonanceStatePacket::frequency,
            ByteBufCodecs.FLOAT, ResonanceStatePacket::amplitude,
            ByteBufCodecs.FLOAT, ResonanceStatePacket::phase,
            ResonanceStatePacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ResonanceStatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().getBlockEntity(packet.pos()) instanceof CrystalBlockEntity crystal) {
                crystal.updateClientState(packet.frequency(), packet.amplitude(), packet.phase());
            }
        });
    }
}
