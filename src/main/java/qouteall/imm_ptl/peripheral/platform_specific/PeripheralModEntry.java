package qouteall.imm_ptl.peripheral.platform_specific;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import qouteall.imm_ptl.peripheral.CommandStickItem;
import qouteall.imm_ptl.peripheral.PeripheralModMain;
import qouteall.imm_ptl.peripheral.guide.IPGuide;

import javax.annotation.Nullable;
import java.util.List;

@Mod("immersive_portals")
public class PeripheralModEntry {
    public static class PortalHelperItem extends BlockItem {
        
        public PortalHelperItem(Block block, Properties settings) {
            super(block, settings);
        }
        
        @Override
        public InteractionResult useOn(UseOnContext context) {
            if (context.getLevel().isClientSide()) {
                if (context.getPlayer() != null) {
                    IPGuide.onClientPlacePortalHelper();
                }
            }
            
            return super.useOn(context);
        }
        
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
            super.appendHoverText(stack, world, tooltip, context);
            
            tooltip.add(new TranslatableComponent("imm_ptl.portal_helper_tooltip"));
        }
    }

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "immersive_portals");
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "immersive_portals");

    public static final RegistryObject<Block> PORTAL_HELPER_BLOCK = BLOCKS.register("portal_helper", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().isRedstoneConductor((a, b, c) -> false)));
    public static final RegistryObject<Item> PORTAL_HELPER_ITEM = ITEMS.register("portal_helper", () -> new PortalHelperItem(PORTAL_HELPER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> COMMAND_STICK_ITEM = ITEMS.register("command_stick", () -> new CommandStickItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    
    private static void registerBlockItems() {
        CommandStickItem.CommandStickData.register(FMLJavaModLoadingContext.get().getModEventBus());
        //PeripheralModMain.registerCommandStickTypes();
        
        CommandStickItem.init();
    }

    public PeripheralModEntry() {
        FMLJavaModLoadingContext.get().getModEventBus().register(PeripheralModEntry.class);
        PeripheralModEntry.registerBlockItems(); //TODO Move this to a DeferredRegistry !IMPORTANT
        //CommandStickItem.CommandStickData.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        PeripheralModMain.init();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PeripheralModEntryClient.onInitializeClient();
    }
}
