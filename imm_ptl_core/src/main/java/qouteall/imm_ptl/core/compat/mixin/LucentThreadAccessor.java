package qouteall.imm_ptl.core.compat.mixin;

import com.legacy.lucent.core.dynamic_lighting.DynamicLightingEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DynamicLightingEngine.class, remap = false)
public interface LucentThreadAccessor {

    @Accessor(value = "INSTANCE")
    static Thread getLucentThread() {
        throw new AssertionError();
    }
}
