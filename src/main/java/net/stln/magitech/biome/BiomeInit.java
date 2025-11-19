package net.stln.magitech.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.stln.magitech.Magitech;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public class BiomeInit {

    public static final ResourceKey<Biome> MISTJADE_FOREST = ResourceKey.create(Registries.BIOME, Magitech.id("mistjade_forest"));

    public static final ResourceKey<Biome> SCORCHED_PLAINS = ResourceKey.create(Registries.BIOME, Magitech.id("scorched_plains"));

    public static final TagKey<Biome> HAS_CELIFERN_FOREST = createTagKey("has_celifern_forest");

    public static final TagKey<Biome> HAS_CHARCOAL_BIRCH_FOREST = createTagKey("has_charcoal_birch_forest");

    public static final TagKey<Biome> HAS_MANA_BERRY_BUSH = createTagKey("has_mana_berry_bush");

    public static final TagKey<Biome> HAS_MISTALIA_PETALS = createTagKey("has_mistalia_petals");

    public static final TagKey<Biome> IS_SCORCHED = createTagKey("is_scorched");

    private static TagKey<Biome> createTagKey(String path) {
        return TagKey.create(Registries.BIOME, Magitech.id(path));
    }

    public static void registerBiomeRegions(FMLCommonSetupEvent event) {
        event.enqueueWork(() ->
        {
            Regions.register(new MistjadeForestRegion(Magitech.id("mistjade_forest"), 2));
            Regions.register(new ScorchedPlainsRegion(Magitech.id("scorched_plains"), 2));

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, Magitech.MOD_ID, ScorchedSoilSurfaceRule.makeRule());
        });
    }
}
