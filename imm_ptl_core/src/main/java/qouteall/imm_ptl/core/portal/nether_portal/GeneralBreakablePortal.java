package qouteall.imm_ptl.core.portal.nether_portal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.PortalPlaceholderBlock;

public class GeneralBreakablePortal extends BreakablePortalEntity {

    public GeneralBreakablePortal(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }
    
    @Override
    protected boolean isPortalIntactOnThisSide() {
        boolean areaIntact = blockPortalShape.area.stream()
            .allMatch(blockPos ->
                level().getBlockState(blockPos).getBlock() == IPRegistry.NETHER_PORTAL_BLOCK.get()
            );
        boolean frameIntact = blockPortalShape.frameAreaWithoutCorner.stream()
            .allMatch(blockPos -> !level().isEmptyBlock(blockPos));
        return areaIntact && frameIntact;
    }
    
    @Override
    protected void addSoundAndParticle() {
    
    }
}
