package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.biome.BiomeInit;
import net.stln.magitech.content.biome.BiomeTagKeys;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(output, lookupProvider, Magitech.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BiomeTagKeys.HAS_CELIFERN_FOREST).add(BiomeInit.MISTJADE_FOREST);
        tag(BiomeTagKeys.HAS_CHARCOAL_BIRCH_FOREST).add(BiomeInit.SCORCHED_PLAINS);
        tag(BiomeTagKeys.HAS_MANA_BERRY_BUSH).add(BiomeInit.MISTJADE_FOREST);
        tag(BiomeTagKeys.HAS_MISTALIA_PETALS).add(BiomeInit.MISTJADE_FOREST);
        tag(BiomeTagKeys.IS_SCORCHED).add(BiomeInit.SCORCHED_PLAINS);
        tag(BiomeTagKeys.HAS_ENGINEER_LODGE)
                .addTag(BiomeTagKeys.IS_HILL)
                .addTag(BiomeTagKeys.IS_TAIGA)
                .addTag(BiomeTagKeys.IS_JUNGLE)
                .addTag(BiomeTagKeys.IS_BEACH)
                .add(Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.DESERT, Biomes.SAVANNA,
                        Biomes.SAVANNA_PLATEAU, Biomes.SNOWY_PLAINS, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS,
                        Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST,
                        Biomes.DARK_FOREST, Biomes.MEADOW, Biomes.GROVE, Biomes.SWAMP);
        tag(BiomeTagKeys.IS_FOREST).add(BiomeInit.MISTJADE_FOREST);
        tag(BiomeTagKeys.IS_OVERWORLD).add(BiomeInit.MISTJADE_FOREST, BiomeInit.SCORCHED_PLAINS);
        tag(BiomeTagKeys.STRONGHOLD_BIASED_TO).add(BiomeInit.MISTJADE_FOREST, BiomeInit.SCORCHED_PLAINS);
        tag(BiomeTagKeys.HAS_STRUCTURE_MINESHAFT).add(BiomeInit.MISTJADE_FOREST, BiomeInit.SCORCHED_PLAINS);
        tag(BiomeTagKeys.HAS_STRUCTURE_PILLAGER_OUTPOST).add(BiomeInit.MISTJADE_FOREST, BiomeInit.SCORCHED_PLAINS);
        tag(BiomeTagKeys.HAS_STRUCTURE_RUINED_PORTAL_STANDARD).add(BiomeInit.MISTJADE_FOREST, BiomeInit.SCORCHED_PLAINS);
        tag(BiomeTagKeys.HAS_STRUCTURE_TRIAL_CHAMBERS).add(BiomeInit.MISTJADE_FOREST, BiomeInit.SCORCHED_PLAINS);
        tag(BiomeTagKeys.IS_PLAINS).add(BiomeInit.SCORCHED_PLAINS);
    }
}
