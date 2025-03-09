package qouteall.imm_ptl.core.platform_specific.mixin.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.portal.custom_portal_gen.CustomPortalGenManagement;

@Mixin(ServerPlayer.class)
public class MixinServerPlayerEntity_MA {
    @Inject(method = "changeDimension", at = @At("HEAD"), remap = false)
    private void onChangeDimensionByVanilla(
            ServerLevel serverWorld, ITeleporter teleporter, CallbackInfoReturnable<Entity> cir
    ) {
        ServerPlayer this_ = (ServerPlayer) (Object) this;
        onBeforeDimensionTravel(this_);
    }
    
    // update chunk visibility data
    @Inject(method = "Lnet/minecraft/server/level/ServerPlayer;teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At("HEAD"))
    private void onTeleported(
        ServerLevel targetWorld,
        double x,
        double y,
        double z,
        float yaw,
        float pitch,
        CallbackInfo ci
    ) {
        ServerPlayer this_ = (ServerPlayer) (Object) this;
        
        if (this_.level() != targetWorld) {
            onBeforeDimensionTravel(this_);
        }
    }
    
    private static void onBeforeDimensionTravel(ServerPlayer player) {
        CustomPortalGenManagement.onBeforeConventionalDimensionChange(player);
        IPGlobal.chunkDataSyncManager.removePlayerFromChunkTrackersAndEntityTrackers(player);
        
        IPGlobal.serverTaskList.addTask(() -> {
            CustomPortalGenManagement.onAfterConventionalDimensionChange(player);
            return true;
        });
    }
}
