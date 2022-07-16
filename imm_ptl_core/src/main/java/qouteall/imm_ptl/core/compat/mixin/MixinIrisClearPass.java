package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILE import net.coderbot.iris.pipeline.ClearPass;
//DISABLED_COMPILE import net.coderbot.iris.vendored.joml.Vector4f;

//DISABLED_COMPILE @Mixin(value = ClearPass.class, remap = false)
public class MixinIrisClearPass {
//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "execute", at = @At("HEAD"), cancellable = true
//DISABLED_COMPILE    )
//DISABLED_COMPILE    private void onExecute(Vector4f par1, CallbackInfo ci) {
//DISABLED_COMPILE        if (IPCGlobal.renderer instanceof ExperimentalIrisPortalRenderer) {
//DISABLED_COMPILE            if (PortalRendering.isRendering()) {
//DISABLED_COMPILE                ci.cancel();
//DISABLED_COMPILE            }
//DISABLED_COMPILE        }
//DISABLED_COMPILE    }
}
