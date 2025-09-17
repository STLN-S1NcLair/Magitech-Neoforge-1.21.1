package net.stln.magitech.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class MistjadeForestRegion extends Region {
    public MistjadeForestRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        new ParameterUtils.ParameterPointListBuilder()
                .temperature(ParameterUtils.Temperature.span(ParameterUtils.Temperature.COOL, ParameterUtils.Temperature.WARM))
                .humidity(ParameterUtils.Humidity.span(ParameterUtils.Humidity.ARID, ParameterUtils.Humidity.HUMID))
                .continentalness(ParameterUtils.Continentalness.span(ParameterUtils.Continentalness.INLAND, ParameterUtils.Continentalness.FAR_INLAND))
                .erosion(ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_4))
                .depth(ParameterUtils.Depth.span(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.SURFACE))
                .weirdness(ParameterUtils.Weirdness.span(ParameterUtils.Weirdness.FULL_RANGE, ParameterUtils.Weirdness.FULL_RANGE))
                .build().forEach(point -> builder.add(point, BiomeInit.MISTJADE_FOREST));

        // Add our points to the mapper
        builder.build().forEach(mapper);
    }
}
