package qouteall.imm_ptl.core.compat.mixin;

import me.jellysquid.mods.sodium.client.gl.GlObject;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import org.lwjgl.opengl.GL20C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.render.FrontClipping;
import qouteall.q_misc_util.Helper;

@Pseudo
@Mixin(value = ChunkShaderInterface.class, remap = false)
public class MixinSodiumChunkShaderInterface {
    @Unique
    private int uIPClippingEquation = -1;

    public MixinSodiumChunkShaderInterface() {
    }

    @Unique
    private void ip_init(int shaderId) {
        this.uIPClippingEquation = GL20C.glGetUniformLocation(shaderId, "imm_ptl_ClippingEquation");
        if (this.uIPClippingEquation < 0) {
            Helper.err("uniform imm_ptl_ClippingEquation not found in transformed sodium shader");
            this.uIPClippingEquation = -1;
        }

    }

    @Inject(
            method = {"<init>"},
            at = {@At("RETURN")},
            require = 0,
            remap = false
    )
    private void onInit(ShaderBindingContext context, ChunkShaderOptions options, CallbackInfo ci) {
        if (context instanceof GlObject glObject) {
            this.ip_init(glObject.handle());
        } else {
            Helper.log("Skipping sodium shader init injection");
        }

    }

    @Inject(
            method = {"setupState"},
            at = {@At("RETURN")},
            remap = false
    )
    private void onSetup(CallbackInfo ci) {
        if (this.uIPClippingEquation != -1) {
            if (FrontClipping.isClippingEnabled) {
                double[] equation = FrontClipping.getActiveClipPlaneEquationForEntities();
                GL20C.glUniform4f(this.uIPClippingEquation, (float)equation[0], (float)equation[1], (float)equation[2], (float)equation[3]);
            } else {
                GL20C.glUniform4f(this.uIPClippingEquation, 0.0F, 0.0F, 0.0F, 1.0F);
            }
        }

    }
}
