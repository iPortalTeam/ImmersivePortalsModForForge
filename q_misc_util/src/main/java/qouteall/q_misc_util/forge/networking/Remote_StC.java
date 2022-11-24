package qouteall.q_misc_util.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import qouteall.q_misc_util.ImplRemoteProcedureCall;
import qouteall.q_misc_util.MiscHelper;

import java.util.function.Supplier;

public class Remote_StC { //TODO @Nick1st Actually register on the right logical side

    private String methodPath;
    private Object[] arguments;

    private Runnable execute;

    public Remote_StC(String methodPath, Object... arguments) {
        this.methodPath = methodPath;
        this.arguments = arguments;
    }

    public Remote_StC(FriendlyByteBuf buf) {
        execute = ImplRemoteProcedureCall.clientReadPacketAndGetHandler(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        ImplRemoteProcedureCall.serializeStringWithArguments(methodPath, arguments, buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> MiscHelper.executeOnRenderThread(
                execute
        ));
        return true;
    }
}
