package qouteall.imm_ptl.core.platform_specific;

//import com.fusionflux.gravity_api.util.GravityChannel;
//import com.fusionflux.gravity_api.util.packet.DefaultGravityPacket;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import qouteall.imm_ptl.core.IPModMain;
import qouteall.imm_ptl.core.IPModMainClient;
import qouteall.imm_ptl.core.commands.AxisArgumentType;
import qouteall.imm_ptl.core.commands.PortalCommand;
import qouteall.imm_ptl.core.commands.SubCommandArgumentType;
import qouteall.imm_ptl.core.commands.TimingFunctionArgumentType;
import qouteall.imm_ptl.core.platform_specific.forge.networking.IPMessage;
import qouteall.q_misc_util.Helper;

import static qouteall.imm_ptl.core.platform_specific.IPModEntry.MODID;

@Mod(MODID)
public class IPModEntry {

    public static final String MODID = "imm_ptl_core";

    public IPModEntry(FMLJavaModLoadingContext context) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            IPModEntryClient.onInitializeClient(context);
        }

        MinecraftForge.EVENT_BUS.register(IPModMain.class);
        MinecraftForge.EVENT_BUS.register(IPModMainClient.class);

//        IPConfig.register(new ForgeConfigSpec.Builder()); //TODO @Nick1st Check if config is used / functioning
        context.getModEventBus().register(IPConfig.class);
        MinecraftForge.EVENT_BUS.addListener(IPModEntry::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(IPModEntry::commonSetup);
        context.getModEventBus().register(IPModEntry.class);
        context.getModEventBus().register(IPRegistry.class);
        context.getModEventBus().register(SubCommandArgumentType.class);
        context.getModEventBus().register(TimingFunctionArgumentType.class);
        context.getModEventBus().register(AxisArgumentType.class);

        IPModMain.init();
        RequiemCompat.init();

        IPRegistry.registerEntities(context.getModEventBus());
        
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
            //GravityChangerInterface.invoker = new GravityChangerInterface.OnGravityChangerPresent(); // TODO @Nick1st GravityAPI does not exist for Forge
            Helper.log("Gravity API is present");
        }
        else {
            Helper.log("Gravity API is not present");
        }
        
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        Helper.log(MODID + " commonSetup called");
        IPMessage.register();
//        IPModCompatibilityWarning.initDedicatedServer();
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        Helper.log(MODID + " registerCommands called");
        PortalCommand.register(event.getDispatcher());
    }
    
}
