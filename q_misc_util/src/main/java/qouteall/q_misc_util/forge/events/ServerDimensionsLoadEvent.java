package qouteall.q_misc_util.forge.events;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraftforge.eventbus.api.Event;

public class ServerDimensionsLoadEvent extends Event { // TODO @Nick1st Fire this
    public WorldGenSettings generatorOptions;
    public RegistryAccess registryManager;

    public ServerDimensionsLoadEvent(WorldGenSettings settings, RegistryAccess access) {
        generatorOptions = settings;
        registryManager = access;
    }
}