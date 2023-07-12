package qouteall.imm_ptl.core.platform_specific.mixin.common;

import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import qouteall.imm_ptl.core.platform_specific.IsVanilla;

@Mixin(PlayerEvent.PlayerChangedDimensionEvent.class)
public class MixinPlayerChangedDimensionEvent implements IsVanilla {

    @Unique
    boolean immptl_vanilla = true;

    @Override
    @Unique
    public boolean isImmptl_vanilla() {
        return immptl_vanilla;
    }

    @Override
    @Unique
    public void setNotVanilla() {
        immptl_vanilla = false;
    }
}
