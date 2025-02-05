package qouteall.imm_ptl.peripheral.mixin.client.dim_stack;

import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import qouteall.imm_ptl.peripheral.ducks.IECreateWorldScreen;

@Mixin(targets = "net/minecraft/client/gui/screens/worldselection/CreateWorldScreen$MoreTab")
public abstract class MixinMoreTabCreateWorldScreen extends GridLayoutTab {
    @Final
    @Shadow
    CreateWorldScreen this$0;

    public MixinMoreTabCreateWorldScreen(Component pTitle) {
        super(pTitle);
    }

    @Inject(method = "<init>", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void post_init(CreateWorldScreen p_268071_, CallbackInfo ci, GridLayout.RowHelper rowHelper){
        rowHelper.addChild(((IECreateWorldScreen)this$0).immersive_portals$getDimStackButton());
    }
}
