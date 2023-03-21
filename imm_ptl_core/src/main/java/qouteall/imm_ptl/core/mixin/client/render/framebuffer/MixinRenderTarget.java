package qouteall.imm_ptl.core.mixin.client.render.framebuffer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL30C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import qouteall.imm_ptl.core.CHelper;
import qouteall.imm_ptl.core.IPCGlobal;
import qouteall.imm_ptl.core.ducks.IEFrameBuffer;

import javax.annotation.Nullable;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL30.GL_DEPTH24_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_DEPTH32F_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_FLOAT_32_UNSIGNED_INT_24_8_REV;

@Mixin(RenderTarget.class)
public abstract class MixinRenderTarget implements IEFrameBuffer {
    
    private boolean isStencilBufferEnabled;
    
    @Shadow
    public int width;
    @Shadow
    public int height;
    
    
    @Shadow
    public abstract void resize(int width, int height, boolean clearError);
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(
        boolean useDepth,
        CallbackInfo ci
    ) {
        isStencilBufferEnabled = false;
    }
    
//    @ModifyArgs(
//        method = "createBuffers",
//        at = @At(
//            value = "INVOKE",
//            target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"
//        )
//    )
//    private void modifyTexImage2D(Args args) {
//        if (Objects.equals(args.get(2), GL_DEPTH_COMPONENT)) {
//            if (isStencilBufferEnabled) {
//                args.set(2, IPCGlobal.useSeparatedStencilFormat ? GL_DEPTH32F_STENCIL8 : GL_DEPTH24_STENCIL8);
//                args.set(6, ARBFramebufferObject.GL_DEPTH_STENCIL);
//                args.set(7, IPCGlobal.useSeparatedStencilFormat ? GL_FLOAT_32_UNSIGNED_INT_24_8_REV : GL30.GL_UNSIGNED_INT_24_8);
//            }
//        }
//    }

    @Redirect( // @Nick1st: Redirect for now, I don't think there's a better way to do this currently.
            // Well, maybe there is. Directly injecting into the called method, doing a lookup for the caller (which is a very heavy method)
            // and then altering the method to manipulate the parameters. This would lead to better compat than a redirect, but as I said, it's very heavy.
            // I can think of a few other potential ways also, but they are very complicated. How does @ModifyArgs work?
            // TODO Find out why @ModifyArgs and if there's something different that is suitable.
            method = "createBuffers",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"
            )
    )
    private void modifyTexImage2D(int pTarget, int pLevel, int pInternalFormat, int pWidth, int pHeight, int pBorder, int pFormat, int pType, IntBuffer pPixels) {
        if (Objects.equals(pInternalFormat, GL_DEPTH_COMPONENT)) {
            if (isStencilBufferEnabled) {
                pInternalFormat = IPCGlobal.useSeparatedStencilFormat ? GL_DEPTH32F_STENCIL8 : GL_DEPTH24_STENCIL8;
                pFormat =  ARBFramebufferObject.GL_DEPTH_STENCIL;
                pType = IPCGlobal.useSeparatedStencilFormat ? GL_FLOAT_32_UNSIGNED_INT_24_8_REV : GL30.GL_UNSIGNED_INT_24_8;
            }
        }
        GlStateManager._texImage2D(pTarget, pLevel, pInternalFormat, pWidth, pHeight, pBorder, pFormat, pType, pPixels);
    }

//    @Redirect(
//        method = "Lcom/mojang/blaze3d/pipeline/RenderTarget;createBuffers(IIZ)V",
//        at = @At(
//            value = "INVOKE",
//            target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V",
//            remap = false
//        )
//    )
//    private void redirectTexImage2d(
//        int target, int level, int internalFormat,
//        int width, int height,
//        int border, int format, int type,
//        IntBuffer pixels
//    ) {
//        if (internalFormat == GL_DEPTH_COMPONENT && isStencilBufferEnabled) {
//            GlStateManager._texImage2D(
//                target,
//                level,
//                IPCGlobal.useAnotherStencilFormat ? GL_DEPTH32F_STENCIL8 : GL_DEPTH24_STENCIL8,
//                width,
//                height,
//                border,
//                ARBFramebufferObject.GL_DEPTH_STENCIL,
//                IPCGlobal.useAnotherStencilFormat ? GL_FLOAT_32_UNSIGNED_INT_24_8_REV : GL30.GL_UNSIGNED_INT_24_8,
//                pixels
//            );
//        }
//        else {
//            GlStateManager._texImage2D(
//                target, level, internalFormat, width, height,
//                border, format, type, pixels
//            );
//        }
//    }
    
    @ModifyArg( // @Nick1st: Originally @ModifyArgs
        method = "createBuffers",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glFramebufferTexture2D(IIIII)V"
        ),
        index = 1
    )
    private int modifyFrameBufferTexture2D(int pAttachment) {
        if (Objects.equals(pAttachment, GL30C.GL_DEPTH_ATTACHMENT)) {
            if (isStencilBufferEnabled) {
                pAttachment = GL30.GL_DEPTH_STENCIL_ATTACHMENT;
            }
        }
        return pAttachment;
    }

//    @Redirect(
//        method = "Lcom/mojang/blaze3d/pipeline/RenderTarget;createBuffers(IIZ)V",
//        at = @At(
//            value = "INVOKE",
//            target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glFramebufferTexture2D(IIIII)V",
//            remap = false
//        )
//    )
//    private void redirectFrameBufferTexture2d(
//        int target, int attachment, int textureTarget, int texture, int level
//    ) {
//
//        if (attachment == GL30C.GL_DEPTH_ATTACHMENT && isStencilBufferEnabled) {
//            GlStateManager._glFramebufferTexture2D(
//                target, GL30.GL_DEPTH_STENCIL_ATTACHMENT, textureTarget, texture, level
//            );
//        }
//        else {
//            GlStateManager._glFramebufferTexture2D(target, attachment, textureTarget, texture, level);
//        }
//    }
    
    @Inject(
        method = "Lcom/mojang/blaze3d/pipeline/RenderTarget;copyDepthFrom(Lcom/mojang/blaze3d/pipeline/RenderTarget;)V",
        at = @At("RETURN")
    )
    private void onCopiedDepthFrom(RenderTarget framebuffer, CallbackInfo ci) {
        CHelper.checkGlError();
    }
    
    @Override
    public boolean getIsStencilBufferEnabled() {
        return isStencilBufferEnabled;
    }
    
    @Override
    public void setIsStencilBufferEnabledAndReload(boolean cond) {
        if (isStencilBufferEnabled != cond) {
            isStencilBufferEnabled = cond;
            resize(width, height, Minecraft.ON_OSX);
        }
    }
}
