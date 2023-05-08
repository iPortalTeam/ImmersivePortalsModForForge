package qouteall.imm_ptl.core.compat.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.imm_ptl.core.render.context_management.RenderStates;

@OnlyIn(Dist.CLIENT)
@Mixin(value = LevelRenderer.class, priority = 900)
public class MixinLucentLevelRenderer {

    // Inject before Lucent and return if the level doesn't fit.
    @Inject(at = @At(value = "RETURN", ordinal = 1), method = "getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I", cancellable = true)
    private static void blockLightDimensionCheck(BlockAndTintGetter level, BlockState state, BlockPos pos, CallbackInfoReturnable<Integer> cir)
    {
        ClientLevel clientLevel = null;
        if (level instanceof ClientLevel) {
            clientLevel = (ClientLevel) level;
        } else if (level instanceof RenderChunkRegion renderChunkRegion) {
            clientLevel = (ClientLevel) renderChunkRegion.level;
        }

        if (clientLevel == null || clientLevel.dimension() != RenderStates.originalPlayerDimension) {
            cir.cancel();
        }
    }
}
