package qouteall.imm_ptl.core.compat.sodium_compatibility;

import me.jellysquid.mods.sodium.client.render.chunk.lists.SortedRenderLists;

public class SodiumRenderingContext {
    public SortedRenderLists renderLists = SortedRenderLists.empty();
    
    public int renderDistance;
    
    public SodiumRenderingContext(int renderDistance) {
        this.renderDistance = renderDistance;
    }
}
