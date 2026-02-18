package net.stln.magitech.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ModifiedVanillaOverworldBuilder;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ScorchedPlainsRegion extends Region {
    public ScorchedPlainsRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        ModifiedVanillaOverworldBuilder builder = new ModifiedVanillaOverworldBuilder();

//        new ParameterUtils.ParameterPointListBuilder()
//                .temperature(ParameterUtils.Temperature.span(ParameterUtils.Temperature.WARM, ParameterUtils.Temperature.HOT))
//                .humidity(ParameterUtils.Humidity.span(ParameterUtils.Humidity.DRY, ParameterUtils.Humidity.NEUTRAL))
//                .continentalness(ParameterUtils.Continentalness.span(ParameterUtils.Continentalness.NEAR_INLAND, ParameterUtils.Continentalness.FAR_INLAND))
//                .erosion(ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_2, ParameterUtils.Erosion.EROSION_4))
//                .depth(ParameterUtils.Depth.span(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.SURFACE))
//                .weirdness(ParameterUtils.Weirdness.span(ParameterUtils.Weirdness.FULL_RANGE, ParameterUtils.Weirdness.FULL_RANGE))
//                .build().forEach(point -> builder.replaceBiome(point, BiomeInit.SCORCHED_PLAINS));

        builder.replaceBiome(Biomes.PLAINS, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.SNOWY_PLAINS, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.SUNFLOWER_PLAINS, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.SAVANNA, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.SAVANNA_PLATEAU, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.JUNGLE, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.BAMBOO_JUNGLE, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.SPARSE_JUNGLE, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.BADLANDS, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.ERODED_BADLANDS, BiomeInit.SCORCHED_PLAINS);
        builder.replaceBiome(Biomes.WOODED_BADLANDS, BiomeInit.SCORCHED_PLAINS);

        // Add our points child the mapper
        builder.build().forEach(mapper);
    }
}
