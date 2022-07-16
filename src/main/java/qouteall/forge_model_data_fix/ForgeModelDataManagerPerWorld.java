package qouteall.forge_model_data_fix;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copies {@link net.minecraftforge.client.model.ModelDataManager}
 */
public class ForgeModelDataManagerPerWorld {
    private final Map<ChunkPos, Set<BlockPos>> needModelDataRefresh = new ConcurrentHashMap<>();
    private final Map<ChunkPos, Map<BlockPos, IModelData>> modelDataCache = new ConcurrentHashMap<>();
    private final WeakReference<Level> currentWorld = new WeakReference<>(null);

    public ForgeModelDataManagerPerWorld() {

    }

    private void cleanCaches(Level world) {

    }

    public void requestModelDataRefresh(BlockEntity te) {
        Preconditions.checkNotNull(te, "Tile entity must not be null");
        Level world = te.getLevel();

        cleanCaches(world);
        needModelDataRefresh.computeIfAbsent(new ChunkPos(te.getBlockPos()), $ -> Collections.synchronizedSet(new HashSet<>()))
                .add(te.getBlockPos());
    }

    private void refreshModelData(Level world, ChunkPos chunk) {
        cleanCaches(world);
        Set<BlockPos> needUpdate = needModelDataRefresh.remove(chunk);

        if (needUpdate != null) {
            Map<BlockPos, IModelData> data = modelDataCache.computeIfAbsent(chunk, $ -> new ConcurrentHashMap<>());
            for (BlockPos pos : needUpdate) {
                BlockEntity toUpdate = world.getBlockEntity(pos);
                if (toUpdate != null && !toUpdate.isRemoved()) {
                    data.put(pos, toUpdate.getModelData());
                } else {
                    data.remove(pos);
                }
            }
        }
    }

    public void onChunkUnload(LevelChunk chunk) {
        needModelDataRefresh.remove(chunk);
        modelDataCache.remove(chunk);
    }

    @Nullable
    public IModelData getModelData(Level world, BlockPos pos) {
        return getModelData(world, new ChunkPos(pos)).get(pos);
    }

    public Map<BlockPos, IModelData> getModelData(Level world, ChunkPos pos) {
        Preconditions.checkArgument(world.isClientSide(), "Cannot request model data for server world");
        refreshModelData(world, pos);
        return modelDataCache.getOrDefault(pos, Collections.emptyMap());
    }
}
