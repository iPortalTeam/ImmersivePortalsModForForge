package qouteall.q_misc_util.forge.events;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

import java.util.Set;

public class ClientDimensionUpdateEvent extends Event {

    public Set<ResourceKey<Level>> dimIdSet;

    public ClientDimensionUpdateEvent(Set<ResourceKey<Level>> levels) {
        dimIdSet = levels;
    }
}
