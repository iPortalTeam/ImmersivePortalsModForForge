package qouteall.imm_ptl.peripheral.alternate_dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public class ChaosBiomeSource extends BiomeSource {
    
    public static final Codec<ChaosBiomeSource> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                Biome.LIST_CODEC.fieldOf("biomes")
                    .forGetter(checkerboardColumnBiomeSource -> checkerboardColumnBiomeSource.allowedBiomes)
            )
            .apply(instance, ChaosBiomeSource::new)
    );
    private final HolderSet<Biome> allowedBiomes;
    
    public ChaosBiomeSource(HolderSet<Biome> holderSet) {
        super(holderSet.stream());
        this.allowedBiomes = holderSet;


//        Set<Holder<Biome>> set = MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(BuiltinRegistries.BIOME).possibleBiomes();
    
    }
    
    
    private Holder<Biome> getRandomBiome(int x, int z) {
        int biomeNum = allowedBiomes.size();
        
        int index = (Math.abs((int) LinearCongruentialGenerator.next(x / 5, z / 5))) % biomeNum;
        return allowedBiomes.get(index);
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }
    
    @Override
    public BiomeSource withSeed(long seed) {
        return this;
    }
    
    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return getRandomBiome(x, z);
    }
    
}
