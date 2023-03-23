package qouteall.imm_ptl.peripheral.alternate_dimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PeripheralRegistries {

    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = DeferredRegister.create(Registries.CHUNK_GENERATOR, "immersive_portals");
    public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCE = DeferredRegister.create(Registries.BIOME_SOURCE, "immersive_portals");

    public static final RegistryObject<Codec<ErrorTerrainGenerator>> ERROR_TERRAIN_GENERATOR = CHUNK_GENERATOR.register("error_terrain_generator", () -> ErrorTerrainGenerator.codec);
    public static final RegistryObject<Codec<NormalSkylandGenerator>> NORMAL_SKYLAND_GENERATOR = CHUNK_GENERATOR.register("normal_skyland_generator", () -> NormalSkylandGenerator.codec);
    public static final RegistryObject<Codec<ChaosBiomeSource>> CHAOS_BIOME_SOURCE = BIOME_SOURCE.register("chaos_biome_source", () -> ChaosBiomeSource.CODEC);
}
