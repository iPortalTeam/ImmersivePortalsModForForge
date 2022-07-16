package qouteall.imm_ptl.core.compat.sodium_compatibility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SodiumRenderingContext {
    //DISABLED_COMPILE   public ChunkRenderList chunkRenderList = new ChunkRenderList();
    //DISABLED_COMPILE  public ChunkGraphIterationQueue iterationQueue = new ChunkGraphIterationQueue();

    //DISABLED_COMPILE  public ObjectList<RenderSection> tickableChunks = new ObjectArrayList<>();
    public ObjectList<BlockEntity> visibleBlockEntities = new ObjectArrayList<>();
}
