package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.block_manipulation.BlockManipulationServer;
import qouteall.q_misc_util.dimension.DimId;

import java.util.function.Supplier;

public class PlayerAction {
    private ResourceKey<Level> dimension;
    private ServerboundPlayerActionPacket packet;

    public PlayerAction(ResourceKey<Level> dimension, ServerboundPlayerActionPacket packet) {
        this.dimension = dimension;
        this.packet = packet;
    }

    public PlayerAction(FriendlyByteBuf buf) {
        dimension = DimId.readWorldId(buf, false);
        packet = new ServerboundPlayerActionPacket(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        DimId.writeWorldId(buf, dimension, true);
        buf.writeEnum(packet.getAction());
        buf.writeBlockPos(packet.getPos());
        buf.writeByte(packet.getDirection().get3DDataValue());
        buf.writeInt(packet.getSequence());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> IPGlobal.serverTaskList.addTask(() -> {
            BlockManipulationServer.processBreakBlock(dimension, packet, ctx.getSender());
            return true;
        }));
        ctx.setPacketHandled(true);
        return true;
    }
}
