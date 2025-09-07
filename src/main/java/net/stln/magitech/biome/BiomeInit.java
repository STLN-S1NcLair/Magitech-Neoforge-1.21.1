package net.stln.magitech.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.stln.magitech.Magitech;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public class BiomeInit {

    public static final ResourceKey<Biome> MISTJADE_FOREST = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mistjade_forest"));

    public static final ResourceKey<Biome> SCORCHED_PLAINS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "scorched_plains"));

    public static final TagKey<Biome> HAS_CELIFERN_FOREST = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "has_celifern_forest"));

    public static final TagKey<Biome> HAS_CHARCOAL_BIRCH_FOREST = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "has_charcoal_birch_forest"));

    public static final TagKey<Biome> HAS_MANA_BERRY_BUSH = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "has_mana_berry_bush"));

    public static final TagKey<Biome> HAS_MISTALIA_PETALS = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "has_mistalia_petals"));

    public static final TagKey<Biome> IS_SCORCHED = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "is_scorched"));

    public static void registerBiomeRegions(FMLCommonSetupEvent event) {
        event.enqueueWork(() ->
        {
            Regions.register(new MistjadeForestRegion(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mistjade_forest"), 2));
            Regions.register(new ScorchedPlainsRegion(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "scorched_plains"), 2));

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, Magitech.MOD_ID, ScorchedSoilSurfaceRule.makeRule());
        });
    }
}
