package qouteall.imm_ptl.core.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import qouteall.imm_ptl.core.IPCGlobal;
import qouteall.imm_ptl.core.portal.Portal;

@OnlyIn(Dist.CLIENT)
public class PortalEntityRenderer extends EntityRenderer<Portal> {
    
    public PortalEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(
        Portal portal,
        float yaw,
        float tickDelta,
        PoseStack matrixStack,
        MultiBufferSource vertexConsumerProvider,
        int light
    ) {
        
        IPCGlobal.renderer.renderPortalInEntityRenderer(portal);
        
        if (OverlayRendering.shouldRenderOverlay(portal)) {
            OverlayRendering.onRenderPortalEntity(portal, matrixStack, vertexConsumerProvider);
        }
        
        
        super.render(portal, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }
    
    @Override
    public ResourceLocation getTextureLocation(Portal portal) {
//        if (portal instanceof BreakablePortalEntity) {
//            if (((BreakablePortalEntity) portal).overlayBlockState != null) {
//                return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
//            }
//        }
        return null;
    }
    
}
