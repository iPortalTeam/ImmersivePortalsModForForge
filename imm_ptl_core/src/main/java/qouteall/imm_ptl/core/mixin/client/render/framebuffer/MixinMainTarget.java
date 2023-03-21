package qouteall.imm_ptl.core.mixin.client.render.framebuffer;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL30C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import qouteall.imm_ptl.core.IPCGlobal;
import qouteall.imm_ptl.core.ducks.IEFrameBuffer;

import static org.lwjgl.opengl.GL30.GL_DEPTH24_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_DEPTH32F_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_FLOAT_32_UNSIGNED_INT_24_8_REV;

@Mixin(MainTarget.class)
public abstract class MixinMainTarget extends RenderTarget {
    
    public MixinMainTarget(boolean useDepth) {
        super(useDepth);
        throw new RuntimeException();
    }
    
//    @ModifyArgs(
//        method = "allocateDepthAttachment",
//        at = @At(
//            value = "INVOKE",
//            target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"
//        )
//    )
//    private void modifyTexImage2D(Args args) {
//        boolean isStencilBufferEnabled = ((IEFrameBuffer) this).getIsStencilBufferEnabled();
//
//        if (isStencilBufferEnabled) {
//            args.set(2, IPCGlobal.useSeparatedStencilFormat ? GL_DEPTH32F_STENCIL8 : GL_DEPTH24_STENCIL8);
//            args.set(6, ARBFramebufferObject.GL_DEPTH_STENCIL);
//            args.set(7, IPCGlobal.useSeparatedStencilFormat ? GL_FLOAT_32_UNSIGNED_INT_24_8_REV : GL30C.GL_UNSIGNED_INT_24_8);
//        }
//    }

    @ModifyArg(
            method = "allocateDepthAttachment",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"
            ),
            index = 2
    )
    private int modifyTexImage2D_1(int pInternalFormat) {
        boolean isStencilBufferEnabled = ((IEFrameBuffer) this).getIsStencilBufferEnabled();

        if (isStencilBufferEnabled) {
            pInternalFormat = IPCGlobal.useSeparatedStencilFormat ? GL_DEPTH32F_STENCIL8 : GL_DEPTH24_STENCIL8;
        }
        return pInternalFormat;
    }

    @ModifyArg(
            method = "allocateDepthAttachment",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"
            ),
            index = 6
    )
    private int modifyTexImage2D_2(int pFormat) {
        boolean isStencilBufferEnabled = ((IEFrameBuffer) this).getIsStencilBufferEnabled();

        if (isStencilBufferEnabled) {
            pFormat = ARBFramebufferObject.GL_DEPTH_STENCIL;
        }
        return pFormat;
    }

    @ModifyArg(
            method = "allocateDepthAttachment",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"
            ),
            index = 7
    )
    private int modifyTexImage2D_3(int pType) {
        boolean isStencilBufferEnabled = ((IEFrameBuffer) this).getIsStencilBufferEnabled();

        if (isStencilBufferEnabled) {
            pType = IPCGlobal.useSeparatedStencilFormat ? GL_FLOAT_32_UNSIGNED_INT_24_8_REV : GL30C.GL_UNSIGNED_INT_24_8;
        }
        return pType;
    }
    
//    @Redirect(
//        method = "Lcom/mojang/blaze3d/pipeline/MainTarget;allocateDepthAttachment(Lcom/mojang/blaze3d/pipeline/MainTarget$Dimension;)Z",
//        at = @At(
//            value = "INVOKE",
//            target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V",
//            remap = false
//        )
//    )
//    private void onTexImage2D(
//        int target, int level, int internalFormat,
//        int width, int height, int border, int format, int type, IntBuffer pixels
//    ) {
//        boolean isStencilBufferEnabled = ((IEFrameBuffer) this).getIsStencilBufferEnabled();
//
//        if (internalFormat == GL_DEPTH_COMPONENT && isStencilBufferEnabled) {
//            GlStateManager._texImage2D(
//                target,
//                level,
//                IPCGlobal.useAnotherStencilFormat ? GL_DEPTH32F_STENCIL8 : GL_DEPTH24_STENCIL8,//
//                width,
//                height,
//                border,
//                ARBFramebufferObject.GL_DEPTH_STENCIL,
//                IPCGlobal.useAnotherStencilFormat ? GL_FLOAT_32_UNSIGNED_INT_24_8_REV : GL30.GL_UNSIGNED_INT_24_8,//
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
        method = "createFrameBuffer",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glFramebufferTexture2D(IIIII)V"
        ),
        index = 1
    )
    private int modifyFrameBufferTexture2d(int pAttachment) {
        boolean isStencilBufferEnabled = ((IEFrameBuffer) this).getIsStencilBufferEnabled();
        
        if (isStencilBufferEnabled) {
            if (pAttachment == GL30.GL_DEPTH_ATTACHMENT) {
                pAttachment = GL30.GL_DEPTH_STENCIL_ATTACHMENT;
            }
        }

        return pAttachment;
    }
    
//    @Redirect(
//        method = "Lcom/mojang/blaze3d/pipeline/MainTarget;createFrameBuffer(II)V",
//        at = @At(
//            value = "INVOKE",
//            target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glFramebufferTexture2D(IIIII)V",
//            remap = false
//        )
//    )
//    private void redirectFrameBufferTexture2d(
//        int target, int attachment, int textureTarget, int texture, int level
//    ) {
//        boolean isStencilBufferEnabled = ((IEFrameBuffer) this).getIsStencilBufferEnabled();
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
    
}
