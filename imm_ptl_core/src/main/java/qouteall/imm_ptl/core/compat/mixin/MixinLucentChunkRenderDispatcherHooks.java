package qouteall.imm_ptl.core.compat.mixin;

import com.legacy.lucent.core.asm_hooks.ChunkRenderDispatcherHooks;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import qouteall.imm_ptl.core.ClientWorldLoader;
import qouteall.imm_ptl.core.render.context_management.RenderStates;

@Mixin(value = ChunkRenderDispatcherHooks.class, remap = false)
public class MixinLucentChunkRenderDispatcherHooks {

    // Set the correct levelRenderer dirty
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/legacy/lucent/core/LucentClient;setDirty(Lnet/minecraft/core/SectionPos;)V"))
    private static void setDirty(SectionPos section) {
        ClientWorldLoader.getWorldRenderer(RenderStates.originalPlayerDimension).setSectionDirty(section.getX(), section.getY(), section.getZ());
    }
}
