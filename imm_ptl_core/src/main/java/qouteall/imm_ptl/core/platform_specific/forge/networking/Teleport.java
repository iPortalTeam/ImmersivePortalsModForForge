package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.dimension.DimId;

import java.util.UUID;
import java.util.function.Supplier;

public class Teleport {
    private ResourceKey<Level> dimensionBefore;
    private Vec3 posBefore;
    private UUID portalEntityId;

    public Teleport(ResourceKey<Level> dimensionBefore, Vec3 posBefore, UUID portalEntityId) {
        this.dimensionBefore = dimensionBefore;
        this.posBefore = posBefore;
        this.portalEntityId = portalEntityId;
    }

    public Teleport(FriendlyByteBuf buf) {
        dimensionBefore = DimId.readWorldId(buf, false);
        posBefore = new Vec3(
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble()
        );
        portalEntityId = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        DimId.writeWorldId(buf, dimensionBefore, true);
        buf.writeDouble(posBefore.x);
        buf.writeDouble(posBefore.y);
        buf.writeDouble(posBefore.z);
        buf.writeUUID(portalEntityId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> MiscHelper.executeOnServerThread(() -> IPGlobal.serverTeleportationManager.onPlayerTeleportedInClient(ctx.getSender(), dimensionBefore, posBefore, portalEntityId)));
        ctx.setPacketHandled(true);
        return true;
    }
}
