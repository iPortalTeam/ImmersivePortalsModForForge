package qouteall.imm_ptl.core.compat.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.imm_ptl.core.compat.sodium_compatibility.IESodiumRenderSectionManager;
import qouteall.imm_ptl.core.compat.sodium_compatibility.SodiumRenderingContext;
import qouteall.imm_ptl.core.render.context_management.RenderStates;

//DISABLED_COMPILE@Mixin(value = RenderSectionManager.class, remap = false)
public class MixinSodiumRenderSectionManager implements IESodiumRenderSectionManager {
    //DISABLED_COMPILE    @Shadow
//DISABLED_COMPILE    @Final
//DISABLED_COMPILE    @Mutable
//DISABLED_COMPILE    private ChunkRenderList chunkRenderList;

    //DISABLED_COMPILE   @Shadow
    //DISABLED_COMPILE   @Final
    //DISABLED_COMPILE   @Mutable
    //DISABLED_COMPILE   private ObjectList<RenderSection> tickableChunks;

    //DISABLED_COMPILE  @Shadow
    //DISABLED_COMPILE  @Final
    //DISABLED_COMPILE  @Mutable
    private ObjectList<BlockEntity> visibleBlockEntities;
    
    @Override
    public void ip_swapContext(SodiumRenderingContext context) {
        //DISABLED_COMPILE   ChunkRenderList chunkRenderListTmp = chunkRenderList;
        //DISABLED_COMPILE   chunkRenderList = context.chunkRenderList;
        //DISABLED_COMPILE   context.chunkRenderList = chunkRenderListTmp;

        //DISABLED_COMPILE   ObjectList<RenderSection> tickableChunksTmp = tickableChunks;
        //DISABLED_COMPILE   tickableChunks = context.tickableChunks;
        //DISABLED_COMPILE   context.tickableChunks = tickableChunksTmp;
        
        ObjectList<BlockEntity> visibleBlockEntitiesTmp = visibleBlockEntities;
        visibleBlockEntities = context.visibleBlockEntities;
        context.visibleBlockEntities = visibleBlockEntitiesTmp;
    }

    //DISABLED_COMPILE    @Inject(method = "isSectionVisible", at = @At("HEAD"), cancellable = true)
    private void onIsSectionVisible(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (RenderStates.portalsRenderedThisFrame != 0) {
            // the section visibility information will be wrong if rendered a portal
            // just cancel this optimization
            cir.setReturnValue(true);
        }
    }
}
