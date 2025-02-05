package qouteall.imm_ptl.core.portal.nether_portal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.PortalPlaceholderBlock;

public class GeneralBreakablePortal extends BreakablePortalEntity {
    
    public static EntityType<GeneralBreakablePortal> entityType = IPRegistry.GENERAL_BREAKABLE_PORTAL.get();
    
    public GeneralBreakablePortal(
        EntityType<?> entityType_1,
        Level world_1
    ) {
        super(entityType_1, world_1);
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
