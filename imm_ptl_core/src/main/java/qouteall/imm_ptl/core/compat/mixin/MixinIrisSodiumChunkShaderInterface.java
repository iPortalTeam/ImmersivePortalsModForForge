package qouteall.imm_ptl.core.compat.mixin;

import net.irisshaders.iris.compat.sodium.impl.shader_overrides.IrisChunkShaderInterface;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL21;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.render.FrontClipping;
import qouteall.q_misc_util.Helper;

@Mixin(value = IrisChunkShaderInterface.class, remap = false)
public class MixinIrisSodiumChunkShaderInterface {
    private int uIPClippingEquation;
    
    private void ip_init(int shaderId) {
        uIPClippingEquation = GL20C.glGetUniformLocation(shaderId, "imm_ptl_ClippingEquation");
        if (uIPClippingEquation < 0) {
            Helper.err("uniform imm_ptl_ClippingEquation not found in transformed iris shader");
            uIPClippingEquation = -1;
        }
    }
    
//    @Inject( // TODO @Nick1st Why is this identifier wrong?
//        method = "<init>",
//        at = @At("RETURN"),
//        require = 0
//    )
//    private void onInit(
//        int handle,
//        ShaderBindingContextExt par2, SodiumTerrainPipeline par3, boolean par4,
//        BlendModeOverride par5, List par6, float par7, CustomUniforms par8, CallbackInfo ci
//    ) {
//        ip_init(handle);
//    }
    
    @Inject(
        method = "setupState", // was setup before, not sure if same method
        at = @At("RETURN")
    )
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
