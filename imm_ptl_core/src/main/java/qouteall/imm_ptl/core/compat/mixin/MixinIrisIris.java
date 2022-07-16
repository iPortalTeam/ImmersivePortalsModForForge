package qouteall.imm_ptl.core.compat.mixin;

//DISABLED_COMPILEimport net.coderbot.iris.Iris;
//DISABLED_COMPILEimport net.coderbot.iris.shaderpack.DimensionId;
//DISABLED_COMPILEimport net.fabricmc.loader.api.FabricLoader;

//DISABLED_COMPILE@Mixin(value = Iris.class, remap = false)
public class MixinIrisIris {
    // test
    // only overworld
//DISABLED_COMPILE    @Inject(
//DISABLED_COMPILE        method = "getCurrentDimension", at = @At("HEAD"), cancellable = true
//DISABLED_COMPILE    )
//DISABLED_COMPILE    private static void onGetCurrentDimension(CallbackInfoReturnable<DimensionId> cir) {
//DISABLED_COMPILE        if (IPCGlobal.renderer instanceof ExperimentalIrisPortalRenderer) {
//DISABLED_COMPILE            cir.setReturnValue(DimensionId.OVERWORLD);
//DISABLED_COMPILE        }
//DISABLED_COMPILE    }
    
//    // it cannot recognize sodium from jitpack
//    @Inject(
//        method = "isSodiumInvalid", at = @At("HEAD"), cancellable = true
//    )
//    private static void onIsSodiumInvalid(CallbackInfoReturnable<Boolean> cir) {
//        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
//            cir.setReturnValue(false);
//        }
//    }
}
