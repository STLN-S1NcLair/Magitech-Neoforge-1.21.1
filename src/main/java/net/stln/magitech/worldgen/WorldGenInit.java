package net.stln.magitech.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;

import java.util.List;
import java.util.function.Supplier;

public class WorldGenInit {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Magitech.MOD_ID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> REDSTONE_CRYSTAL_SURFACE_FEATURE =
            FEATURES.register("redstone_crystal_surface_feature", () -> new OreSurfaceFeature(BlockInit.REDSTONE_CRYSTAL_CLUSTER.get().defaultBlockState(), List.of(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE), 0.25));

    public static final ResourceKey<ConfiguredFeature<?, ?>> REDSTONE_CRYSTAL_SURFACE_CONFIGURED_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "redstone_crystal_surface_feature"));

    public static final ResourceKey<PlacedFeature> REDSTONE_CRYSTAL_SURFACE_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "redstone_crystal_surface_feature"));

    public static final ResourceKey<BiomeModifier> REDSTONE_CRYSTAL_SURFACE_BIOME_MODIFIER_KEY =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "redstone_crystal_surface_feature"));



    public static void Configured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(REDSTONE_CRYSTAL_SURFACE_CONFIGURED_KEY,
                new ConfiguredFeature<>(REDSTONE_CRYSTAL_SURFACE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE)
        );
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(REDSTONE_CRYSTAL_SURFACE_PLACED_KEY, new PlacedFeature(
                configured.getOrThrow(REDSTONE_CRYSTAL_SURFACE_CONFIGURED_KEY),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))
        ));
    }

    public static void bootstrapBiomeModifier(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(REDSTONE_CRYSTAL_SURFACE_PLACED_KEY, new PlacedFeature(
                configured.getOrThrow(REDSTONE_CRYSTAL_SURFACE_CONFIGURED_KEY),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))
        ));
    }

    public static void registerBiomeModifiers() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                WorldGenInit.REDSTONE_CRYSTAL_SURFACE_PLACED_KEY
        );
    }

    public static void registerFeatures(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Features for" + Magitech.MOD_ID);
        FEATURES.register(eventBus);
    }
}
