package qouteall.imm_ptl.core.platform_specific;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.Validate;
import qouteall.imm_ptl.core.*;
import qouteall.imm_ptl.core.compat.IPModCompatibilityWarning;
import qouteall.imm_ptl.core.compat.iris_compatibility.ExperimentalIrisPortalRenderer;
import qouteall.imm_ptl.core.compat.iris_compatibility.IrisInterface;
import qouteall.imm_ptl.core.compat.sodium_compatibility.SodiumInterface;
import qouteall.imm_ptl.core.render.*;
import qouteall.imm_ptl.core.teleportation.ClientTeleportationManager;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.my_util.MyTaskList;

import java.util.Arrays;

public class IPModEntryClient {

    @SubscribeEvent
    public static void initPortalRenderers(EntityRenderersEvent.RegisterRenderers event) {
        
        Arrays.stream(new EntityType<?>[]{
                IPRegistry.PORTAL.get(),
                IPRegistry.NETHER_PORTAL_NEW.get(),
                IPRegistry.END_PORTAL.get(),
                IPRegistry.MIRROR.get(),
                IPRegistry.BREAKABLE_MIRROR.get(),
                IPRegistry.GLOBAL_TRACKED_PORTAL.get(),
                IPRegistry.BORDER_PORTAL.get(),
                IPRegistry.END_FLOOR_PORTAL.get(),
                IPRegistry.GENERAL_BREAKABLE_PORTAL.get()
        }).peek(
            Validate::notNull
        ).forEach(
            entityType -> event.registerEntityRenderer(
                entityType,
                (EntityRendererProvider) PortalEntityRenderer::new
            )
        );

        event.registerEntityRenderer(IPRegistry.LOADING_INDICATOR.get(), LoadingIndicatorRenderer::new);
        
    }

    public static void onInitializeClient() {
        FMLJavaModLoadingContext.get().getModEventBus().register(IPModMainClient.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(IPModEntryClient.class);
        
        boolean isSodiumPresent =
                ModList.get().isLoaded("rubidium");
        if (isSodiumPresent) {
            Helper.log("Rubidium is present");
            
            SodiumInterface.invoker = new SodiumInterface.OnSodiumPresent();
            
            IPGlobal.clientTaskList.addTask(MyTaskList.oneShotTask(() -> {
                if (IPGlobal.enableWarning) {
                    CHelper.printChat(
                        new TranslatableComponent("imm_ptl.sodium_warning")
                            .append(IPMcHelper.getDisableWarningText())
                    );
                }
            }));
        }
        else {
            Helper.log("Rubidium is not present");
        }
        
        if (ModList.get().isLoaded("oculus")) {
            Helper.log("Oculus is present");
            IrisInterface.invoker = new IrisInterface.OnIrisPresent();
            ExperimentalIrisPortalRenderer.init();
            
            IPGlobal.clientTaskList.addTask(MyTaskList.oneShotTask(() -> {
                if (IPGlobal.enableWarning) {
                    CHelper.printChat(
                        new TranslatableComponent("imm_ptl.iris_warning")
                            .append(IPMcHelper.getDisableWarningText())
                    );
                }
            }));
        }
        else {
            Helper.log("Oculus is not present");
        }
        
        IPModCompatibilityWarning.initClient();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().execute(() -> {
                ShaderCodeTransformation.init();

                MyRenderHelper.init();

                IPCGlobal.rendererUsingStencil = new RendererUsingStencil();
                IPCGlobal.rendererUsingFrameBuffer = new RendererUsingFrameBuffer();

                IPCGlobal.renderer = IPCGlobal.rendererUsingStencil;
                IPCGlobal.clientTeleportationManager = new ClientTeleportationManager();
            });
        }
    }
    
}
