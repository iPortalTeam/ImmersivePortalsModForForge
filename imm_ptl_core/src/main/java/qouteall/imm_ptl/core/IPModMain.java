package qouteall.imm_ptl.core;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import qouteall.imm_ptl.core.chunk_loading.*;
import qouteall.imm_ptl.core.commands.AxisArgumentType;
import qouteall.imm_ptl.core.commands.PortalCommand;
import qouteall.imm_ptl.core.commands.SubCommandArgumentType;
import qouteall.imm_ptl.core.commands.TimingFunctionArgumentType;
import qouteall.imm_ptl.core.compat.IPPortingLibCompat;
import qouteall.imm_ptl.core.miscellaneous.GcMonitor;
import qouteall.imm_ptl.core.network.IPNetworking;
import qouteall.imm_ptl.core.platform_specific.IPConfig;
import qouteall.imm_ptl.core.platform_specific.O_O;
import qouteall.imm_ptl.core.portal.PortalExtension;
import qouteall.imm_ptl.core.portal.animation.NormalAnimation;
import qouteall.imm_ptl.core.portal.animation.RotationAnimation;
import qouteall.imm_ptl.core.portal.global_portals.GlobalPortalStorage;
import qouteall.imm_ptl.core.teleportation.CollisionHelper;
import qouteall.imm_ptl.core.teleportation.ServerTeleportationManager;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.LifecycleHack;

import java.io.File;
import java.nio.file.Path;

public class IPModMain {
    
    public static void init() {
        loadConfig();
        
        Helper.log("Immersive Portals Mod Initializing");

        IPNetworking.init(); // TODO @Nick1st CHeck this out
        
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
        
//        CommandRegistrationCallback.EVENT.register( // TODO @Nick1st Check this out
//            (dispatcher, registryAccess, environment) -> PortalCommand.register(dispatcher)
//        );
//        SubCommandArgumentType.init();
//        TimingFunctionArgumentType.init();
//        AxisArgumentType.init();
        
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
    
    private static void loadConfig() {
        // upgrade old config
        Path gameDir = O_O.getGameDir();
        File oldConfigFile = gameDir.resolve("config").resolve("immersive_portals_fabric.json").toFile();
        if (oldConfigFile.exists()) {
            File dest = gameDir.resolve("config").resolve("immersive_portals.json").toFile();
            boolean succeeded = oldConfigFile.renameTo(dest);
            if (succeeded) {
                Helper.log("Upgraded old config file");
            }
            else {
                Helper.err("Failed to upgrade old config file");
            }
        }

        Helper.log("Loading Immersive Portals config");
        IPGlobal.configHolder = AutoConfig.register(IPConfig.class, GsonConfigSerializer::new);
        IPGlobal.configHolder.registerSaveListener((configHolder, ipConfig) -> {
            ipConfig.onConfigChanged();
            return InteractionResult.SUCCESS;
        });
        IPConfig ipConfig = IPConfig.readConfig();
        ipConfig.onConfigChanged();
    }
}
