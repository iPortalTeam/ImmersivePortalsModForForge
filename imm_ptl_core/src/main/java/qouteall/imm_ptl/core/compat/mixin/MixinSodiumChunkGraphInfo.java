package qouteall.imm_ptl.core.compat.mixin;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.imm_ptl.core.compat.sodium_compatibility.SodiumInterface;

@Mixin(value = OcclusionCuller.class, remap = false)
public abstract class MixinSodiumChunkGraphInfo {
    
    // do portal frustum culling
    @Inject(
        method = "isOutsideFrustum",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void onIsCulledByFrustum(Viewport viewport, RenderSection section, CallbackInfoReturnable<Boolean> cir) {
        if (SodiumInterface.frustumCuller != null) {
            double x = section.getOriginX();
            double y = section.getOriginY();
            double z = section.getOriginZ();
            
            boolean invisible = SodiumInterface.frustumCuller.canDetermineInvisibleWithWorldCoord(
                x, y, z, x + 16.0F, y + 16.0F, z + 16.0F
            );
            if (invisible) {
                cir.setReturnValue(true);
            }
        }
    }
}
