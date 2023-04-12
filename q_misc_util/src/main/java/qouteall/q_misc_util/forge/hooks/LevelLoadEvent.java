package qouteall.q_misc_util.forge.hooks;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.forge.networking.Dim_Sync;
import qouteall.q_misc_util.forge.networking.Message;

public class LevelLoadEvent {

    private static boolean hasStarted = false;

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load dimensionLoadEvent) {
        if (!dimensionLoadEvent.getLevel().isClientSide()) {
            if (hasStarted) {
                Dim_Sync dimSyncPacket = new Dim_Sync();
                for (ServerPlayer player : MiscHelper.getServer().getPlayerList().getPlayers()) {
                    Message.sendToPlayer(dimSyncPacket, player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload dimensionUnloadEvent) {
        if (!dimensionUnloadEvent.getLevel().isClientSide()) {
            if (hasStarted) {
                Dim_Sync dimSyncPacket = new Dim_Sync();
                for (ServerPlayer player : MiscHelper.getServer().getPlayerList().getPlayers()) {
                    Message.sendToPlayer(dimSyncPacket, player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void serverHasStarted(ServerStartedEvent serverStartedEvent) {
        hasStarted = true;
    }

    @SubscribeEvent
    public static void serverHasStopped(ServerStoppedEvent serverStoppedEvent) {
        hasStarted = false;
    }
}