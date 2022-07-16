package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILEimport net.coderbot.iris.postprocess.FinalPassRenderer;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.IPCGlobal;

import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;

//DISABLED_COMPILE@Mixin(value = FinalPassRenderer.class, remap = false)
public class MixinIrisFinalPassRenderer {
//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "renderFinalPass", at = @At("HEAD")
//DISABLED_COMPILE    )
    void onRenderFinalPass(CallbackInfo ci) {
        if (IPCGlobal.debugEnableStencilWithIris) {
            GL11.glDisable(GL_STENCIL_TEST);
        }
    }
}
