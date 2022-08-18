package qouteall.q_misc_util.forge.events;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

import java.util.Set;

public class ServerDimensionDynamicUpdateEvent extends Event {
    Set<ResourceKey<Level>> dimensions;

    public ServerDimensionDynamicUpdateEvent(Set<ResourceKey<Level>> levels) {
        dimensions = levels;
    }
}
