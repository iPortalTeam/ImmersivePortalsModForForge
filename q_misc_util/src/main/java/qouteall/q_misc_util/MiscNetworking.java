package qouteall.q_misc_util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MiscNetworking {
    public static final ResourceLocation id_stcRemote =
        new ResourceLocation("imm_ptl", "remote_stc");
    public static final ResourceLocation id_ctsRemote =
        new ResourceLocation("imm_ptl", "remote_cts");

    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
//        ClientPlayNetworking.registerGlobalReceiver( //TODO Reimplement this !IMPORTANT
//            MiscNetworking.id_stcRemote,
//            (c, handler, buf, responseSender) -> {
//                MiscHelper.executeOnRenderThread(
//                    ImplRemoteProcedureCall.clientReadPacketAndGetHandler(buf)
//                );
//            }
//        );
    }
    
    public static void init() {
//        ServerPlayNetworking.registerGlobalReceiver( //TODO Reimplement this !IMPORTANT
//            MiscNetworking.id_ctsRemote,
//            (server, player, handler, buf, responseSender) -> {
//                MiscHelper.executeOnServerThread(
//                    ImplRemoteProcedureCall.serverReadPacketAndGetHandler(player, buf)
//                );
//            }
//        );
    }
}
