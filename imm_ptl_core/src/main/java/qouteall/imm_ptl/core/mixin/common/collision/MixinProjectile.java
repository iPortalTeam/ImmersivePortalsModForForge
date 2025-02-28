package qouteall.imm_ptl.core.mixin.common.collision;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;

@Mixin(Projectile.class)
public abstract class MixinProjectile extends MixinEntity {

    // make it recognize the owner in another dimension
    @Redirect(
        method = "getOwner",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;getEntity(Ljava/util/UUID;)Lnet/minecraft/world/entity/Entity;"
        )
    )
    private Entity redirectGetEntityFromUuid(
        net.minecraft.server.level.ServerLevel serverLevel,
        java.util.UUID uuid
    ) {
        MinecraftServer server = serverLevel.getServer();
        for (ServerLevel world : server.getAllLevels()) {
            Entity entity = world.getEntity(uuid);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }
    
    @Inject(method = "Lnet/minecraft/world/entity/projectile/Projectile;onHit(Lnet/minecraft/world/phys/HitResult;)V", at = @At(value = "HEAD"), cancellable = true)
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
