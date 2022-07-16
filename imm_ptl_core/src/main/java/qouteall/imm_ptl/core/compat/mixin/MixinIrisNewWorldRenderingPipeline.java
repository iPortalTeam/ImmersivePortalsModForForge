package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILEimport net.coderbot.iris.pipeline.newshader.NewWorldRenderingPipeline;
//DISABLED_COMPILEimport net.coderbot.iris.shadows.ShadowRenderTargets;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.compat.iris_compatibility.IEIrisNewWorldRenderingPipeline;

//DISABLED_COMPILE@Mixin(value = NewWorldRenderingPipeline.class, remap = false)
public class MixinIrisNewWorldRenderingPipeline implements IEIrisNewWorldRenderingPipeline {
//DISABLED_COMPILE    @Shadow private boolean isRenderingWorld;
    
//    @Shadow private ShadowRenderTargets shadowRenderTargets;

//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "finalizeLevelRendering", at = @At("HEAD"), cancellable = true
    //DISABLED_COMPILE    )
    private void onFinalizeLevelRendering(CallbackInfo ci) {
//DISABLED_COMPILE        if (IPCGlobal.renderer instanceof ExperimentalIrisPortalRenderer) {
//DISABLED_COMPILE            if (PortalRendering.isRendering()) {
//DISABLED_COMPILE                ci.cancel();
//DISABLED_COMPILE            }
//DISABLED_COMPILE        }
    }

//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "beginTranslucents",
//DISABLED_COMPILE        at = @At(
//DISABLED_COMPILE            value = "INVOKE",
//DISABLED_COMPILE            target = "Lnet/coderbot/iris/postprocess/CompositeRenderer;renderAll()V",
//DISABLED_COMPILE            shift = At.Shift.AFTER
//DISABLED_COMPILE        )
//DISABLED_COMPILE    )
    private void onAfterDeferredCompositeRendering(CallbackInfo ci) {
//DISABLED_COMPILE        if (IPCGlobal.renderer instanceof ExperimentalIrisPortalRenderer r) {
//DISABLED_COMPILE            r.onAfterIrisDeferredCompositeRendering();
//DISABLED_COMPILE        }
    }
    
    @Override
    public void ip_setIsRenderingWorld(boolean cond) {
//DISABLED_COMPILE        isRenderingWorld = cond;
    }
    
//    @Override
//    public ShadowRenderTargets ip_getShadowRenderTargets() {
//        return shadowRenderTargets;
//    }
}
