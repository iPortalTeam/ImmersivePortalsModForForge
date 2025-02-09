package qouteall.imm_ptl.core.compat.mixin;

import com.mojang.blaze3d.shaders.Program;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.irisshaders.iris.pipeline.SodiumTerrainPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.render.ShaderCodeTransformation;
import qouteall.q_misc_util.Helper;

import java.util.Optional;

@Pseudo
@Mixin(value = SodiumTerrainPipeline.class, remap = false)
public class MixinIrisSodiumSodiumTerrainPipeline {
    @Shadow
    Optional<String> terrainSolidVertex;
    @Shadow
    Optional<String> terrainCutoutVertex;
    @Shadow
    Optional<String> translucentVertex;
    @Unique
    private boolean immptlPatched = false;

    public MixinIrisSodiumSodiumTerrainPipeline() {
    }

    @Inject(
            method = {"patchShaders"},
            at = {@At("RETURN")}
    )
    private void onPatchShaderEnds(ChunkVertexType chunkVertexType, CallbackInfo ci) {
        if (!this.immptlPatched) {
            this.immptlPatched = true;
            this.terrainSolidVertex = this.terrainSolidVertex.map((code) -> {
                return ShaderCodeTransformation.transform(Program.Type.VERTEX, "iris_sodium_terrain_vertex", code);
            });
            this.terrainCutoutVertex = this.terrainCutoutVertex.map((code) -> {
                return ShaderCodeTransformation.transform(Program.Type.VERTEX, "iris_sodium_terrain_vertex", code);
            });
            this.translucentVertex = this.translucentVertex.map((code) -> {
                return ShaderCodeTransformation.transform(Program.Type.VERTEX, "iris_sodium_terrain_vertex", code);
            });
        } else {
            Helper.err("iris terrain shader ImmPtl patched twice");
        }

    }
}
