package qouteall.imm_ptl.core.compat.mixin;

import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import qouteall.imm_ptl.core.render.context_management.PortalRendering;
import qouteall.q_misc_util.Helper;

import java.util.ArrayList;

@Mixin(
        value = {RenderRegion.class},
        remap = false
)
public class MixinSodiumRenderRegion {
    @Shadow
    @Final
    private ChunkRenderList renderList;
    @Unique
    private @Nullable ArrayList<ChunkRenderList> chunkRenderListsForPortalRendering = null;

    public MixinSodiumRenderRegion() {
    }

    /**
     * @author qouteall
     * @reason Needed to work with portals
     */
    @Overwrite
    public ChunkRenderList getRenderList() {
        if (!PortalRendering.isRendering()) {
            return this.renderList;
        } else {
            RenderRegion this_ = (RenderRegion)(Object)this;
            if (this.chunkRenderListsForPortalRendering == null) {
                this.chunkRenderListsForPortalRendering = new ArrayList<>();
            }

            int layer = PortalRendering.getPortalLayer();
            int index = layer - 1;
            ChunkRenderList result = Helper.arrayListComputeIfAbsent(this.chunkRenderListsForPortalRendering, index, () -> {
                return new ChunkRenderList(this_);
            });
            return result;
        }
    }
}
