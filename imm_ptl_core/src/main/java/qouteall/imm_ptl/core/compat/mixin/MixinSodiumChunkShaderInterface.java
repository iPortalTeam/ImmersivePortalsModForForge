package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILEimport me.jellysquid.mods.sodium.client.gl.GlObject;
//DISABLED_COMPILEimport me.jellysquid.mods.sodium.client.model.vertex.type.ChunkVertexType;
//DISABLED_COMPILEimport me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
//DISABLED_COMPILEimport me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderBindingContext;

import org.lwjgl.opengl.GL20C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import qouteall.q_misc_util.Helper;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface", remap = false)
public class MixinSodiumChunkShaderInterface {
    private int uIPClippingEquation;
    
    private void ip_init(int shaderId) {
        uIPClippingEquation = GL20C.glGetUniformLocation(shaderId, "imm_ptl_ClippingEquation");
        if (uIPClippingEquation < 0) {
            Helper.err("uniform imm_ptl_ClippingEquation not found in transformed sodium shader");
            uIPClippingEquation = -1;
        }
    }

//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "<init>",
//DISABLED_COMPILE        at = @At("RETURN"),
//DISABLED_COMPILE        require = 0,
//DISABLED_COMPILE        remap = false
//DISABLED_COMPILE    )
//DISABLED_COMPILE    private void onInit(
//DISABLED_COMPILE        ShaderBindingContext context,
//DISABLED_COMPILE        ChunkShaderOptions options,
//DISABLED_COMPILE        CallbackInfo ci
//DISABLED_COMPILE    ) {
//DISABLED_COMPILE        ip_init(((GlObject) context).handle());
//DISABLED_COMPILE    }

//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "setup",
//DISABLED_COMPILE        at = @At("RETURN"),
//DISABLED_COMPILE        remap = false
//DISABLED_COMPILE    )
//DISABLED_COMPILE    private void onSetup(ChunkVertexType vertexType, CallbackInfo ci) {
//DISABLED_COMPILE        if (uIPClippingEquation != -1) {
//DISABLED_COMPILE            if (FrontClipping.isClippingEnabled) {
//DISABLED_COMPILE                double[] equation = FrontClipping.getActiveClipPlaneEquationForEntities();
//DISABLED_COMPILE                GL20C.glUniform4f(
//DISABLED_COMPILE                    uIPClippingEquation,
    //DISABLED_COMPILE                   (float) equation[0],
    //DISABLED_COMPILE                   (float) equation[1],
    //DISABLED_COMPILE                   (float) equation[2],
    //DISABLED_COMPILE                   (float) equation[3]
    //DISABLED_COMPILE               );
    //DISABLED_COMPILE           }
//DISABLED_COMPILE           else {
//DISABLED_COMPILE                GL20C.glUniform4f(
//DISABLED_COMPILE                    uIPClippingEquation,
//DISABLED_COMPILE                    0, 0, 0, 1
    //DISABLED_COMPILE               );
    //DISABLED_COMPILE           }
    //DISABLED_COMPILE       }
//DISABLED_COMPILE    }
}
