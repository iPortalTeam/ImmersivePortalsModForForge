package qouteall.q_misc_util.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import qouteall.q_misc_util.ImplRemoteProcedureCall;
import qouteall.q_misc_util.MiscHelper;

import java.util.function.Supplier;

public class Remote_StC { //TODO Actually register on the right logical side

    private final FriendlyByteBuf buf;

    public Remote_StC(FriendlyByteBuf buf) {
        this.buf = buf;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.setBytes(0, this.buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> MiscHelper.executeOnRenderThread(
                ImplRemoteProcedureCall.clientReadPacketAndGetHandler(buf)
        ));
        return true;
    }
}
