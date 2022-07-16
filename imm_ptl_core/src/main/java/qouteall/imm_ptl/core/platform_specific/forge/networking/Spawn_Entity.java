package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import qouteall.imm_ptl.core.ClientWorldLoader;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.dimension.DimId;
import qouteall.q_misc_util.my_util.SignalArged;

import java.util.Optional;
import java.util.function.Supplier;

public class Spawn_Entity {
    public static final SignalArged<Portal> clientPortalSpawnSignal = new SignalArged<>();

    private Entity entity;


    // Receiver only
    Optional<EntityType<?>> entityType;
    int entityId;
    ResourceKey<Level> dim;
    CompoundTag compoundTag;

    public Spawn_Entity(Entity entity) {
        this.entity = entity;
    }

    public Spawn_Entity(FriendlyByteBuf buf) {
        entityType = EntityType.byString(buf.readUtf());
        entityId = buf.readInt();
        dim = DimId.readWorldId(buf, true);
        compoundTag = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(EntityType.getKey(entity.getType()).toString());
        buf.writeInt(entity.getId());
        DimId.writeWorldId(
                buf, entity.level.dimension(),
                entity.level.isClientSide
        );
        CompoundTag tag = new CompoundTag();
        entity.saveWithoutId(tag);
        buf.writeNbt(tag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(this::processEntitySpawn);
        ctx.setPacketHandled(true);
        return true;
    }

    public void processEntitySpawn() {
        if (entityType.isEmpty()) {
            Helper.err("unknown entity type " + entityType);
            return;
        }

        MiscHelper.executeOnRenderThread(() -> {
            Minecraft.getInstance().getProfiler().push("ip_spawn_entity");

            ClientLevel world = ClientWorldLoader.getWorld(dim);

            Entity entity = entityType.get().create(
                    world
            );
            entity.load(compoundTag);
            entity.setId(entityId);
            entity.setPacketCoordinates(entity.getX(), entity.getY(), entity.getZ());
            world.putNonPlayerEntity(entityId, entity);

            //do not create client world while rendering or gl states will be disturbed
            if (entity instanceof Portal) {
                ClientWorldLoader.getWorld(((Portal) entity).dimensionTo);
                clientPortalSpawnSignal.emit(((Portal) entity));
            }

            Minecraft.getInstance().getProfiler().pop();
        });
    }
}
