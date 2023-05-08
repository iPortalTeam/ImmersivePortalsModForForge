package qouteall.imm_ptl.core.compat.mixin;

import com.legacy.lucent.core.LucentClient;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.ClientWorldLoader;
import qouteall.imm_ptl.core.render.context_management.RenderStates;

// TODO @Nick1st Remove: It is not possible to Mixin into this class. It gets loaded before we are ready.
@Mixin(value = LucentClient.class, remap = false)
public class MixinLucentClient {

    // Set the player level renderer dirty instead of a random one
    @Inject(method = "setDirty", at = @At("HEAD"), cancellable = true)
    private static void setDirtyPatched(SectionPos section, CallbackInfo ci) {
        ClientWorldLoader.getWorldRenderer(RenderStates.originalPlayerDimension).setSectionDirty(section.getX(), section.getY(), section.getZ());
        ci.cancel();
    }
}
