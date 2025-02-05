package qouteall.imm_ptl.peripheral.alternate_dimension;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.ducks.IEWorld;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.api.DimensionAPI;
import qouteall.q_misc_util.forge.events.ServerDimensionsLoadEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class AlternateDimensions {
    
    public static void init() {
        MinecraftForge.EVENT_BUS.register(AlternateDimensions.class);
        
        IPGlobal.postServerTickSignal.connect(AlternateDimensions::tick);
    }

    @SubscribeEvent
    public static void onServerDimensionsLoad(ServerDimensionsLoadEvent event) {
        initializeAlternateDimensions(event.generatorOptions, event.registryManager);
    }

    private static void initializeAlternateDimensions(
        WorldOptions worldOptions, RegistryAccess registryManager
    ) {
        Registry<LevelStem> registry = registryManager.registryOrThrow(Registries.LEVEL_STEM);
        
        long seed = worldOptions.seed();
        if (!IPGlobal.enableAlternateDimensions) {
            return;
        }
        
        Holder<DimensionType> surfaceTypeHolder = registryManager
            .registryOrThrow(Registries.DIMENSION_TYPE)
            .getHolder(ResourceKey.create(
                Registries.DIMENSION_TYPE,
                new ResourceLocation("immersive_portals:surface_type")
            ))
            .orElseThrow(() -> new RuntimeException("Missing immersive_portals:surface_type"));
        
        Holder<DimensionType> surfaceTypeBrightHolder = registryManager
            .registryOrThrow(Registries.DIMENSION_TYPE)
            .getHolder(ResourceKey.create(
                Registries.DIMENSION_TYPE,
                new ResourceLocation("immersive_portals:surface_type_bright")
            ))
            .orElseThrow(() -> new RuntimeException("Missing immersive_portals:surface_type_bright"));
        
        DimensionAPI.addDimension(
            registry,
            alternate1.location(),
            surfaceTypeBrightHolder,
            createSkylandGenerator(registryManager, seed)
        );
        
        DimensionAPI.addDimension(
            registry,
            alternate2.location(),
            surfaceTypeHolder,
            createSkylandGenerator(registryManager, seed + 1) // different seed
        );
        
        DimensionAPI.addDimension(
            registry,
            alternate3.location(),
            surfaceTypeHolder,
            createErrorTerrainGenerator(seed + 1, registryManager)
        );
        
        DimensionAPI.addDimension(
            registry,
            alternate4.location(),
            surfaceTypeHolder,
            createErrorTerrainGenerator(seed, registryManager)
        );
        
        DimensionAPI.addDimension(
            registry,
            alternate5.location(),
            surfaceTypeHolder,
            createVoidGenerator(registryManager)
        );
    }
    
    
    public static final ResourceKey<DimensionType> surfaceType = ResourceKey.create(
        Registries.DIMENSION_TYPE,
        new ResourceLocation("immersive_portals:surface_type")
    );
    public static final ResourceKey<Level> alternate1 = ResourceKey.create(
        Registries.DIMENSION,
        new ResourceLocation("immersive_portals:alternate1")
    );
    public static final ResourceKey<Level> alternate2 = ResourceKey.create(
        Registries.DIMENSION,
        new ResourceLocation("immersive_portals:alternate2")
    );
    public static final ResourceKey<Level> alternate3 = ResourceKey.create(
        Registries.DIMENSION,
        new ResourceLocation("immersive_portals:alternate3")
    );
    public static final ResourceKey<Level> alternate4 = ResourceKey.create(
        Registries.DIMENSION,
        new ResourceLocation("immersive_portals:alternate4")
    );
    public static final ResourceKey<Level> alternate5 = ResourceKey.create(
        Registries.DIMENSION,
        new ResourceLocation("immersive_portals:alternate5")
    );
    
    public static boolean isAlternateDimension(Level world) {
        final ResourceKey<Level> key = world.dimension();
        return key == alternate1 ||
            key == alternate2 ||
            key == alternate3 ||
            key == alternate4 ||
            key == alternate5;
    }
    
    private static void syncWithOverworldTimeWeather(@Nullable ServerLevel world, ServerLevel overworld) {
        if (world == null) {
            return;
        }
        ((IEWorld) world).portal_setWeather(
            overworld.getRainLevel(1), overworld.getRainLevel(1),
            overworld.getThunderLevel(1), overworld.getThunderLevel(1)
        );
    }
    
    public static ChunkGenerator createSkylandGenerator(RegistryAccess rm, long seed) {
        return NormalSkylandGenerator.create(
            rm.registryOrThrow(Registries.BIOME).asLookup(),
            rm.registryOrThrow(Registries.DENSITY_FUNCTION).asLookup(),
            rm.registryOrThrow(Registries.NOISE).asLookup(),
            rm.registryOrThrow(Registries.NOISE_SETTINGS).asLookup(),
            rm.registryOrThrow(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).asLookup(),
            seed
        );
    }
    
    public static ChunkGenerator createErrorTerrainGenerator(long seed, RegistryAccess rm) {
        return ErrorTerrainGenerator.create(
            rm.registryOrThrow(Registries.BIOME).asLookup(),
            rm.registryOrThrow(Registries.NOISE_SETTINGS).asLookup(),
                rm.registryOrThrow(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).asLookup()
        );
    }
    
    public static ChunkGenerator createVoidGenerator(RegistryAccess rm) {
        Registry<Biome> biomeRegistry = rm.registryOrThrow(Registries.BIOME);
        
        Registry<StructureSet> structureSets = rm.registryOrThrow(Registries.STRUCTURE_SET);
        
        Holder.Reference<Biome> plainsHolder = biomeRegistry.getHolderOrThrow(Biomes.PLAINS);
        
        FlatLevelGeneratorSettings flatChunkGeneratorConfig =
            new FlatLevelGeneratorSettings(
                Optional.empty(),
                plainsHolder,
                List.of()
            );
        flatChunkGeneratorConfig.getLayersInfo().add(new FlatLayerInfo(1, Blocks.AIR));
        flatChunkGeneratorConfig.updateLayers();
        
        return new FlatLevelSource(flatChunkGeneratorConfig);
    }
    
    
    private static void tick() {
        if (!IPGlobal.enableAlternateDimensions) {
            return;
        }
        
        ServerLevel overworld = McHelper.getServerWorld(Level.OVERWORLD);
        
        MinecraftServer server = MiscHelper.getServer();
        
        syncWithOverworldTimeWeather(server.getLevel(alternate1), overworld);
        syncWithOverworldTimeWeather(server.getLevel(alternate2), overworld);
        syncWithOverworldTimeWeather(server.getLevel(alternate3), overworld);
        syncWithOverworldTimeWeather(server.getLevel(alternate4), overworld);
        syncWithOverworldTimeWeather(server.getLevel(alternate5), overworld);
    }
}
