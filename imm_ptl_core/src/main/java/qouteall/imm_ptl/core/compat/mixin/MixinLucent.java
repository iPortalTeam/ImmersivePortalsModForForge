package qouteall.imm_ptl.core.compat.mixin;

import com.legacy.lucent.api.LucentData;
import com.legacy.lucent.core.dynamic_lighting.DynamicLightingEngine;
import com.legacy.lucent.core.dynamic_lighting.LightData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.imm_ptl.core.ClientWorldLoader;
import qouteall.imm_ptl.core.render.context_management.RenderStates;

import java.util.List;
import java.util.Map;

// TODO @Nick1st Optimize this, by reducing the mixins if possible, as they add some overhead.
@Mixin(value = DynamicLightingEngine.class, remap = false)
public abstract class MixinLucent {

    @Shadow
    private static VoxelShape getShape(BlockState state, BlockPos pos, Direction dir) {
        return null;
    }


    @Shadow private static Map<BlockPos, LightData> lightData;

    // Make sure the getShape Helper method gets the right level
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private static void patchedGetShape(BlockState state, BlockPos pos, Direction dir, CallbackInfoReturnable<VoxelShape> cir) {
        ClientLevel level = ClientWorldLoader.getWorldAsync(RenderStates.originalPlayerDimension);
        VoxelShape shape = (state.getLightBlock(level, pos) < 15 && !state.canOcclude())
                || !state.getMaterial().isSolid() ? Shapes.empty() : state.getFaceOcclusionShape(level, pos, dir);
        cir.setReturnValue(shape);
        cir.cancel();
    }

    // Make sure the can lightPass occlusion test runs in the right level
    @Inject(method = "canLightPass", at = @At("HEAD"), cancellable = true)
    private static void patchedCanLightPass(BlockPos currentPos, BlockPos relativePos, Direction dir, CallbackInfoReturnable<Boolean> cir) {
        ClientLevel level = ClientWorldLoader.getWorldAsync(RenderStates.originalPlayerDimension);
        boolean canLightPass = !Shapes.faceShapeOccludes(getShape(level.getBlockState(currentPos), currentPos, dir), getShape(level.getBlockState(relativePos), relativePos, dir.getOpposite()));
        cir.setReturnValue(canLightPass);
        cir.cancel();
    }

    // Make sure Lucent gets the entities from the right level
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), remap = true)
    private static List<Entity> getAllEntitiesFromTheRightDimension(ClientLevel instance, Class<Entity> entityClass, AABB aabb) {
        aabb = RenderStates.originalPlayerBoundingBox.inflate(LucentData.maxVisibleDistance);
        List<Entity> allEntities = ClientWorldLoader.getWorldAsync(RenderStates.originalPlayerDimension).getEntitiesOfClass(entityClass, aabb);
        if (!allEntities.contains(Minecraft.getInstance().player)) {
            allEntities.add(Minecraft.getInstance().player);
        }
        return allEntities;
    }

    //Give Lucent the correct player position
    @ModifyArg(method = "getEntityLightLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ClipContext;<init>(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/ClipContext$Block;Lnet/minecraft/world/level/ClipContext$Fluid;Lnet/minecraft/world/entity/Entity;)V"), index = 0)
    private static Vec3 truePlayerEyePosition(Vec3 pFrom) {
        return new Vec3(RenderStates.originalPlayerPos.x, RenderStates.originalPlayerPos.y + Minecraft.getInstance().player.getEyeY(), RenderStates.originalPlayerPos.z); // TODO @Nick1st Make sure I can get the EyeHeight like this
    }

    // Give Lucent the correct level to clip
    @Redirect(method = "getEntityLightLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;clip(Lnet/minecraft/world/level/ClipContext;)Lnet/minecraft/world/phys/BlockHitResult;"), remap = true)
    private static BlockHitResult clipWithRightPosition(Level instance, ClipContext clipContext) {
        return ClientWorldLoader.getWorldAsync(RenderStates.originalPlayerDimension).clip(clipContext);
    }

    // Calculate the distance correctly.
    @Redirect(method = "getEntityLightLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;distanceTo(Lnet/minecraft/world/entity/Entity;)F"), remap = true)
    private static float correctAlwaysVisibleDistanceCheck(LocalPlayer instance, Entity entity) {
        Vec3 entityPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        if (entity instanceof LocalPlayer) {
            entityPos = RenderStates.originalPlayerPos;
        }
        return (float) RenderStates.originalPlayerPos.distanceTo(entityPos);
    }

    // Calculate the correct Skylight value
    @Redirect(method = "getEntityLightLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBrightness(Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/BlockPos;)I"), remap = true)
    private static int correctSkyLight(ClientLevel instance, LightLayer lightLayer, BlockPos blockPos) {
        return ClientWorldLoader.getWorldAsync(RenderStates.originalPlayerDimension).getBrightness(lightLayer, blockPos);
    }

    // Set the player level renderer dirty instead of a random one
    @Inject(method = "setDirty", at = @At(value = "INVOKE", target = "Lcom/legacy/lucent/core/LucentClient;setDirty(Lnet/minecraft/core/SectionPos;)V"), cancellable = true)
    private static void setDirtyPatched(SectionPos section, CallbackInfo ci) {
        ClientWorldLoader.getWorldRenderer(RenderStates.originalPlayerDimension).setSectionDirty(section.getX(), section.getY(), section.getZ());
        ci.cancel();
    }

    // Fixes the level used to query the skylight
    @ModifyVariable(method = "calcLight", at = @At(value = "HEAD"), argsOnly = true)
    private static BlockAndTintGetter getCorrectSkylightLevel(BlockAndTintGetter level) {
        return ClientWorldLoader.getWorldAsync(RenderStates.originalPlayerDimension);
    }

    // Fix a race condition if the Renderstate originalPlayerDimension is not yet set.
    @Inject(method = "blockChanged", at = @At("HEAD"), cancellable = true)
    private static void injectNullCheck(BlockPos pos, CallbackInfo ci) {
        if (RenderStates.originalPlayerDimension == null) {
            ci.cancel();
        }
    }

}
