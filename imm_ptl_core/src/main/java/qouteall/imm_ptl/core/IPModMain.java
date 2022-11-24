package qouteall.imm_ptl.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import qouteall.imm_ptl.core.chunk_loading.*;
import qouteall.imm_ptl.core.commands.PortalCommand;
import qouteall.imm_ptl.core.commands.SubCommandArgumentType;
import qouteall.imm_ptl.core.compat.IPPortingLibCompat;
import qouteall.imm_ptl.core.miscellaneous.GcMonitor;
import qouteall.imm_ptl.core.platform_specific.O_O;
import qouteall.imm_ptl.core.portal.PortalExtension;
import qouteall.imm_ptl.core.portal.animation.NormalAnimation;
import qouteall.imm_ptl.core.portal.animation.RotationAnimation;
import qouteall.imm_ptl.core.portal.global_portals.GlobalPortalStorage;
import qouteall.imm_ptl.core.teleportation.CollisionHelper;
import qouteall.imm_ptl.core.teleportation.ServerTeleportationManager;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.LifecycleHack;

public class IPModMain {
    
    public static void init() {
        O_O.loadConfigFabric();
        
        Helper.log("Immersive Portals Mod Initializing");

        IPGlobal.postClientTickSignal.connect(IPGlobal.clientTaskList::processTasks);
        IPGlobal.postServerTickSignal.connect(IPGlobal.serverTaskList::processTasks);
        IPGlobal.preGameRenderSignal.connect(IPGlobal.preGameRenderTaskList::processTasks);
        
        IPGlobal.clientCleanupSignal.connect(() -> {
            if (ClientWorldLoader.getIsInitialized()) {
                IPGlobal.clientTaskList.forceClearTasks();
            }
        });
        IPGlobal.serverCleanupSignal.connect(IPGlobal.serverTaskList::forceClearTasks);
        
        IPGlobal.serverTeleportationManager = new ServerTeleportationManager();
        IPGlobal.chunkDataSyncManager = new ChunkDataSyncManager();
        
        NewChunkTrackingGraph.init();
        
        WorldInfoSender.init();
        
        GlobalPortalStorage.init();
        
        EntitySync.init();
        
        CollisionHelper.init();
        
        PortalExtension.init();
        
        GcMonitor.initCommon();
        
        ServerPerformanceMonitor.init();
        
        MyLoadingTicket.init();
        
        IPPortingLibCompat.init();
        
//        CommandRegistrationCallback.EVENT.register(
//            (dispatcher, registryAccess, environment) -> PortalCommand.register(dispatcher)
//        );
        SubCommandArgumentType.init();
        
        // intrinsic animation driver types
        RotationAnimation.init();
        NormalAnimation.init();
        
        LifecycleHack.markNamespaceStable("immersive_portals");
        LifecycleHack.markNamespaceStable("imm_ptl");

        MinecraftForge.EVENT_BUS.register(IPModMain.class);
        MinecraftForge.EVENT_BUS.register(IPModMainClient.class);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        PortalCommand.register(event.getDispatcher());
    }
    
}
