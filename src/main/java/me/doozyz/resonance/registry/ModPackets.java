package me.doozyz.resonance.registry;

import me.doozyz.resonance.network.NetworkPingPacket;
import me.doozyz.resonance.network.ResonanceStatePacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPackets {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModPackets::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        // Server to Client packets
        registrar.playToClient(
                ResonanceStatePacket.TYPE,
                ResonanceStatePacket.STREAM_CODEC,
                ResonanceStatePacket::handle
        );

        registrar.playToClient(
                NetworkPingPacket.TYPE,
                NetworkPingPacket.STREAM_CODEC,
                NetworkPingPacket::handle
        );
    }
}
