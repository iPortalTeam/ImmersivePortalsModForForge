package qouteall.q_misc_util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import qouteall.q_misc_util.dimension.DimensionMisc;
import qouteall.q_misc_util.dimension.DimsCommand;
import qouteall.q_misc_util.dimension.DynamicDimensionsImpl;
import qouteall.q_misc_util.dimension.ExtraDimensionStorage;
import qouteall.q_misc_util.forge.networking.Message;

@Mod("q_misc_util")
public class MiscUtilModEntry {

    public MiscUtilModEntry() {
        DimensionMisc.init();

        ExtraDimensionStorage.init();

        DynamicDimensionsImpl.init();

        MiscNetworking.init();

        FMLJavaModLoadingContext.get().getModEventBus().register(MiscUtilModEntry.class);
        MinecraftForge.EVENT_BUS.addListener(MiscUtilModEntry::serverTick);
        MinecraftForge.EVENT_BUS.addListener(MiscUtilModEntry::registerCommand);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MiscUtilModEntryClient.onInitializeClient();
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        Message.register();
    }

    public static void serverTick(TickEvent.ServerTickEvent event) {
        MiscGlobals.serverTaskList.processTasks();
    }

    public static void registerCommand(RegisterCommandsEvent event) {
        DimsCommand.register(event.getDispatcher());
    }
}
