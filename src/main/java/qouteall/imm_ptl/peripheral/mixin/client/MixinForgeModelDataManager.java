package qouteall.imm_ptl.peripheral.mixin.client;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import qouteall.forge_model_data_fix.ForgeModelDataManagerPerWorld;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.chunk_loading.MyClientChunkManager;
import qouteall.q_misc_util.Helper;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(value = ModelDataManager.class, remap = false)
public class MixinForgeModelDataManager {

    @Shadow
    private static WeakReference<Level> currentLevel;
    @Shadow
    @Final
    private static Map<ChunkPos, Set<BlockPos>> needModelDataRefresh;
    @Shadow
    @Final
    private static Map<ChunkPos, Map<BlockPos, IModelData>> modelDataCache;

    private static final ConcurrentHashMap<ResourceKey<Level>, ForgeModelDataManagerPerWorld>
            portal_modelDataManagerMap;

    static {
        portal_modelDataManagerMap = new ConcurrentHashMap<>();

        IPGlobal.clientTaskList.addTask(() -> {
            if(IPGlobal.enableModelDataFix) {
                IPGlobal.clientCleanupSignal.connect(MixinForgeModelDataManager::portal_cleanup);

                MyClientChunkManager.clientChunkUnloadSignal.connect(
                        (chunk) -> portal_getManager(chunk.getLevel()).onChunkUnload(chunk)
                );

                Helper.log("IP Forge Model Data Fix initialized!");
            } else {
                Helper.log("IP Forge Model Data Fix is disabled");
            }
            return true;
        });
    }

    private static void portal_cleanup() {
        portal_modelDataManagerMap.clear();
    }

    private static ForgeModelDataManagerPerWorld portal_getManager(Level level) {
        ResourceKey<Level> dimension = level.dimension();
        return portal_modelDataManagerMap.computeIfAbsent(dimension, k -> new ForgeModelDataManagerPerWorld());
    }

    /**
     * @author qouteall
     * @reason It is used to clean the caches from the fix.
     */
    @Overwrite
    private static void cleanCaches(Level level) {
        if (IPGlobal.enableModelDataFix) {
            if (level != currentLevel.get()) {
                currentLevel = new WeakReference<>(level);
                needModelDataRefresh.clear();
                modelDataCache.clear();
            }
        }
    }

    @Inject(
            method = "requestModelDataRefresh",
            at= @At("HEAD"),
            cancellable = true
    )
    private static void onRequestModelDataRefresh(BlockEntity te, CallbackInfo ci) {
        if (!IPGlobal.enableModelDataFix) {
            return;
        }

        Validate.notNull(te);
        portal_getManager(Objects.requireNonNull(te.getLevel())).requestModelDataRefresh(te);

        ci.cancel();
    }

    @Inject(
            method = "refreshModelData",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onRefreshModelData(Level level, ChunkPos chunk, CallbackInfo ci) {
        if(!IPGlobal.enableModelDataFix) {
            return;
        }

        ci.cancel();
    }

    @Inject(
            method = "getModelData(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraftforge/client/model/data/IModelData;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGetModelData1(Level level, BlockPos pos, CallbackInfoReturnable<IModelData> cir) {
        if (!IPGlobal.enableModelDataFix) {
            return;
        }

        cir.setReturnValue(portal_getManager(level).getModelData(level, pos));
    }

    @Inject(
            method = "getModelData(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;)Ljava/util/Map;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGetModelData2(Level level, ChunkPos pos, CallbackInfoReturnable<Map<BlockPos, IModelData>> cir) {
        if (!IPGlobal.enableModelDataFix) {
            return;
        }

        cir.setReturnValue(portal_getManager(level).getModelData(level, pos));
    }
}