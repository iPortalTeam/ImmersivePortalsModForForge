package qouteall.imm_ptl.core.compat.mixin;

import com.mojang.blaze3d.shaders.Program;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.imm_ptl.core.render.ShaderCodeTransformation;

import java.util.Optional;

@Pseudo
//DISABLED_COMPILE@Mixin(value = SodiumTerrainPipeline.class, remap = false)
public class MixinIrisSodiumSodiumTerrainPipeline {
    //DISABLED_COMPILE    @Inject(method = "getTerrainVertexShaderSource", at = @At("RETURN"), cancellable = true)
    private void onGetTerrainVertexShaderSource(CallbackInfoReturnable<Optional<String>> cir) {
        Optional<String> original = cir.getReturnValue();
        cir.setReturnValue(original.map(code ->
            ShaderCodeTransformation.transform(
                Program.Type.VERTEX,
                "iris_sodium_terrain_vertex",
                code
            )
        ));
    }

    //DISABLED_COMPILE    @Inject(method = "getTranslucentVertexShaderSource", at = @At("RETURN"), cancellable = true)
    private void onGetTranslucentVertexShaderSource(CallbackInfoReturnable<Optional<String>> cir) {
        Optional<String> original = cir.getReturnValue();
        cir.setReturnValue(original.map(code ->
            ShaderCodeTransformation.transform(
                Program.Type.VERTEX,
                "iris_sodium_translucent_vertex",
                code
            )
        ));
    }
}
