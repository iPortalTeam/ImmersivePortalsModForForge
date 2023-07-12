package qouteall.imm_ptl.core.platform_specific;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.IPModMain;
import qouteall.imm_ptl.core.commands.PortalCommand;
import qouteall.imm_ptl.core.compat.GravityChangerInterface;
import qouteall.imm_ptl.core.compat.IPModCompatibilityWarning;
import qouteall.imm_ptl.core.platform_specific.forge.networking.IPMessage;
import qouteall.imm_ptl.core.portal.custom_portal_gen.CustomPortalGenManagement;
import qouteall.q_misc_util.Helper;

import static qouteall.imm_ptl.core.platform_specific.IPModEntry.MODID;

@Mod(MODID)
public class IPModEntry {

    public static final String MODID = "imm_ptl_core";

    public IPModEntry() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            IPModEntryClient.onInitializeClient();
        }

        IPConfig.register(new ForgeConfigSpec.Builder());
        FMLJavaModLoadingContext.get().getModEventBus().register(IPConfig.class);
        MinecraftForge.EVENT_BUS.addListener(IPModEntry::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(IPModEntry::onPlayerChangeDimension);
        FMLJavaModLoadingContext.get().getModEventBus().register(IPModEntry.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(IPRegistry.class);

        IPModMain.init();
        RequiemCompat.init();

        IPRegistry.registerEntities(FMLJavaModLoadingContext.get().getModEventBus());
        
        IPRegistry.registerMyDimensionsFabric();
        
        IPRegistry.registerChunkGenerators();
        
        if (ModList.get().isLoaded("dimthread")) {
            O_O.isDimensionalThreadingPresent = true;
            Helper.log("Dimensional Threading is present");
        }
        else {
            Helper.log("Dimensional Threading is not present");
        }
        
        if (O_O.getIsPehkuiPresent()) {
            PehkuiInterfaceInitializer.init();
            Helper.log("Pehkui is present");
        }
        else {
            Helper.log("Pehkui is not present");
        }
        
        if (ModList.get().isLoaded("gravitychanger")) {
            GravityChangerInterface.invoker = new GravityChangerInterface.OnGravityChangerPresent();
            Helper.log("Gravity Changer is present");
        }
        else {
            Helper.log("Gravity Changer is not present");
        }
        
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        Helper.log(MODID + " commonSetup called");
        IPMessage.register();
        IPModCompatibilityWarning.initDedicatedServer();
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        Helper.log(MODID + " registerCommands called");
        PortalCommand.register(event.getDispatcher());
    }

    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (((IsVanilla) (Object) event).isImmptl_vanilla()) {
            onBeforeDimensionTravel((ServerPlayer) event.getPlayer());
        }
    }

    private static void onBeforeDimensionTravel(ServerPlayer player) {
        CustomPortalGenManagement.onBeforeConventionalDimensionChange(player);
        IPGlobal.chunkDataSyncManager.removePlayerFromChunkTrackersAndEntityTrackers(player);

        IPGlobal.serverTaskList.addTask(() -> {
            CustomPortalGenManagement.onAfterConventionalDimensionChange(player);
            return true;
        });
    }
    
}
