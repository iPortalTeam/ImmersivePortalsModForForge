package qouteall.imm_ptl.peripheral.platform_specific;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import qouteall.imm_ptl.peripheral.PeripheralModMain;

public class PeripheralModEntryClient {
    public static void registerBlockRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(
            PeripheralModEntry.PORTAL_HELPER_BLOCK.get(),
            RenderType.cutout()
        );
    }

    public static void onInitializeClient() {
        PeripheralModEntryClient.registerBlockRenderLayers();
        
        PeripheralModMain.initClient();
    }
}
