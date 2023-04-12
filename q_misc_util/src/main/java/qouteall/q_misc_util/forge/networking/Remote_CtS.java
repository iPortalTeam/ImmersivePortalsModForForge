package qouteall.q_misc_util.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import qouteall.q_misc_util.ImplRemoteProcedureCall;
import qouteall.q_misc_util.MiscHelper;

import java.util.function.Supplier;

public class Remote_CtS {
    private String methodPath;
    private Object[] arguments;

    private FriendlyByteBuf receivedBuffer;

    public Remote_CtS(String methodPath, Object... arguments) {
        this.methodPath = methodPath;
        this.arguments = arguments;
    }

    public Remote_CtS(FriendlyByteBuf buf) {
        receivedBuffer = buf;
    }

    public void toBytes(FriendlyByteBuf buf) {
        ImplRemoteProcedureCall.serializeStringWithArguments(methodPath, arguments, buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> MiscHelper.executeOnServerThread(
                ImplRemoteProcedureCall.serverReadPacketAndGetHandler(ctx.getSender(), receivedBuffer)
        ));
        return true;
    }
}
