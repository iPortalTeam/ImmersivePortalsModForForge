package qouteall.imm_ptl.core.platform_specific;

import com.demonwav.mcdev.annotations.Env;
import com.demonwav.mcdev.annotations.CheckEnv;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.my_util.SignalArged;

@CheckEnv(Env.CLIENT)
public class IPNetworkingClient {

    public static final SignalArged<Portal> clientPortalSpawnSignal = new SignalArged<>();
//    private static final Minecraft client = Minecraft.getInstance();
//
//    public static void init() {
//
//        ClientPlayNetworking.registerGlobalReceiver(
//            IPNetworking.id_stcSpawnEntity,
//            (c, handler, buf, responseSender) -> {
//                processStcSpawnEntity(buf);
//
//            }
//        );
//
//        ClientPlayNetworking.registerGlobalReceiver(
//            IPNetworking.id_stcDimensionConfirm,
//            (c, handler, buf, responseSender) -> {
//                processStcDimensionConfirm(buf);
//            }
//        );
//
//        ClientPlayNetworking.registerGlobalReceiver(
//            IPNetworking.id_stcUpdateGlobalPortal,
//            (c, handler, buf, responseSender) -> {
//                processGlobalPortalUpdate(buf);
//            }
//        );
//
//
//    }
//
//    private static void processStcSpawnEntity(FriendlyByteBuf buf) {
//        String entityTypeString = buf.readUtf();
//
//        int entityId = buf.readInt();
//
//        ResourceKey<Level> dim = DimId.readWorldId(buf, true);
//
//        CompoundTag compoundTag = buf.readNbt();
//
//        processEntitySpawn(entityTypeString, entityId, dim, compoundTag);
//    }
//
//    private static void processStcDimensionConfirm(FriendlyByteBuf buf) {
//
//        ResourceKey<Level> dimension = DimId.readWorldId(buf, true);
//        Vec3 pos = new Vec3(
//            buf.readDouble(),
//            buf.readDouble(),
//            buf.readDouble()
//        );
//
//        MiscHelper.executeOnRenderThread(() -> {
//            IPCGlobal.clientTeleportationManager.acceptSynchronizationDataFromServer(
//                dimension, pos,
//                false
//            );
//        });
//    }
//
//    private static void processGlobalPortalUpdate(FriendlyByteBuf buf) {
//        ResourceKey<Level> dimension = DimId.readWorldId(buf, true);
//        CompoundTag compoundTag = buf.readNbt();
//        MiscHelper.executeOnRenderThread(() -> {
//            GlobalPortalStorage.receiveGlobalPortalSync(dimension, compoundTag);
//        });
//    }
//
//    public static Packet createCtsPlayerAction(
//        ResourceKey<Level> dimension,
//        ServerboundPlayerActionPacket packet
//    ) {
//        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
//        DimId.writeWorldId(buf, dimension, true);
//        packet.write(buf);
//        return new ServerboundCustomPayloadPacket(IPNetworking.id_ctsPlayerAction, buf);
//    }
//
//    public static Packet createCtsRightClick(
//        ResourceKey<Level> dimension,
//        ServerboundUseItemOnPacket packet
//    ) {
//        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
//        DimId.writeWorldId(buf, dimension, true);
//        packet.write(buf);
//        return new ServerboundCustomPayloadPacket(IPNetworking.id_ctsRightClick, buf);
//    }
//
//    public static Packet createCtsTeleport(
//        ResourceKey<Level> dimensionBefore,
//        Vec3 posBefore,
//        UUID portalEntityId
//    ) {
//        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
//        DimId.writeWorldId(buf, dimensionBefore, true);
//        buf.writeDouble(posBefore.x);
//        buf.writeDouble(posBefore.y);
//        buf.writeDouble(posBefore.z);
//        buf.writeUUID(portalEntityId);
//        return new ServerboundCustomPayloadPacket(IPNetworking.id_ctsTeleport, buf);
//    }
//
//    public static void processEntitySpawn(String entityTypeString, int entityId, ResourceKey<Level> dim, CompoundTag compoundTag) {
//        Optional<EntityType<?>> entityType = EntityType.byString(entityTypeString);
//        if (!entityType.isPresent()) {
//            Helper.err("unknown entity type " + entityTypeString);
//            return;
//        }
//
//        MiscHelper.executeOnRenderThread(() -> {
//            client.getProfiler().push("ip_spawn_entity");
//
//            ClientLevel world = ClientWorldLoader.getWorld(dim);
//
//            Entity entity = entityType.get().create(
//                world
//            );
//            entity.load(compoundTag);
//            entity.setId(entityId);
//            entity.syncPacketPositionCodec(entity.getX(), entity.getY(), entity.getZ());
//            world.putNonPlayerEntity(entityId, entity);
//
//            //do not create client world while rendering or gl states will be disturbed
//            if (entity instanceof Portal) {
//                ClientWorldLoader.getWorld(((Portal) entity).dimensionTo);
//                clientPortalSpawnSignal.emit(((Portal) entity));
//            }
//
//            client.getProfiler().pop();
//        });
//    }
}
