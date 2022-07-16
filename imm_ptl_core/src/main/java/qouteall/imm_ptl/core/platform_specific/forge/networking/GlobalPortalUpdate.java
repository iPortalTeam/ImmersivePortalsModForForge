package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import qouteall.imm_ptl.core.portal.global_portals.GlobalPortalStorage;
import qouteall.q_misc_util.dimension.DimId;

import java.util.function.Supplier;

public class GlobalPortalUpdate {
    private ResourceKey<Level> dimension;
    private CompoundTag compoundTag;

    public GlobalPortalUpdate(GlobalPortalStorage storage) {
        this.dimension = storage.world.get().dimension();
        this.compoundTag = storage.save(new CompoundTag());
    }

    public GlobalPortalUpdate(FriendlyByteBuf buf) {
        dimension = DimId.readWorldId(buf, true);
        compoundTag = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        DimId.writeWorldId(buf, dimension, false);
        buf.writeNbt(compoundTag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> GlobalPortalStorage.receiveGlobalPortalSync(dimension, compoundTag));
        ctx.setPacketHandled(true);
        return true;
    }
}
