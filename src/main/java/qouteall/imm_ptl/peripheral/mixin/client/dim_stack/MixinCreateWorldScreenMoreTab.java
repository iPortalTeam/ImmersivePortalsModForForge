package qouteall.imm_ptl.peripheral.mixin.client.dim_stack;

import net.minecraft.client.gui.components.Button;
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

@Mixin(CreateWorldScreen.MoreTab.class)
public class MixinCreateWorldScreenMoreTab {
    // the implicit parent object reference
    @Final
    @Shadow
    CreateWorldScreen this$0;

    @Inject(
        method = "<init>",
        at = @At("RETURN"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onInitEnd(
        CreateWorldScreen createWorldScreen, CallbackInfo ci,
        GridLayout.RowHelper rowHelper
    ) {
        rowHelper.addChild(
            Button.builder(
                Component.translatable("imm_ptl.altius_screen_button"),
                button -> ((IECreateWorldScreen) this$0).ip_openDimStackScreen()
            ).width(210).build()
        );
    }
}
