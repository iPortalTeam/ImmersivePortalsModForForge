package qouteall.imm_ptl.core.mixin.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface MixinEntityAccess {

    @Invoker("setLevel")
    void immersive_portals$callSetLevel(Level level);
}
