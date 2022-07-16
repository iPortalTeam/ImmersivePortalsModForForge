package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILEimport net.coderbot.iris.compat.sodium.impl.shader_overrides.IrisChunkShaderInterface;
//DISABLED_COMPILEimport net.coderbot.iris.compat.sodium.impl.shader_overrides.ShaderBindingContextExt;
//DISABLED_COMPILEimport net.coderbot.iris.gl.blending.BlendModeOverride;
//DISABLED_COMPILEimport net.coderbot.iris.pipeline.SodiumTerrainPipeline;

import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL21;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.render.FrontClipping;
import qouteall.q_misc_util.Helper;

//DISABLED_COMPILE@Mixin(value = IrisChunkShaderInterface.class, remap = false)
public class MixinIrisSodiumChunkShaderInterface {
    private int uIPClippingEquation;
    
    private void ip_init(int shaderId) {
        uIPClippingEquation = GL20C.glGetUniformLocation(shaderId, "imm_ptl_ClippingEquation");
        if (uIPClippingEquation < 0) {
            Helper.err("uniform imm_ptl_ClippingEquation not found in transformed iris shader");
            uIPClippingEquation = -1;
        }
    }

//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "<init>",
//DISABLED_COMPILE        at = @At("RETURN"),
//DISABLED_COMPILE        require = 0
//DISABLED_COMPILE    )
    private void onInit(
//DISABLED_COMPILE        int handle, ShaderBindingContextExt contextExt, SodiumTerrainPipeline pipeline,
//DISABLED_COMPILE        boolean isShadowPass, BlendModeOverride blendModeOverride, float alpha,
//DISABLED_COMPILE        CallbackInfo ci
    ) {
//DISABLED_COMPILE        ip_init(handle);
    }

//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "setup",
//DISABLED_COMPILE        at = @At("RETURN")
//DISABLED_COMPILE    )
    private void onSetup(CallbackInfo ci) {
        if (uIPClippingEquation != -1) {
            if (FrontClipping.isClippingEnabled) {
                double[] equation = FrontClipping.getActiveClipPlaneEquationForEntities();
                GL21.glUniform4f(
                    uIPClippingEquation,
                    (float) equation[0],
                    (float) equation[1],
                    (float) equation[2],
                    (float) equation[3]
                );
            }
            else {
                GL21.glUniform4f(
                    uIPClippingEquation,
                    0, 0, 0, 1
                );
            }
        }
    }
}
