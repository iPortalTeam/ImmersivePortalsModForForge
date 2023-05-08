package qouteall.imm_ptl.core.compat;

import com.legacy.lucent.api.plugin.ILucentPlugin;
import com.legacy.lucent.api.plugin.LucentPlugin;
import com.legacy.lucent.api.registry.EntityLightSourcePosRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.compat.mixin.LucentThreadAccessor;
import qouteall.imm_ptl.core.platform_specific.IPModEntry;
import qouteall.imm_ptl.core.render.context_management.RenderStates;

@LucentPlugin
public class LucentCompat implements ILucentPlugin {

    public static boolean canAccessClientOnlyThread() {
        return Thread.currentThread() == LucentThreadAccessor.getLucentThread();
    }

    @Override
    public String ownerModID() {
        return IPModEntry.MODID;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void registerEntityLightSourcePositionGetter(EntityLightSourcePosRegistry registry) {
        registry.register(EntityType.PLAYER, player -> new Vec3(RenderStates.originalPlayerPos.x(),
                RenderStates.originalPlayerBoundingBox.getYsize() / 2.0 + RenderStates.originalPlayerPos.y(),
                RenderStates.originalPlayerPos.z()));
    }
}
