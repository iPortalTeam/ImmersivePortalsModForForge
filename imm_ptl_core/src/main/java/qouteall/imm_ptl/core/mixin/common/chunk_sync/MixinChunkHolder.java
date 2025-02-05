package qouteall.imm_ptl.core.mixin.common.chunk_sync;

import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.chunk_loading.NewChunkTrackingGraph;
import qouteall.imm_ptl.core.ducks.IEChunkHolder;
import qouteall.imm_ptl.core.ducks.IEThreadedAnvilChunkStorage;
import qouteall.imm_ptl.core.network.PacketRedirection;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ChunkHolder.class)
public class MixinChunkHolder implements IEChunkHolder {
    
    @Shadow
    @Final
    ChunkPos pos;
    
    @Shadow
    @Final
    private ChunkHolder.PlayerProvider playerProvider;
    @Unique
    private boolean immersive_portals$boundaryOnly;

    @Inject(method = "broadcastChanges", at = @At("HEAD"))
    private void pre_broadcastChanges(LevelChunk pChunk, CallbackInfo ci){
        this.immersive_portals$boundaryOnly = false; // reset
    }

    @Inject(method = "broadcastChanges", at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkHolder$PlayerProvider;getPlayers(Lnet/minecraft/world/level/ChunkPos;Z)Ljava/util/List;"))
    private void post_getPlayers_0(LevelChunk pChunk, CallbackInfo ci){
        this.immersive_portals$boundaryOnly = true;
    }

    @Inject(method = "broadcastChanges", at = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkHolder$PlayerProvider;getPlayers(Lnet/minecraft/world/level/ChunkPos;Z)Ljava/util/List;"))
    private void post_getPlayers_1(LevelChunk pChunk, CallbackInfo ci){
        this.immersive_portals$boundaryOnly = false;
    }
    
    /**
     * @author qouteall
     * @reason overwriting is clearer
     */
    @Overwrite
    private void broadcast(List<ServerPlayer> pPlayers, Packet<?> pPacket) {
        ResourceKey<Level> dimension =
            ((IEThreadedAnvilChunkStorage) playerProvider).ip_getWorld().dimension();
        
        Consumer<ServerPlayer> func = player ->
            PacketRedirection.sendRedirectedMessage(
                player, dimension, pPacket
            );

        if (this.immersive_portals$boundaryOnly) {
            NewChunkTrackingGraph.getFarWatchers(
                dimension, pos.x, pos.z
            ).forEach(func);
        }
        else {
            NewChunkTrackingGraph.getPlayersViewingChunk(
                dimension, pos.x, pos.z
            ).forEach(func);
        }
        
    }
    
}
