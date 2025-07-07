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

    public static final TagKey<Biome> HAS_CELIFERN_FOREST = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "has_celifern_forest"));

    public static final TagKey<Biome> HAS_MANA_BERRY_BUSH = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "has_mana_berry_bush"));

    public static final TagKey<Biome> HAS_MISTALIA_PETALS = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "has_mistalia_petals"));

    public static void registerBiomeRegions(FMLCommonSetupEvent event) {
        event.enqueueWork(() ->
        {
            Regions.register(new MysticalBiomeRegion(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mystical_biome"), 2));

//            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, Magitech.MOD_ID, TestSurfaceRuleData.makeRules());
        });
    }
}
