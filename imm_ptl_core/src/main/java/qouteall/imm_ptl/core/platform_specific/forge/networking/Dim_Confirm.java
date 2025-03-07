package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import qouteall.imm_ptl.core.IPCGlobal;
import qouteall.imm_ptl.core.teleportation.ClientTeleportationManager;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.dimension.DimId;

import java.util.function.Supplier;

public class Dim_Confirm {
    private ResourceKey<Level> dimensionType;
    private Vec3 pos;

    public Dim_Confirm(ResourceKey<Level> dimensionType, Vec3 pos) {
        this.dimensionType = dimensionType;
        this.pos = pos;
    }

    public Dim_Confirm(FriendlyByteBuf buf) {
        dimensionType = DimId.readWorldId(buf, true);
        pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void toBytes(FriendlyByteBuf buf) {
        DimId.writeWorldId(buf, dimensionType, false);
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> MiscHelper.executeOnRenderThread(() -> ClientTeleportationManager.acceptSynchronizationDataFromServer(dimensionType, pos, false)));
        ctx.setPacketHandled(true);
        return true;
    }
}
