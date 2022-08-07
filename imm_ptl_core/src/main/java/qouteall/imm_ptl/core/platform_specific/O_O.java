package qouteall.imm_ptl.core.platform_specific;

import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import qouteall.imm_ptl.core.chunk_loading.MyClientChunkManager;
import qouteall.imm_ptl.core.portal.custom_portal_gen.PortalGenInfo;

public class O_O {
    public static boolean isDimensionalThreadingPresent = false;
    
    public static boolean isForge() {
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerChangeDimensionClient(
        ResourceKey<Level> from, ResourceKey<Level> to
    ) {
        RequiemCompat.onPlayerTeleportedClient();
    }
    
//    @Environment(EnvType.CLIENT)
//    public static void segregateClientEntity(
//        ClientWorld fromWorld,
//        Entity entity
//    ) {
//        ((IEClientWorld_MA) fromWorld).segregateEntity(entity);
//        entity.removed = false;
//    }
//
//    public static void segregateServerEntity(
//        ServerWorld fromWorld,
//        Entity entity
//    ) {
//        fromWorld.removeEntity(entity);
//        entity.removed = false;
//    }
//
//    public static void segregateServerPlayer(
//        ServerWorld fromWorld,
//        ServerPlayerEntity player
//    ) {
//        fromWorld.removePlayer(player);
//        player.removed = false;
//    }
    
    public static void onPlayerTravelOnServer(
        ServerPlayer player,
        ResourceKey<Level> from,
        ResourceKey<Level> to
    ) {
        RequiemCompat.onPlayerTeleportedServer(player);
    }
    
    public static void onServerConstructed() {
        // forge version initialize server config
    }
    
    private static final BlockState obsidianState = Blocks.OBSIDIAN.defaultBlockState();
    
    public static boolean isObsidian(BlockState blockState) {
        return blockState == obsidianState;
    }
    
    public static void postClientChunkLoadEvent(LevelChunk chunk) {
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Load(chunk));
        //ClientChunkEvents.CHUNK_LOAD.invoker().onChunkLoad(((ClientLevel) chunk.getLevel()), chunk);
    }
    
    public static void postClientChunkUnloadEvent(LevelChunk chunk) {
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Unload(chunk));
        //ClientChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload(((ClientLevel) chunk.getLevel()), chunk);
    }
    
    public static boolean isDedicatedServer() {
        return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
    }
    
    public static void postPortalSpawnEventForge(PortalGenInfo info) {
    
    }
    
    @OnlyIn(Dist.CLIENT)
    public static ClientChunkCache createMyClientChunkManager(ClientLevel world, int loadDistance) {
        return new MyClientChunkManager(world, loadDistance);
    }
    
    public static boolean getIsPehkuiPresent() {
        return ModList.get().isLoaded("pehkui");
    }
}
