package qouteall.imm_ptl.peripheral.platform_specific;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import qouteall.imm_ptl.peripheral.CommandStickItem;
import qouteall.imm_ptl.peripheral.PeripheralModMain;
import qouteall.imm_ptl.peripheral.alternate_dimension.PeripheralRegistries;
import qouteall.imm_ptl.peripheral.wand.PortalWandItem;

import javax.annotation.Nullable;
import java.util.List;

import static qouteall.imm_ptl.peripheral.platform_specific.PeripheralModEntry.MODID;

@Mod(MODID)
public class PeripheralModEntry {

    public static final String MODID = "immersive_portals";
    public static class PortalHelperItem extends BlockItem {
        
        public PortalHelperItem(Block block, Properties settings) {
            super(block, settings);
        }
        
        @Override
        public InteractionResult useOn(UseOnContext context) {
            if (context.getLevel().isClientSide()) {
                if (context.getPlayer() != null) {
                    // TODO @Nick1st
                    //IPOuterClientMisc.onClientPlacePortalHelper();
                }
            }
            
            return super.useOn(context);
        }
        
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
            super.appendHoverText(stack, world, tooltip, context);
            
            tooltip.add(Component.translatable("imm_ptl.portal_helper_tooltip"));
        }
    }

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Block> PORTAL_HELPER_BLOCK = BLOCKS.register("portal_helper", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).noOcclusion().isRedstoneConductor((a, b, c) -> false)));
    public static final RegistryObject<Item> PORTAL_HELPER_ITEM = ITEMS.register("portal_helper", () -> new PortalHelperItem(PORTAL_HELPER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> COMMAND_STICK_ITEM = ITEMS.register("command_stick", () -> new CommandStickItem(new Item.Properties()));
    public static final RegistryObject<Item> PORTAL_WAND = ITEMS.register("portal_wand", () -> new PortalWandItem(new Item.Properties()));

    private static void registerBlockItems() {
        //PeripheralModMain.registerCommandStickTypes();
        
        CommandStickItem.init();
    }

    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to creative tab
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(PORTAL_HELPER_ITEM.get().getDefaultInstance());
            event.accept(PORTAL_HELPER_ITEM.get().getDefaultInstance());
        }
    }

    public PeripheralModEntry() {
        FMLJavaModLoadingContext.get().getModEventBus().register(PeripheralModEntry.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommandStickItem::buildContents);
        PeripheralModEntry.registerBlockItems(); //TODO Move this to a real DeferredRegistry @Nick1st
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PeripheralRegistries.CHUNK_GENERATOR.register(FMLJavaModLoadingContext.get().getModEventBus());
        PeripheralRegistries.BIOME_SOURCE.register(FMLJavaModLoadingContext.get().getModEventBus());
        CommandStickItem.CommandStickData.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        PeripheralModMain.init();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PeripheralModEntryClient.onInitializeClient();
    }
}
