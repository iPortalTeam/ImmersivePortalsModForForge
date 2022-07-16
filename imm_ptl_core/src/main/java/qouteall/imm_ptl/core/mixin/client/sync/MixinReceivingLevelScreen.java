package qouteall.imm_ptl.core.mixin.client.sync;

import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ReceivingLevelScreen.class)
public class MixinReceivingLevelScreen {
//    @Redirect(
//        method = "tick",
//        at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/client/renderer/LevelRenderer;isChunkCompiled(Lnet/minecraft/core/BlockPos;)Z"
//        )
//    )
//    private boolean redirectIsChunkCompiled(LevelRenderer instance, BlockPos blockPos) {
//        Minecraft client = Minecraft.getInstance();
//        ClientLevel world = client.level;
//
//        ClientChunkCache chunkSource = world.getChunkSource();
//
//        if (chunkSource instanceof MyClientChunkManager myClientChunkManager) {
//            ChunkPos chunkPos = new ChunkPos(blockPos);
//
//            return myClientChunkManager.isChunkLoaded(chunkPos.x, chunkPos.z);
//        }
//        else {
//            return instance.isChunkCompiled(blockPos);
//        }
//    }
}
