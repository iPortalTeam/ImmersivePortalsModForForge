package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILEimport me.jellysquid.mods.sodium.client.render.chunk.graph.ChunkGraphInfo;
//DISABLED_COMPILEimport me.jellysquid.mods.sodium.client.util.frustum.Frustum;

//DISABLED_COMPILE@Mixin(value = ChunkGraphInfo.class, remap = false)
public abstract class MixinSodiumChunkGraphInfo {
    //DISABLED_COMPILE    @Shadow
    public abstract int getOriginX();

    //DISABLED_COMPILE    @Shadow
    public abstract int getOriginY();

    //DISABLED_COMPILE    @Shadow
    public abstract int getOriginZ();
    
    // do portal frustum culling
//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "isCulledByFrustum",
//DISABLED_COMPILE        at = @At("HEAD"),
//DISABLED_COMPILE        cancellable = true
//DISABLED_COMPILE    )
//DISABLED_COMPILE    private void onIsCulledByFrustum(Frustum frustum, CallbackInfoReturnable<Boolean> cir) {
//DISABLED_COMPILE        if (SodiumInterface.frustumCuller != null) {
//DISABLED_COMPILE            double x = this.getOriginX();
//DISABLED_COMPILE            double y = this.getOriginY();
//DISABLED_COMPILE            double z = this.getOriginZ();

//DISABLED_COMPILE            boolean invisible = SodiumInterface.frustumCuller.canDetermineInvisibleWithWorldCoord(
//DISABLED_COMPILE                x, y, z, x + 16.0F, y + 16.0F, z + 16.0F
//DISABLED_COMPILE            );
//DISABLED_COMPILE            if (invisible) {
//DISABLED_COMPILE                cir.setReturnValue(true);
//DISABLED_COMPILE            }
//DISABLED_COMPILE        }
//DISABLED_COMPILE    }
}
