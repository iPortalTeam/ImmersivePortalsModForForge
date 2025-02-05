package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import qouteall.imm_ptl.core.portal.Portal;
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

    FriendlyByteBuf buf;

    public Spawn_Entity(Entity entity) {
        this.entity = entity;
    }

    public Spawn_Entity(FriendlyByteBuf buf) {
//        entityType = EntityType.byString(buf.readUtf()); // TODO @Nick1st This is strange...
//        entityId = buf.readInt();
//        dim = DimId.readWorldId(buf, true);
//        compoundTag = buf.readNbt();
        this.buf = buf;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(EntityType.getKey(entity.getType()).toString());
        buf.writeInt(entity.getId());
        DimId.writeWorldId(
                buf, entity.level().dimension(),
                entity.level().isClientSide
        );
        CompoundTag tag = new CompoundTag();
        entity.saveWithoutId(tag);
        buf.writeNbt(tag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        entityType = EntityType.byString(buf.readUtf()); // TODO @Nick1st This is strange...
        entityId = buf.readInt();
        dim = DimId.readWorldId(buf, true);
        compoundTag = buf.readNbt();
        ctx.enqueueWork(() -> Spawn_Entity_Client.processEntitySpawn(this));
        ctx.setPacketHandled(true);
        return true;
    }
}
