package qouteall.imm_ptl.core.platform_specific.forge.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import qouteall.imm_ptl.core.ClientWorldLoader;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.MiscHelper;

public class Spawn_Entity_Client {
    private Spawn_Entity_Client() {

    }

    public static void processEntitySpawn(Spawn_Entity pack) {
        if (pack.entityType.isEmpty()) {
            Helper.err("unknown entity type " + pack.entityType);
            return;
        }

        MiscHelper.executeOnRenderThread(() -> {
            Minecraft.getInstance().getProfiler().push("ip_spawn_entity");

            ClientLevel world = ClientWorldLoader.getWorld(pack.dim);

            Entity entity = pack.entityType.get().create(
                    world
            );
            entity.load(pack.compoundTag);
            entity.setId(pack.entityId);
            entity.setPosRaw(entity.getX(), entity.getY(), entity.getZ());
            world.putNonPlayerEntity(pack.entityId, entity);

            //do not create client world while rendering or gl states will be disturbed
            if (entity instanceof Portal) {
                ClientWorldLoader.getWorld(((Portal) entity).dimensionTo);
                Spawn_Entity.clientPortalSpawnSignal.emit(((Portal) entity));
            }

            Minecraft.getInstance().getProfiler().pop();
        });
    }
}
