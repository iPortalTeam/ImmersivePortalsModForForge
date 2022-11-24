package qouteall.imm_ptl.core.platform_specific;

//public class IPNetworking {
//
//    public static final ResourceLocation id_ctsTeleport =
//        new ResourceLocation("imm_ptl", "teleport");
//    public static final ResourceLocation id_stcSpawnEntity =
//        new ResourceLocation("imm_ptl", "spawn_entity");
//    public static final ResourceLocation id_stcDimensionConfirm =
//        new ResourceLocation("imm_ptl", "dim_confirm");
//    public static final ResourceLocation id_stcUpdateGlobalPortal =
//        new ResourceLocation("imm_ptl", "upd_glb_ptl");
//    public static final ResourceLocation id_ctsPlayerAction =
//        new ResourceLocation("imm_ptl", "player_action");
//    public static final ResourceLocation id_ctsRightClick =
//        new ResourceLocation("imm_ptl", "right_click");
//
//    public static void init() {
//        ServerPlayNetworking.registerGlobalReceiver(
//            id_ctsTeleport,
//            (server, player, handler, buf, responseSender) -> {
//                processCtsTeleport(player, buf);
//            }
//        );
//
//        ServerPlayNetworking.registerGlobalReceiver(
//            id_ctsPlayerAction,
//            (server, player, handler, buf, responseSender) -> {
//                processCtsPlayerAction(player, buf);
//            }
//        );
//
//        ServerPlayNetworking.registerGlobalReceiver(
//            id_ctsRightClick,
//            (server, player, handler, buf, responseSender) -> {
//                processCtsRightClick(player, buf);
//            }
//        );
//
//
//    }
//
//    public static Packet createStcDimensionConfirm(
//        ResourceKey<Level> dimensionType,
//        Vec3 pos
//    ) {
//        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
//        DimId.writeWorldId(buf, dimensionType, false);
//        buf.writeDouble(pos.x);
//        buf.writeDouble(pos.y);
//        buf.writeDouble(pos.z);
//        return new ClientboundCustomPayloadPacket(id_stcDimensionConfirm, buf);
//    }
//
//    //NOTE my packet is redirected but I cannot get the packet handler info here
//    public static Packet createStcSpawnEntity(
//        Entity entity
//    ) {
//        EntityType entityType = entity.getType();
//        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
//        buf.writeUtf(EntityType.getKey(entityType).toString());
//        buf.writeInt(entity.getId());
//        DimId.writeWorldId(
//            buf, entity.level.dimension(),
//            entity.level.isClientSide
//        );
//        CompoundTag tag = new CompoundTag();
//        entity.saveWithoutId(tag);
//        buf.writeNbt(tag);
//        return new ClientboundCustomPayloadPacket(id_stcSpawnEntity, buf);
//    }
//
//    public static Packet createGlobalPortalUpdate(
//        GlobalPortalStorage storage
//    ) {
//        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
//
//        DimId.writeWorldId(buf, storage.world.get().dimension(), false);
//        buf.writeNbt(storage.save(new CompoundTag()));
//
//        return new ClientboundCustomPayloadPacket(id_stcUpdateGlobalPortal, buf);
//    }
//
//    private static void processCtsTeleport(ServerPlayer player, FriendlyByteBuf buf) {
//        ResourceKey<Level> dim = DimId.readWorldId(buf, false);
//        Vec3 posBefore = new Vec3(
//            buf.readDouble(),
//            buf.readDouble(),
//            buf.readDouble()
//        );
//        UUID portalEntityId = buf.readUUID();
//
//        MiscHelper.executeOnServerThread(() -> {
//            IPGlobal.serverTeleportationManager.onPlayerTeleportedInClient(
//                player,
//                dim,
//                posBefore,
//                portalEntityId
//            );
//        });
//    }
//
//    private static void processCtsPlayerAction(ServerPlayer player, FriendlyByteBuf buf) {
//        ResourceKey<Level> dim = DimId.readWorldId(buf, false);
//        ServerboundPlayerActionPacket packet = new ServerboundPlayerActionPacket(buf);
//        IPGlobal.serverTaskList.addTask(() -> {
//            BlockManipulationServer.processBreakBlock(
//                dim, packet,
//                player
//            );
//            return true;
//        });
//    }
//
//    private static void processCtsRightClick(ServerPlayer player, FriendlyByteBuf buf) {
//        ResourceKey<Level> dim = DimId.readWorldId(buf, false);
//        ServerboundUseItemOnPacket packet = new ServerboundUseItemOnPacket(buf);
//        IPGlobal.serverTaskList.addTask(() -> {
//            BlockManipulationServer.processRightClickBlock(
//                dim, packet,
//                player
//            );
//            return true;
//        });
//    }
//
//}
