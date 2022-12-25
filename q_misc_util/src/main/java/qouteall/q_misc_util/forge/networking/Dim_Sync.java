package qouteall.q_misc_util.forge.networking;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.Validate;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.dimension.DimensionIdRecord;
import qouteall.q_misc_util.dimension.DimensionTypeSync;
import qouteall.q_misc_util.forge.events.ClientDimensionUpdateEvent;
import qouteall.q_misc_util.mixin.client.IEClientPacketListener_Misc;

import java.util.Set;
import java.util.function.Supplier;

public class Dim_Sync {
    private CompoundTag idMap;
    private CompoundTag typeMap;

    public Dim_Sync() {
        Validate.notNull(DimensionIdRecord.serverRecord);
        idMap = DimensionIdRecord.recordToTag(
                DimensionIdRecord.serverRecord,
                dim -> MiscHelper.getServer().getLevel(dim) != null
        );
        typeMap = DimensionTypeSync.createTagFromServerWorldInfo();
    }

    public Dim_Sync(FriendlyByteBuf buf) {
        idMap = buf.readNbt();
        typeMap = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(idMap);
        buf.writeNbt(typeMap);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> processDimSync(idMap, typeMap, (ClientPacketListener) ctx.getNetworkManager().getPacketListener()));
        ctx.setPacketHandled(true);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private static void processDimSync(CompoundTag idMap, CompoundTag typeMap, ClientPacketListener packetListener) {
        DimensionIdRecord.clientRecord = DimensionIdRecord.tagToRecord(idMap);

        MiscHelper.executeOnRenderThread(() -> {
            DimensionTypeSync.acceptTypeMapData(typeMap);

            Helper.log("Received Dimension Int Id Sync");
            Helper.log("\n" + DimensionIdRecord.clientRecord);

            // it's used for command completion
            Set<ResourceKey<Level>> dimIdSet = DimensionIdRecord.clientRecord.getDimIdSet();
            ((IEClientPacketListener_Misc) packetListener).ip_setLevels(dimIdSet);

            //DimensionAPI.clientDimensionUpdateEvent.invoker().run(dimIdSet);
            MinecraftForge.EVENT_BUS.post(new ClientDimensionUpdateEvent(dimIdSet));
        });
    }
}