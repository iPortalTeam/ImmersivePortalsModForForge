package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import qouteall.imm_ptl.core.platform_specific.IPModEntry;

public class IPMessage {
    public static SimpleChannel INSTANCE;

    // Every packet needs a unique ID (unique for this channel)
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        // Make the channel. If needed you can do version checking here
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(IPModEntry.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();



        INSTANCE.messageBuilder(Dim_Confirm.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Dim_Confirm::new)
                .encoder(Dim_Confirm::toBytes)
                .consumer(Dim_Confirm::handle)
                .add();

        INSTANCE.messageBuilder(Spawn_Entity.class, id(), NetworkDirection.PLAY_TO_CLIENT) //Actually find out if we need an override or if this is fine (It seems to be)
                .decoder(Spawn_Entity::new)
                .encoder(Spawn_Entity::toBytes)
                .consumer(Spawn_Entity::handle)
                .add();

        INSTANCE.messageBuilder(Teleport.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(Teleport::new)
                .encoder(Teleport::toBytes)
                .consumer(Teleport::handle)
                .add();

        INSTANCE.messageBuilder(PlayerAction.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlayerAction::new)
                .encoder(PlayerAction::toBytes)
                .consumer(PlayerAction::handle)
                .add();

        INSTANCE.messageBuilder(RightClick.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RightClick::new)
                .encoder(RightClick::toBytes)
                .consumer(RightClick::handle)
                .add();

        INSTANCE.messageBuilder(GlobalPortalUpdate.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GlobalPortalUpdate::new)
                .encoder(GlobalPortalUpdate::toBytes)
                .consumer(GlobalPortalUpdate::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
