package qouteall.imm_ptl.core.mixin.common.collision;

import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;

@Mixin(Snowball.class)
public abstract class MixinSnowball extends MixinEntity {
    
    @Inject(method = "Lnet/minecraft/world/entity/projectile/Snowball;onHit(Lnet/minecraft/world/phys/HitResult;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void onHit(HitResult hitResult, CallbackInfo ci) {
        if (hitResult instanceof BlockHitResult) {
            Block hittingBlock = ip_getLevel().getBlockState(((BlockHitResult) hitResult).getBlockPos()).getBlock();
            if (hitResult.getType() == HitResult.Type.BLOCK &&
                hittingBlock == IPRegistry.NETHER_PORTAL_BLOCK.get()
            ) {
                ci.cancel();
            }
        }
    }
}
