package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILEimport me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
//DISABLED_COMPILEimport me.jellysquid.mods.sodium.client.util.frustum.Frustum;

//DISABLED_COMPILE@Mixin(value = SodiumWorldRenderer.class, remap = false)
public class MixinSodiumWorldRenderer {
//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "updateChunks",
//DISABLED_COMPILE        at = @At("HEAD")
//DISABLED_COMPILE    )
//DISABLED_COMPILE    private void onUpdateChunks(Camera camera, Frustum frustum, int frame, boolean spectator, CallbackInfo ci) {
//DISABLED_COMPILE        SodiumInterface.frustumCuller = new FrustumCuller();
//DISABLED_COMPILE        Vec3 cameraPos = camera.getPosition();
//DISABLED_COMPILE        SodiumInterface.frustumCuller.update(cameraPos.x, cameraPos.y, cameraPos.z);
//DISABLED_COMPILE    }
}
