package qouteall.q_misc_util;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.Validate;
import qouteall.q_misc_util.dimension.DimensionIdRecord;
import qouteall.q_misc_util.dimension.DimensionTypeSync;
import qouteall.q_misc_util.forge.events.ClientDimensionUpdateEvent;
import qouteall.q_misc_util.mixin.client.IEClientPacketListener_Misc;

import java.util.Set;
import java.util.function.Supplier;

public class MiscNetworking {
    public static final ResourceLocation id_stcRemote =
        new ResourceLocation("imm_ptl", "remote_stc");
    public static final ResourceLocation id_ctsRemote =
        new ResourceLocation("imm_ptl", "remote_cts");
    
    public static final ResourceLocation id_stcDimSync =
        new ResourceLocation("imm_ptl", "dim_sync");
    
    // no need to make this client only
    public static boolean handleMiscUtilPacketClientSide(
        ResourceLocation id,
        FriendlyByteBuf buf,
        ClientGamePacketListener networkHandler
    ) {
        if (id.equals(id_stcRemote)) {
            MiscHelper.executeOnRenderThread(
                ImplRemoteProcedureCall.clientReadPacketAndGetHandler(buf)
            );
            return true;
        }
        else if (id.equals(id_stcDimSync)) {
            processDimSync(buf, networkHandler);
            return true;
        }
        return false;
    }
    
    public static boolean handleMiscUtilPacketServerSide(
        ResourceLocation id,
        ServerPlayer player,
        FriendlyByteBuf buf
    ) {
        if (id.equals(id_ctsRemote)) {
            MiscHelper.executeOnServerThread(
                ImplRemoteProcedureCall.serverReadPacketAndGetHandler(player, buf)
            );
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static void initClient() {

    }

    public static void init() {

    }

    public static Packet createDimSyncPacket() {
        Validate.notNull(DimensionIdRecord.serverRecord);
        
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        
        CompoundTag idMapTag = DimensionIdRecord.recordToTag(
            DimensionIdRecord.serverRecord,
            dim -> MiscHelper.getServer().getLevel(dim) != null
        );
        buf.writeNbt(idMapTag);
        
        CompoundTag typeMapTag = DimensionTypeSync.createTagFromServerWorldInfo();
        buf.writeNbt(typeMapTag);
        
        return new ClientboundCustomPayloadPacket(id_stcDimSync, buf);
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void processDimSync(
        FriendlyByteBuf buf,
        ClientGamePacketListener packetListener
    ) {
        CompoundTag idMap = buf.readNbt();
        
        DimensionIdRecord.clientRecord = DimensionIdRecord.tagToRecord(idMap);
        
        CompoundTag typeMap = buf.readNbt();
        
        MiscHelper.executeOnRenderThread(() -> {
            DimensionTypeSync.acceptTypeMapData(typeMap);
            
            Helper.log("Received Dimension Int Id Sync");
            Helper.log("\n" + DimensionIdRecord.clientRecord);
            
            // it's used for command completion
            Set<ResourceKey<Level>> dimIdSet = DimensionIdRecord.clientRecord.getDimIdSet();
            ((IEClientPacketListener_Misc) packetListener).ip_setLevels(dimIdSet);

            MinecraftForge.EVENT_BUS.post(new ClientDimensionUpdateEvent(dimIdSet));
        });
    }
}
