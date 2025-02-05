package qouteall.q_misc_util.forge.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Message {
    public static SimpleChannel INSTANCE;

    // Every packet needs a unique ID (unique for this channel)
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        // Make the channel. If needed you can do version checking here
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation("iputil", "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();


        // Register all our packets. We only have one right now. The new message has a unique ID, an indication
        // of how it is going to be used (from client to server) and ways to encode and decode it. Finally, 'handle'
        // will actually execute when the packet is received
        INSTANCE.messageBuilder(Dim_Sync.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Dim_Sync::new)
                .encoder(Dim_Sync::toBytes)
                .consumerMainThread(Dim_Sync::handle)
                .add();

        INSTANCE.messageBuilder(Remote_StC.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Remote_StC::new)
                .encoder(Remote_StC::toBytes)
                .consumerMainThread(Remote_StC::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}