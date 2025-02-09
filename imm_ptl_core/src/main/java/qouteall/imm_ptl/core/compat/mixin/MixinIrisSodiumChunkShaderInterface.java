package qouteall.imm_ptl.core.compat.mixin;

import me.jellysquid.mods.sodium.client.gl.GlObject;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.irisshaders.iris.compat.sodium.impl.shader_overrides.IrisChunkShaderInterface;
import net.irisshaders.iris.compat.sodium.impl.shader_overrides.ShaderBindingContextExt;
import net.irisshaders.iris.gl.blending.BlendModeOverride;
import net.irisshaders.iris.pipeline.SodiumTerrainPipeline;
import net.irisshaders.iris.uniforms.custom.CustomUniforms;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL21;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.render.FrontClipping;
import qouteall.q_misc_util.Helper;

import java.util.List;

@Pseudo
@Mixin(value = IrisChunkShaderInterface.class, remap = false)
public class MixinIrisSodiumChunkShaderInterface {
    private int uIPClippingEquation;

    public MixinIrisSodiumChunkShaderInterface() {
    }

    private void ip_init(int shaderId) {
        this.uIPClippingEquation = GL20C.glGetUniformLocation(shaderId, "imm_ptl_ClippingEquation");
        if (this.uIPClippingEquation < 0) {
            Helper.err("uniform imm_ptl_ClippingEquation not found in transformed iris shader");
            this.uIPClippingEquation = -1;
        }

    }

    @Inject(
            method = {"<init>"},
            at = {@At("RETURN")},
            require = 0
    )
    private void onInit(int handle, ShaderBindingContextExt contextExt, SodiumTerrainPipeline pipeline, ChunkShaderOptions options, boolean isTess, boolean isShadowPass, BlendModeOverride blendModeOverride, List bufferOverrides, float alpha, CustomUniforms customUniforms, CallbackInfo ci) {
        this.ip_init(handle);
    }

    @Inject(
            method = {"setupState"},
            at = {@At("RETURN")}
    )
    private void onSetup(CallbackInfo ci) {
        if (this.uIPClippingEquation != -1) {
            if (FrontClipping.isClippingEnabled) {
                double[] equation = FrontClipping.getActiveClipPlaneEquationForEntities();
                GL21.glUniform4f(this.uIPClippingEquation, (float)equation[0], (float)equation[1], (float)equation[2], (float)equation[3]);
            } else {
                GL21.glUniform4f(this.uIPClippingEquation, 0.0F, 0.0F, 0.0F, 1.0F);
            }
        }

    }
}
