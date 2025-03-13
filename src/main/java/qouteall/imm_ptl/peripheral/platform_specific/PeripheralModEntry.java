package qouteall.imm_ptl.peripheral.platform_specific;

import net.minecraft.core.registries.Registries;
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
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Block> PORTAL_HELPER_BLOCK = BLOCKS.register("portal_helper", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).noOcclusion().isRedstoneConductor((a, b, c) -> false)));
    public static final RegistryObject<Item> PORTAL_HELPER_ITEM = ITEMS.register("portal_helper", () -> new PortalHelperItem(PORTAL_HELPER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> COMMAND_STICK_ITEM = ITEMS.register("command_stick", () -> new CommandStickItem(new Item.Properties()));
    public static final RegistryObject<Item> PORTAL_WAND = ITEMS.register("portal_wand", () -> new PortalWandItem(new Item.Properties()));
    public static final RegistryObject<CreativeModeTab> PERIPHERAL_TAB = TABS.register("misc", () -> CreativeModeTab.builder()
            .title(Component.translatable("imm_ptl.peripheral_tooltip"))
            .icon(PORTAL_WAND.get()::getDefaultInstance)
            .displayItems((displayParameters, output) -> {
                output.accept(PORTAL_WAND.get());
                output.accept(PORTAL_HELPER_ITEM.get());
                CommandStickItem.CommandStickData.getEntries().forEach(entry -> {
                   ItemStack stack = COMMAND_STICK_ITEM.get().getDefaultInstance();
                   stack.setTag(entry.get().toTag());
                   output.accept(stack);
                });
            })
            .build());

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

    public PeripheralModEntry(FMLJavaModLoadingContext context) {
        context.getModEventBus().register(PeripheralModEntry.class);
        context.getModEventBus().addListener(CommandStickItem::buildContents);
        PeripheralModEntry.registerBlockItems(); //TODO Move this to a real DeferredRegistry @Nick1st
        BLOCKS.register(context.getModEventBus());
        ITEMS.register(context.getModEventBus());
        TABS.register(context.getModEventBus());
        PeripheralRegistries.CHUNK_GENERATOR.register(context.getModEventBus());
        PeripheralRegistries.BIOME_SOURCE.register(context.getModEventBus());
        CommandStickItem.CommandStickData.register(context.getModEventBus());
        
        PeripheralModMain.init();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PeripheralModEntryClient.onInitializeClient();
    }
}
