package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.block_manipulation.BlockManipulationServer;
import qouteall.q_misc_util.dimension.DimId;

import java.util.function.Supplier;

public class RightClick {
    private ResourceKey<Level> dimension;
    private ServerboundUseItemOnPacket packet;

    public RightClick(ResourceKey<Level> dimension, ServerboundUseItemOnPacket packet) {
        this.dimension = dimension;
        this.packet = packet;
    }

    public RightClick(FriendlyByteBuf buf) {
        dimension = DimId.readWorldId(buf, false);
        packet = new ServerboundUseItemOnPacket(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        DimId.writeWorldId(buf, dimension, true);
        buf.writeEnum(packet.getHand());
        buf.writeBlockHitResult(packet.getHitResult());
        buf.writeInt(packet.getSequence());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> IPGlobal.serverTaskList.addTask(() -> {
            BlockManipulationServer.processRightClickBlock(dimension, packet, ctx.getSender());
            return true;
        }));
        ctx.setPacketHandled(true);
        return true;
    }
}
