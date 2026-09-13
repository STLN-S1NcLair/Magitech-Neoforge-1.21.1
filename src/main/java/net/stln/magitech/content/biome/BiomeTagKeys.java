package net.stln.magitech.content.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.stln.magitech.Magitech;

public final class BiomeTagKeys {
    public static final TagKey<Biome> HAS_CELIFERN_FOREST = mod("has_celifern_forest");
    public static final TagKey<Biome> HAS_CHARCOAL_BIRCH_FOREST = mod("has_charcoal_birch_forest");
    public static final TagKey<Biome> HAS_MANA_BERRY_BUSH = mod("has_mana_berry_bush");
    public static final TagKey<Biome> HAS_MISTALIA_PETALS = mod("has_mistalia_petals");
    public static final TagKey<Biome> IS_SCORCHED = mod("is_scorched");
    public static final TagKey<Biome> HAS_ENGINEER_LODGE = mod("has_engineer_lodge");

    public static final TagKey<Biome> IS_HILL = minecraft("is_hill");
    public static final TagKey<Biome> IS_TAIGA = minecraft("is_taiga");
    public static final TagKey<Biome> IS_JUNGLE = minecraft("is_jungle");
    public static final TagKey<Biome> IS_BEACH = minecraft("is_beach");
    public static final TagKey<Biome> IS_FOREST = minecraft("is_forest");
    public static final TagKey<Biome> IS_OVERWORLD = minecraft("is_overworld");
    public static final TagKey<Biome> STRONGHOLD_BIASED_TO = minecraft("stronghold_biased_to");
    public static final TagKey<Biome> HAS_STRUCTURE_MINESHAFT = minecraft("has_structure/mineshaft");
    public static final TagKey<Biome> HAS_STRUCTURE_PILLAGER_OUTPOST = minecraft("has_structure/pillager_outpost");
    public static final TagKey<Biome> HAS_STRUCTURE_RUINED_PORTAL_STANDARD = minecraft("has_structure/ruined_portal_standard");
    public static final TagKey<Biome> HAS_STRUCTURE_TRIAL_CHAMBERS = minecraft("has_structure/trial_chambers");
    public static final TagKey<Biome> IS_PLAINS = external("c", "is_plains");

    private BiomeTagKeys() {
    }

    private static TagKey<Biome> mod(String path) {
        return TagKey.create(Registries.BIOME, Magitech.id(path));
    }

    private static TagKey<Biome> minecraft(String path) {
        return external("minecraft", path);
    }

    private static TagKey<Biome> external(String namespace, String path) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
