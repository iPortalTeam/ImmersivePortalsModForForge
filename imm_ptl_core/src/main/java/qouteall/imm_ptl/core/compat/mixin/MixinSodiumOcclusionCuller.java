package qouteall.imm_ptl.core.compat.mixin;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.imm_ptl.core.CHelper;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalLike;
import qouteall.imm_ptl.core.render.context_management.PortalRendering;

import java.util.function.Consumer;

@Mixin({OcclusionCuller.class})
public abstract class MixinSodiumOcclusionCuller {
    @Unique
    private @Nullable SectionPos ip_modifiedStartPoint;
    @Unique
    private static boolean ip_tolerantInitialFrustumTestFail;

    public MixinSodiumOcclusionCuller() {
    }

    @Shadow(
            remap = false
    )
    protected abstract RenderSection getRenderSection(int var1, int var2, int var3);

    @Shadow(
            remap = false
    )
    public static boolean isWithinFrustum(Viewport viewport, RenderSection section) {
        throw new RuntimeException();
    }

    @ModifyVariable(
            method = {"findVisible"},
            at = @At("HEAD"),
            argsOnly = true,
            remap = false
    )
    boolean modifyUseOcclusionCulling(boolean originalValue, @Coerce Object vistor, Viewport viewport, float searchDistance, boolean useOcclusionCulling, int frame) {
        boolean doUseOcclusionCulling = PortalRendering.shouldEnableSodiumCaveCulling();
        this.ip_modifiedStartPoint = null;
        ip_tolerantInitialFrustumTestFail = false;
        if (PortalRendering.isRendering()) {
            PortalLike renderingPortal = PortalRendering.getRenderingPortal();
            if (renderingPortal instanceof Portal) {
                Portal portal = (Portal)renderingPortal;
                Vec3 cameraPos = CHelper.getCurrentCameraPos();
                this.ip_modifiedStartPoint = /*portal.getPortalShape().getModifiedVisibleSectionIterationOrigin(portal, cameraPos);*/ null;
                if (this.ip_modifiedStartPoint != null) {
                    doUseOcclusionCulling = false;
                    RenderSection renderSection = this.getRenderSection(this.ip_modifiedStartPoint.x(), this.ip_modifiedStartPoint.y(), this.ip_modifiedStartPoint.z());
                    if (renderSection != null && !isWithinFrustum(viewport, renderSection)) {
                        ip_tolerantInitialFrustumTestFail = true;
                    }
                }
            }
        }

        return doUseOcclusionCulling;
    }

    @Redirect(
            method = {"init"},
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/viewport/Viewport;getChunkCoord()Lnet/minecraft/core/SectionPos;",
                    remap = true
            ),
            remap = false
    )
    private SectionPos redirectGetChunkCoordInInit(Viewport instance) {
        return this.ip_modifiedStartPoint != null ? this.ip_modifiedStartPoint : instance.getChunkCoord();
    }

    @Redirect(
            method = {"initWithinWorld"},
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/viewport/Viewport;getChunkCoord()Lnet/minecraft/core/SectionPos;"
            ),
            remap = false
    )
    private SectionPos redirectGetChunkCoordInInitWithinWorld(Viewport instance) {
        return this.ip_modifiedStartPoint != null ? this.ip_modifiedStartPoint : instance.getChunkCoord();
    }

    @Inject(
            method = {"isWithinFrustum"},
            at = {@At("RETURN")},
            cancellable = true,
            remap = false
    )
    private static void onIsOutsideFrustum(Viewport viewport, RenderSection section, CallbackInfoReturnable<Boolean> cir) {
        if (ip_tolerantInitialFrustumTestFail) {
            boolean withinFrustum = cir.getReturnValueZ();
            if (withinFrustum) {
                ip_tolerantInitialFrustumTestFail = false;
            }

            cir.setReturnValue(true);
        }

    }
}