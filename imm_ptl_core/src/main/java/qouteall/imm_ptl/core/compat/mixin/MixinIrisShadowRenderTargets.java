package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILEimport net.coderbot.iris.gl.texture.InternalTextureFormat;
//DISABLED_COMPILEimport net.coderbot.iris.shadows.ShadowRenderTargets;

import qouteall.imm_ptl.core.compat.iris_compatibility.IEIrisShadowRenderTargets;

//DISABLED_COMPILE@Mixin(value = ShadowRenderTargets.class, remap = false)
public class MixinIrisShadowRenderTargets implements IEIrisShadowRenderTargets {
//    ShadowMapSwapper ip_shadowMapSwapper;
//
//    @Inject(
//        method = "<init>",
//        at = @At("RETURN")
//    )
//    void onInit(int resolution, InternalTextureFormat[] formats, CallbackInfo ci) {
//        ip_shadowMapSwapper = new ShadowMapSwapper(resolution, (ShadowRenderTargets) (Object) this);
//    }
//
//    @Inject(
//        method = "destroy",
//        at = @At("HEAD")
//    )
//    private void onDestroy(CallbackInfo ci) {
//        ip_shadowMapSwapper.dispose();
//        ip_shadowMapSwapper = null;
//    }
//
//    @Override
//    public ShadowMapSwapper getShadowMapSwapper() {
//        return ip_shadowMapSwapper;
//    }
}
