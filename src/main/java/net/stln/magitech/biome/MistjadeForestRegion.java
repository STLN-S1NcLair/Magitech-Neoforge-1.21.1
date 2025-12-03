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

public class MistjadeForestRegion extends Region {
    public MistjadeForestRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        ModifiedVanillaOverworldBuilder builder = new ModifiedVanillaOverworldBuilder();

//        new ParameterUtils.ParameterPointListBuilder()
//                .temperature(ParameterUtils.Temperature.span(ParameterUtils.Temperature.COOL, ParameterUtils.Temperature.WARM))
//                .humidity(ParameterUtils.Humidity.span(ParameterUtils.Humidity.ARID, ParameterUtils.Humidity.HUMID))
//                .continentalness(ParameterUtils.Continentalness.span(ParameterUtils.Continentalness.NEAR_INLAND, ParameterUtils.Continentalness.FAR_INLAND))
//                .erosion(ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_4))
//                .depth(ParameterUtils.Depth.span(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.SURFACE))
//                .weirdness(ParameterUtils.Weirdness.span(ParameterUtils.Weirdness.FULL_RANGE, ParameterUtils.Weirdness.FULL_RANGE))
//                .build().forEach(point -> builder.replaceBiome(point, BiomeInit.MISTJADE_FOREST));

        builder.replaceBiome(Biomes.FOREST, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.FLOWER_FOREST, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.BIRCH_FOREST, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.DARK_FOREST, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.OLD_GROWTH_BIRCH_FOREST, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.WINDSWEPT_FOREST, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.TAIGA, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.OLD_GROWTH_PINE_TAIGA, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.SNOWY_TAIGA, BiomeInit.MISTJADE_FOREST);
        builder.replaceBiome(Biomes.OLD_GROWTH_SPRUCE_TAIGA, BiomeInit.MISTJADE_FOREST);

        // Add our points to the mapper
        builder.build().forEach(mapper);
    }
}
