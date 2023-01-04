package qouteall.q_misc_util.forge.events;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraftforge.eventbus.api.Event;

public class ServerDimensionsLoadEvent extends Event { // TODO @Nick1st Fire this
    public WorldOptions generatorOptions;
    public RegistryAccess registryManager;

    public ServerDimensionsLoadEvent(WorldOptions settings, RegistryAccess access) {
        generatorOptions = settings;
        registryManager = access;
    }
}