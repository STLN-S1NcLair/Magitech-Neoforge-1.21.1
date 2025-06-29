package net.stln.magitech.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.UpwardsBranchingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.worldgen.tree.RandomBranchingTrunkPlacer;

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




    public static final Supplier<Feature<NoneFeatureConfiguration>> FLUORITE_CRYSTAL_SURFACE_FEATURE =
            FEATURES.register("fluorite_crystal_surface_feature", () -> new OreSurfaceFeature(BlockInit.FLUORITE_CRYSTAL_CLUSTER.get().defaultBlockState(), List.of(BlockInit.FLUORITE_ORE.get(), BlockInit.DEEPSLATE_FLUORITE_ORE.get()), 0.25));

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLUORITE_CRYSTAL_SURFACE_CONFIGURED_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluorite_crystal_surface_feature"));

    public static final ResourceKey<PlacedFeature> FLUORITE_CRYSTAL_SURFACE_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluorite_crystal_surface_feature"));

    public static final ResourceKey<BiomeModifier> FLUORITE_CRYSTAL_SURFACE_BIOME_MODIFIER_KEY =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluorite_crystal_surface_feature"));



    public static final Supplier<Feature<OreConfiguration>> FLUORITE_ORE_FEATURE =
            FEATURES.register("fluorite_ore_feature", () -> Feature.ORE);

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLUORITE_ORE_CONFIGURED_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluorite_ore_feature"));

    public static final ResourceKey<PlacedFeature> FLUORITE_ORE_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluorite_ore_feature"));

    public static final ResourceKey<BiomeModifier> FLUORITE_ORE_BIOME_MODIFIER_KEY =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluorite_ore_feature"));



    public static final ResourceKey<ConfiguredFeature<?, ?>> CELIFERN_CONFIGURED_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "celifern"));


    public static void Configured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest ruletestStone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest ruletestDeepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        context.register(REDSTONE_CRYSTAL_SURFACE_CONFIGURED_KEY,
                new ConfiguredFeature<>(REDSTONE_CRYSTAL_SURFACE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE)
        );

        context.register(FLUORITE_CRYSTAL_SURFACE_CONFIGURED_KEY,
                new ConfiguredFeature<>(FLUORITE_CRYSTAL_SURFACE_FEATURE.get(), NoneFeatureConfiguration.INSTANCE)
        );

        List<OreConfiguration.TargetBlockState> fluoriteList = List.of(
                OreConfiguration.target(ruletestStone, BlockInit.FLUORITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(ruletestDeepslate, BlockInit.DEEPSLATE_FLUORITE_ORE.get().defaultBlockState())
        );
        context.register(FLUORITE_ORE_CONFIGURED_KEY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(fluoriteList, 8)));

        HolderGetter<Block> holdergetter = context.lookup(Registries.BLOCK);

        context.register(CELIFERN_CONFIGURED_KEY, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BlockInit.CELIFERN_LOG.get()),
                new RandomBranchingTrunkPlacer(8, 1, 3),

                BlockStateProvider.simple(BlockInit.CELIFERN_LEAVES.get()),
                new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 70),

                new TwoLayersFeatureSize(1, 0, 2)).build()));
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(REDSTONE_CRYSTAL_SURFACE_PLACED_KEY, new PlacedFeature(
                configured.getOrThrow(REDSTONE_CRYSTAL_SURFACE_CONFIGURED_KEY),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))
        ));
        context.register(FLUORITE_CRYSTAL_SURFACE_PLACED_KEY, new PlacedFeature(
                configured.getOrThrow(FLUORITE_CRYSTAL_SURFACE_CONFIGURED_KEY),
                List.of(CountPlacement.of(1), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))
        ));
        context.register(FLUORITE_ORE_PLACED_KEY, new PlacedFeature(
                configured.getOrThrow(FLUORITE_ORE_CONFIGURED_KEY),
                List.of(CountPlacement.of(12), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(128))))
        );
    }

    public static void bootstrapBiomeModifier(BootstrapContext<BiomeModifier> context) {
        var biomes = context.lookup(Registries.BIOME);
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        context.register(REDSTONE_CRYSTAL_SURFACE_BIOME_MODIFIER_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(REDSTONE_CRYSTAL_SURFACE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));

        context.register(FLUORITE_CRYSTAL_SURFACE_BIOME_MODIFIER_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(FLUORITE_CRYSTAL_SURFACE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));

        context.register(FLUORITE_ORE_BIOME_MODIFIER_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(FLUORITE_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
    }

//    public static void registerBiomeModifiers() {
//        BiomeModifications.addFeature(
//                BiomeSelectors.foundInOverworld(),
//                GenerationStep.Decoration.UNDERGROUND_DECORATION,
//                WorldGenInit.REDSTONE_CRYSTAL_SURFACE_PLACED_KEY
//        );
//        BiomeModifications.addFeature(
//                BiomeSelectors.foundInOverworld(),
//                GenerationStep.Decoration.UNDERGROUND_DECORATION,
//                WorldGenInit.FLUORITE_CRYSTAL_SURFACE_PLACED_KEY
//        );
//        BiomeModifications.addFeature(
//                BiomeSelectors.foundInOverworld(),
//                GenerationStep.Decoration.UNDERGROUND_ORES,
//                WorldGenInit.FLUORITE_ORE_PLACED_KEY
//        );
//    }

    public static void registerFeatures(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Features for" + Magitech.MOD_ID);
        FEATURES.register(eventBus);
    }
}
