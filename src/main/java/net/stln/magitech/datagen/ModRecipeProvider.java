package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ItemTagKeys;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.component.MaterialComponent;
import net.stln.magitech.content.recipe.CompressingRecipe;
import net.stln.magitech.content.recipe.CrushingRecipe;
import net.stln.magitech.content.recipe.InfusionRecipe;
import net.stln.magitech.content.recipe.PartCuttingRecipe;
import net.stln.magitech.content.recipe.SpellConversionRecipe;
import net.stln.magitech.content.recipe.ToolAssemblyRecipe;
import net.stln.magitech.content.recipe.ToolMaterialRecipe;
import net.stln.magitech.content.recipe.ZardiusCrucibleRecipe;
import net.stln.magitech.content.fluid.FluidInit;
import net.stln.magitech.datagen.recipe.StoneRecipeGenerator;
import net.stln.magitech.datagen.recipe.BlockSetRecipeGenerator;
import net.stln.magitech.datagen.recipe.VanillaSimpleRecipeGenerator;
import net.stln.magitech.datagen.recipe.WoodRecipeGenerator;
import net.stln.magitech.feature.magic.spell.SpellInit;
import net.stln.magitech.feature.tool.material.MaterialInit;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        StoneRecipeGenerator.buildStoneRecipesWithPolishedAndBrick(output,
                BlockInit.ALCHECRYSITE_ITEM.get(), BlockInit.ALCHECRYSITE_SLAB_ITEM.get(), BlockInit.ALCHECRYSITE_STAIRS_ITEM.get(), BlockInit.ALCHECRYSITE_WALL_ITEM.get(),
                BlockInit.POLISHED_ALCHECRYSITE_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_SLAB_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_STAIRS_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_WALL_ITEM.get(),
                BlockInit.ALCHECRYSITE_BRICKS_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_SLAB_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_STAIRS_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_WALL_ITEM.get());
        BlockSetRecipeGenerator.stonecutting(output, BlockInit.ALCHECRYSITE_ITEM.get(), BlockInit.ALCHECRYSITE_TILES_ITEM.get(), 1, "_from_alchecrysite_stonecutting");
        BlockSetRecipeGenerator.stonecutting(output, BlockInit.POLISHED_ALCHECRYSITE_ITEM.get(), BlockInit.ALCHECRYSITE_TILES_ITEM.get(), 1, "_from_polished_alchecrysite_stonecutting");
        StoneRecipeGenerator.buildStoneRecipes(output, BlockInit.VESPERITE_ITEM.get(), BlockInit.VESPERITE_SLAB_ITEM.get(), BlockInit.VESPERITE_STAIRS_ITEM.get(), BlockInit.VESPERITE_WALL_ITEM.get());
        StoneRecipeGenerator.buildStoneRecipesFromResourceBlock(output, BlockInit.FLUORITE_BLOCK_ITEM.get(), BlockInit.FLUORITE_BRICKS_ITEM.get(), BlockInit.FLUORITE_BRICK_SLAB_ITEM.get(), BlockInit.FLUORITE_BRICK_STAIRS_ITEM.get(), BlockInit.FLUORITE_BRICK_WALL_ITEM.get());
        WoodRecipeGenerator.buildWoodRecipes(output, ItemTagKeys.CELIFERN_LOGS, BlockInit.CELIFERN_LOG_ITEM.get(), BlockInit.CELIFERN_WOOD_ITEM.get(), BlockInit.STRIPPED_CELIFERN_LOG_ITEM.get(), BlockInit.STRIPPED_CELIFERN_WOOD_ITEM.get(), BlockInit.CELIFERN_PLANKS_ITEM.get(), BlockInit.CELIFERN_SLAB_ITEM.get(), BlockInit.CELIFERN_STAIRS_ITEM.get(), BlockInit.CELIFERN_FENCE_ITEM.get(), BlockInit.CELIFERN_FENCE_GATE_ITEM.get(), BlockInit.CELIFERN_DOOR_ITEM.get(), BlockInit.CELIFERN_TRAPDOOR_ITEM.get(), BlockInit.CELIFERN_PRESSURE_PLATE_ITEM.get(), BlockInit.CELIFERN_BUTTON_ITEM.get(), BlockInit.CELIFERN_SIGN_ITEM.get(), BlockInit.CELIFERN_HANGING_SIGN_ITEM.get(), ItemInit.CELIFERN_BOAT.get(), ItemInit.CELIFERN_CHEST_BOAT.get());
        WoodRecipeGenerator.buildWoodRecipes(output, ItemTagKeys.CHARCOAL_BIRCH_LOGS, BlockInit.CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.CHARCOAL_BIRCH_PLANKS_ITEM.get(), BlockInit.CHARCOAL_BIRCH_SLAB_ITEM.get(), BlockInit.CHARCOAL_BIRCH_STAIRS_ITEM.get(), BlockInit.CHARCOAL_BIRCH_FENCE_ITEM.get(), BlockInit.CHARCOAL_BIRCH_FENCE_GATE_ITEM.get(), BlockInit.CHARCOAL_BIRCH_DOOR_ITEM.get(), BlockInit.CHARCOAL_BIRCH_TRAPDOOR_ITEM.get(), BlockInit.CHARCOAL_BIRCH_PRESSURE_PLATE_ITEM.get(), BlockInit.CHARCOAL_BIRCH_BUTTON_ITEM.get(), BlockInit.CHARCOAL_BIRCH_SIGN_ITEM.get(), BlockInit.CHARCOAL_BIRCH_HANGING_SIGN_ITEM.get(), ItemInit.CHARCOAL_BIRCH_BOAT.get(), ItemInit.CHARCOAL_BIRCH_CHEST_BOAT.get());
        WoodRecipeGenerator.buildWoodRecipes(output, ItemTagKeys.MYSTWOOD_LOGS, BlockInit.MYSTWOOD_LOG_ITEM.get(), BlockInit.MYSTWOOD_WOOD_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM.get(), BlockInit.MYSTWOOD_PLANKS_ITEM.get(), BlockInit.MYSTWOOD_SLAB_ITEM.get(), BlockInit.MYSTWOOD_STAIRS_ITEM.get(), BlockInit.MYSTWOOD_FENCE_ITEM.get(), BlockInit.MYSTWOOD_FENCE_GATE_ITEM.get(), BlockInit.MYSTWOOD_DOOR_ITEM.get(), BlockInit.MYSTWOOD_TRAPDOOR_ITEM.get(), BlockInit.MYSTWOOD_PRESSURE_PLATE_ITEM.get(), BlockInit.MYSTWOOD_BUTTON_ITEM.get(), BlockInit.MYSTWOOD_SIGN_ITEM.get(), BlockInit.MYSTWOOD_HANGING_SIGN_ITEM.get(), ItemInit.MYSTWOOD_BOAT.get(), ItemInit.MYSTWOOD_CHEST_BOAT.get());

        buildVanilla(output);
        buildCustom(output);
    }

    private static void buildVanilla(RecipeOutput output) {
        shaped(output, ItemInit.ALCHEMICAL_FLASK.get(), 4, keys('#', i(BlockInit.MANA_INSULATING_GLASS_ITEM.get())), "# #", " # ");
        shaped(output, ItemInit.APPLIED_ARCANE_CIRCUITRY.get(), 1, keys('Z', component(ItemInit.REINFORCED_ROD.get(), MaterialInit.ZINC.get(), false), 'B', i(Items.BOOK), 'N', i(ItemInit.AGGREGATED_NOCTIS.get()), 'L', i(ItemInit.AGGREGATED_LUMINIS.get()), 'F', i(ItemInit.AGGREGATED_FLUXIA.get())), "ZN ", "LBZ", " FZ");
        shaped(output, BlockInit.ASSEMBLY_WORKBENCH_ITEM.get(), 1, keys('#', tag(ItemTagKeys.STRIPPED_LOGS), 'S', tag(ItemTagKeys.STONES), 'P', ItemTags.PLANKS), "SSS", "#P#", "SPS");
        shaped(output, BlockInit.ENGINEERING_WORKBENCH_ITEM.get(), 1, keys('#', tag(ItemTagKeys.STRIPPED_LOGS), 'S', tag(ItemTagKeys.STONES), 'P', ItemTags.PLANKS), "SSS", "#P#", "#S#");
        shaped(output, BlockInit.REPAIRING_WORKBENCH_ITEM.get(), 1, keys('#', tag(ItemTagKeys.STRIPPED_LOGS), 'S', tag(ItemTagKeys.STONES), 'P', ItemTags.PLANKS), "SSS", "#S#", "SPS");
        shaped(output, BlockInit.UPGRADE_WORKBENCH_ITEM.get(), 1, keys('#', tag(ItemTagKeys.STRIPPED_LOGS), 'S', tag(ItemTagKeys.STONES), 'I', i(Items.IRON_INGOT), 'P', ItemTags.PLANKS), "SSS", "#I#", "SPS");
        shaped(output, BlockInit.COMPRESSOR_ITEM.get(), 1, keys('I', i(ItemInit.FLUXIUM_INGOT.get()), 'U', i(ItemInit.FLUXIUM_NUGGET.get()), 'C', i(Items.IRON_BLOCK), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get())), "IAI", "UCU", "IAI");
        shaped(output, BlockInit.CRUSHER_ITEM.get(), 1, keys('I', i(ItemInit.FLUXIUM_INGOT.get()), 'C', tag(ItemTagKeys.INGOTS_IRON), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), 'G', i(BlockInit.MANA_INSULATING_GLASS_ITEM.get())), "IAI", "CGC", "IAI");
        shaped(output, BlockInit.DETANGLER_ITEM.get(), 1, keys('I', i(ItemInit.FLUXIUM_INGOT.get()), 'U', i(ItemInit.FLUXIUM_NUGGET.get()), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), 'F', i(ItemInit.HIGH_PURITY_FLUORITE.get())), "IUI", "UIU", "FAF");
        shaped(output, BlockInit.ENTANGLER_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'I', i(ItemInit.FLUXIUM_INGOT.get()), 'U', i(ItemInit.FLUXIUM_NUGGET.get()), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), 'F', i(ItemInit.HIGH_PURITY_FLUORITE.get())), "IUI", "UNU", "FAF");
        shaped(output, BlockInit.INFUSION_ALTAR_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'V', i(BlockInit.VESPERITE_ITEM.get()), 'M', i(BlockInit.MYSTWOOD_PLANKS_ITEM.get()), 'F', tag(ItemTagKeys.GEMS_FLUORITE)), "VFV", "NMN", "MVM");
        shaped(output, BlockInit.ITEM_COLLECTOR_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'I', i(ItemInit.FLUXIUM_INGOT.get()), 'C', tag(ItemTagKeys.INGOTS_COPPER), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), 'F', i(ItemInit.HIGH_PURITY_FLUORITE.get())), "INI", "CFC", "IAI");
        shaped(output, BlockInit.MANA_COLLECTOR_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'I', i(ItemInit.FLUXIUM_INGOT.get()), 'U', i(ItemInit.FLUXIUM_NUGGET.get()), 'C', tag(ItemTagKeys.INGOTS_COPPER), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), 'F', i(ItemInit.HIGH_PURITY_FLUORITE.get())), "ICI", "UFU", "ANA");
        shaped(output, BlockInit.MANA_JUNCTION_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'I', i(ItemInit.FLUXIUM_INGOT.get()), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), 'F', i(ItemInit.HIGH_PURITY_FLUORITE.get())), "INA", "NFN", "ANI");
        shaped(output, BlockInit.MANA_NODE_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'V', i(BlockInit.VESPERITE_ITEM.get()), 'M', i(BlockInit.MYSTWOOD_PLANKS_ITEM.get()), 'F', tag(ItemTagKeys.GEMS_FLUORITE)), " V ", "MFM", "NVN");
        shaped(output, BlockInit.MANA_PUMP_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'I', i(ItemInit.FLUXIUM_INGOT.get()), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), 'F', i(ItemInit.HIGH_PURITY_FLUORITE.get())), "IAI", "NFN", "IAI");
        shaped(output, BlockInit.MANA_RECEIVER_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'I', i(ItemInit.FLUXIUM_INGOT.get()), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), 'F', i(ItemInit.HIGH_PURITY_FLUORITE.get())), "   ", "IFI", "ANA");
        shaped(output, BlockInit.MANA_RELAY_ITEM.get(), 1, keys('N', i(BlockInit.MANA_NODE_ITEM.get()), 'V', i(BlockInit.VESPERITE_ITEM.get()), 'M', i(BlockInit.MYSTWOOD_PLANKS_ITEM.get())), " N ", "MVM");
        shaped(output, BlockInit.MANA_STRANDER_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'I', i(ItemInit.FLUXIUM_INGOT.get()), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get()), '#', i(BlockInit.ENHANCED_MANA_NODE_ITEM.get())), " # ", "IAI", "ANA");
        shaped(output, BlockInit.MANA_VESSEL_ITEM.get(), 1, keys('N', i(Items.GOLD_NUGGET), 'V', i(BlockInit.VESPERITE_ITEM.get()), 'M', i(BlockInit.MYSTWOOD_PLANKS_ITEM.get()), 'F', i(BlockInit.FLUORITE_BLOCK_ITEM.get())), "VMV", "NFN", "VMV");
        shaped(output, BlockInit.PEDESTAL_PYLON_ITEM.get(), 1, keys('V', i(BlockInit.VESPERITE_ITEM.get()), 'M', i(BlockInit.MYSTWOOD_PLANKS_ITEM.get())), "VMV", " M ", "MVM");
        shaped(output, ItemInit.MATERIALS_AND_TOOLCRAFT_DESIGN.get(), 1, keys('B', i(Items.BOOK), 'S', component(ItemInit.REINFORCED_ROD.get(), MaterialInit.IRON.get(), false), 'P', component(ItemInit.PLATE.get(), MaterialInit.STONE.get(), false), 'L', tag(ItemTagKeys.STRIPPED_LOGS)), "PLP", "SBS", "PLP");
        shaped(output, ItemInit.SULFURIC_ACID_BATTERY.get(), 1, keys('C', component(ItemInit.REINFORCED_ROD.get(), MaterialInit.COPPER.get(), true), 'Z', component(ItemInit.REINFORCED_ROD.get(), MaterialInit.ZINC.get(), true), 'S', i(ItemInit.SULFURIC_ACID_FLASK.get())), "C ", "SZ");
        shaped(output, ItemInit.TOOL_BELT.get(), 1, keys('P', component(ItemInit.PLATE.get(), MaterialInit.IRON.get(), true), 'L', i(Items.LEATHER)), " LP", "L L", "PL ");
        shaped(output, BlockInit.TOOL_HANGER_ITEM.get(), 2, keys('#', tag(ItemTagKeys.STRIPPED_LOGS), 'S', tag(ItemTagKeys.STONES), 'P', ItemTags.PLANKS), "SPS", "## ", "S  ");
        shaped(output, BlockInit.TRAP_HATCH_ITEM.get(), 2, keys('I', i(ItemInit.FLUXIUM_INGOT.get()), 'A', i(BlockInit.ALCHECRYSITE_ITEM.get())), "IAI", "IAI");
        shaped(output, BlockInit.ZARDIUS_CRUCIBLE_ITEM.get(), 1, RecipeCategory.REDSTONE, "wooden_door", keys('#', tag(ItemTagKeys.GEMS_TOURMALINE), 'I', i(Items.IRON_INGOT)), "I I", "I#I", "III");
        shaped(output, BlockInit.FLUORITE_BLOCK_ITEM.get(), 1, keys('#', tag(ItemTagKeys.GEMS_FLUORITE)), "###", "###", "###");
        shaped(output, ItemInit.FLUORITE.get(), "_from_fluorite_block", 9, keys('#', i(BlockInit.FLUORITE_BLOCK_ITEM.get())), "#");
        shaped(output, ItemInit.FLUXIUM_INGOT.get(), "_from_nugget", 1, keys('#', tag(ItemTagKeys.NUGGETS_FLUXIUM)), "###", "###", "###");
        shaped(output, ItemInit.FLUXIUM_NUGGET.get(), 9, keys('#', tag(ItemTagKeys.INGOTS_FLUXIUM)), "#");
        shaped(output, ItemInit.FLUXIUM_RING.get(), 1, keys('I', i(ItemInit.FLUXIUM_INGOT.get()), 'N', i(ItemInit.FLUXIUM_NUGGET.get())), " IN", "I I", "NI ");
        shaped(output, BlockInit.RAW_ZINC_BLOCK_ITEM.get(), 1, keys('#', tag(ItemTagKeys.RAW_MATERIALS_ZINC)), "###", "###", "###");
        shaped(output, BlockInit.SULFUR_BLOCK_ITEM.get(), 1, RecipeCategory.BUILDING_BLOCKS, "", keys('#', tag(ItemTagKeys.GEMS_SULFUR)), "##", "##");
        shapeless(output, stack(ItemInit.GLISTENING_LEXICON.get(), 1), i(Items.BOOK), tag(ItemTagKeys.GEMS_FLUORITE));
        shapeless(output, stack(ItemInit.MANA_PIE.get(), 1), i(Items.WHEAT), i(Items.WHEAT), i(Items.SUGAR), i(ItemInit.MANA_BERRIES.get()));
        shapeless(output, stack(Items.GUNPOWDER, 2), "_from_sulfur", tag(ItemTagKeys.GEMS_SULFUR), i(Items.CHARCOAL), i(Items.BONE_MEAL));
    }

    private static void buildCustom(RecipeOutput output) {
        cooking(output, ItemInit.CITRINE.get(), i(Items.AMETHYST_SHARD), 0, 200, false, "", "misc");
        cooking(output, ItemInit.FLUORITE.get(), i(BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM.get()), 1, 200, false, "_from_deepslate_fluorite_ore_smelting", "");
        cooking(output, ItemInit.FLUORITE.get(), i(BlockInit.FLUORITE_ORE_ITEM.get()), 1, 200, false, "_from_fluorite_ore_smelting", "");
        cooking(output, ItemInit.TOURMALINE.get(), i(BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM.get()), 1, 200, false, "_from_deepslate_tourmaline_ore_smelting", "");
        cooking(output, ItemInit.TOURMALINE.get(), i(BlockInit.TOURMALINE_ORE_ITEM.get()), 1, 200, false, "_from_tourmaline_ore_smelting", "");
        cooking(output, ItemInit.ZINC_INGOT.get(), i(ItemInit.RAW_ZINC.get()), 0, 200, false, "_from_raw_zinc_smelting", "misc");
        cooking(output, ItemInit.FLUORITE.get(), i(BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM.get()), 1, 100, true, "_from_deepslate_fluorite_ore_blasting", "");
        cooking(output, ItemInit.FLUORITE.get(), i(BlockInit.FLUORITE_ORE_ITEM.get()), 1, 100, true, "_from_fluorite_ore_blasting", "");
        cooking(output, ItemInit.TOURMALINE.get(), i(BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM.get()), 1, 100, true, "_from_deepslate_tourmaline_ore_blasting", "");
        cooking(output, ItemInit.TOURMALINE.get(), i(BlockInit.TOURMALINE_ORE_ITEM.get()), 1, 100, true, "_from_tourmaline_ore_blasting", "");
        cooking(output, ItemInit.ZINC_INGOT.get(), i(ItemInit.RAW_ZINC.get()), 0, 200, true, "_from_raw_zinc_blasting", "misc");

        custom(output, "compressing", ItemInit.RESTRAINT_QUARTZ.get(), new CompressingRecipe("", sized(i(ItemInit.RESTRAINT_QUARTZ_DUST.get()), 4), stack(ItemInit.RESTRAINT_QUARTZ.get(), 1)));
        custom(output, "crushing", Items.AMETHYST_SHARD, new CrushingRecipe("", sized(i(Items.AMETHYST_BLOCK), 1), stack(Items.AMETHYST_SHARD, 4)));
        custom(output, "crushing", Items.REDSTONE, new CrushingRecipe("", sized(i(ItemInit.REDSTONE_CRYSTAL.get()), 1), stack(Items.REDSTONE, 3)));
        custom(output, "crushing", ItemInit.RESTRAINT_QUARTZ_DUST.get(), new CrushingRecipe("", sized(i(ItemInit.QUARTZ_PLANT.get()), 4), stack(ItemInit.RESTRAINT_QUARTZ_DUST.get(), 1)));
        custom(output, "crushing", ItemInit.SULFUR.get(), new CrushingRecipe("", sized(i(BlockInit.SULFUR_BLOCK_ITEM.get()), 1), stack(ItemInit.SULFUR.get(), 3)));

        infusion(output, ItemInit.AETHER_LIFTER.get(), i(Items.IRON_BOOTS), 1, 500000, 1, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 16), s(ItemInit.HOLLOW_CRYSTAL.get(), 16), s(ItemInit.PHANTOM_CRYSTAL.get(), 16), s(ItemInit.AEGIS_WEAVE.get(), 8), s(ItemInit.FLUXIUM_INGOT.get(), 16));
        infusion(output, ItemInit.FLAMGLIDE_STRIDER.get(), i(Items.LEATHER_BOOTS), 1, 500000, 1, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 16), s(ItemInit.EMBER_CRYSTAL.get(), 16), s(ItemInit.FLOW_CRYSTAL.get(), 16), s(ItemInit.AEGIS_WEAVE.get(), 8), s(ItemInit.FLUXIUM_INGOT.get(), 16));
        infusion(output, ItemInit.FLUXIUM_INGOT.get(), i(Items.IRON_INGOT), 4, 10000, 8, s(tag(ItemTagKeys.AGGREGATED_STRAND), 1), s(tag(ItemTagKeys.INGOTS_ZINC), 8));
        infusion(output, ItemInit.HIGH_PURITY_FLUORITE.get(), "_infuser", i(ItemInit.MANA_CHARGED_FLUORITE.get()), 2, 10000, 1);
        infusion(output, ItemInit.MANA_CHARGED_FLUORITE.get(), "_infuser", tag(ItemTagKeys.GEMS_FLUORITE), 1, 4000, 1);
        infusion(output, BlockInit.ALCHECRYSITE_ITEM.get(), "_infuser", i(BlockInit.VESPERITE_ITEM.get()), 8, 10000, 8, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 4), s(tag(ItemTagKeys.GEMS_CITRINE), 2), s(tag(ItemTagKeys.GEMS_TOURMALINE), 4), s(Items.GOLD_INGOT, 1));

        infusion(output, "infusion/accesories", ItemInit.ARDOR_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.EMBER_CRYSTAL.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.CELERITAS_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.PHANTOM_CRYSTAL.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.CHARGEBIND_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.SURGE_CRYSTAL.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.CRACK_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.TREMOR_CRYSTAL.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.DAWN_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.AGGREGATED_LUMINIS.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.DISTORTION_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.HOLLOW_CRYSTAL.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.FLUXBOUND_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.AGGREGATED_FLUXIA.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.MANA_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.PROTECTION_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.MAGIC_CRYSTAL.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.QUENCH_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.GLACE_CRYSTAL.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.UMBRAL_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.AGGREGATED_NOCTIS.get(), 4));
        infusion(output, "infusion/accesories", ItemInit.UPDRAFT_RING.get(), i(ItemInit.FLUXIUM_RING.get()), 1, 40000, 1, s(ItemInit.FLOW_CRYSTAL.get(), 4));

        Ingredient aspectCrystalBase = tag(ItemTagKeys.ASPECT_CRYSTAL_BASE);
        infusion(output, "infusion/aspect_crystal", ItemInit.EMBER_CRYSTAL.get(), "_infuser_from_blaze", aspectCrystalBase, 1, 40000, 1, s(Items.BLAZE_ROD, 1));
        infusion(output, "infusion/aspect_crystal", ItemInit.EMBER_CRYSTAL.get(), "_infuser_from_citrine", aspectCrystalBase, 1, 40000, 1, s(tag(ItemTagKeys.GEMS_CITRINE), 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.EMBER_CRYSTAL.get(), "_infuser_from_coal", aspectCrystalBase, 1, 40000, 1, s(ItemTags.COALS, 8));
        infusion(output, "infusion/aspect_crystal", ItemInit.EMBER_CRYSTAL.get(), "_infuser_from_gunpowder", aspectCrystalBase, 1, 40000, 1, s(Items.GUNPOWDER, 2));
        infusion(output, "infusion/aspect_crystal", ItemInit.EMBER_CRYSTAL.get(), "_infuser_from_lava", aspectCrystalBase, 2, 80000, 2, s(Items.LAVA_BUCKET, 1));
        infusion(output, "infusion/aspect_crystal", ItemInit.FLOW_CRYSTAL.get(), "_infuser_from_flower", aspectCrystalBase, 1, 40000, 1, s(ItemTags.FLOWERS, 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.FLOW_CRYSTAL.get(), "_infuser_from_moss", aspectCrystalBase, 1, 40000, 1, s(Items.MOSS_BLOCK, 8));
        infusion(output, "infusion/aspect_crystal", ItemInit.FLOW_CRYSTAL.get(), "_infuser_from_sapling", aspectCrystalBase, 1, 40000, 1, s(ItemTags.SAPLINGS, 8));
        infusion(output, "infusion/aspect_crystal", ItemInit.FLOW_CRYSTAL.get(), "_infuser_from_water", aspectCrystalBase, 1, 40000, 1, s(Items.WATER_BUCKET, 1));
        infusion(output, "infusion/aspect_crystal", ItemInit.GLACE_CRYSTAL.get(), "_infuser_from_calcite", aspectCrystalBase, 1, 40000, 1, s(Items.CALCITE, 2));
        infusion(output, "infusion/aspect_crystal", ItemInit.GLACE_CRYSTAL.get(), "_infuser_from_ice", aspectCrystalBase, 1, 40000, 1, s(Items.ICE, 2));
        infusion(output, "infusion/aspect_crystal", ItemInit.GLACE_CRYSTAL.get(), "_infuser_from_quartz", aspectCrystalBase, 1, 40000, 1, s(Items.QUARTZ, 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.GLACE_CRYSTAL.get(), "_infuser_from_snow", aspectCrystalBase, 1, 40000, 1, s(Items.SNOW_BLOCK, 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.HOLLOW_CRYSTAL.get(), "_infuser_from_chorus", aspectCrystalBase, 1, 40000, 1, s(Items.CHORUS_FRUIT, 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.HOLLOW_CRYSTAL.get(), "_infuser_from_end_stone", aspectCrystalBase, 1, 40000, 1, s(Items.END_STONE, 8));
        infusion(output, "infusion/aspect_crystal", ItemInit.HOLLOW_CRYSTAL.get(), "_infuser_from_ender_pearl", aspectCrystalBase, 1, 40000, 1, s(Items.ENDER_PEARL, 2));
        infusion(output, "infusion/aspect_crystal", ItemInit.MAGIC_CRYSTAL.get(), "_infuser_from_amethyst", aspectCrystalBase, 1, 40000, 1, s(Items.AMETHYST_SHARD, 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.MAGIC_CRYSTAL.get(), "_infuser_from_gold", aspectCrystalBase, 1, 40000, 1, s(Items.GOLD_INGOT, 1));
        infusion(output, "infusion/aspect_crystal", ItemInit.PHANTOM_CRYSTAL.get(), "_infuser_from_glow_berries", aspectCrystalBase, 1, 40000, 1, s(Items.GLOW_BERRIES, 8));
        infusion(output, "infusion/aspect_crystal", ItemInit.PHANTOM_CRYSTAL.get(), "_infuser_from_glowstone", aspectCrystalBase, 1, 40000, 1, s(Items.GLOWSTONE_DUST, 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.PHANTOM_CRYSTAL.get(), "_infuser_from_phantom", aspectCrystalBase, 1, 40000, 1, s(Items.PHANTOM_MEMBRANE, 2));
        infusion(output, "infusion/aspect_crystal", ItemInit.SURGE_CRYSTAL.get(), "_infuser_from_copper", aspectCrystalBase, 1, 40000, 1, s(Items.COPPER_INGOT, 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.SURGE_CRYSTAL.get(), "_infuser_from_redstone", aspectCrystalBase, 1, 40000, 1, s(Items.REDSTONE, 4));
        infusion(output, "infusion/aspect_crystal", ItemInit.TREMOR_CRYSTAL.get(), "_infuser_from_deepslate", aspectCrystalBase, 1, 40000, 1, s(Items.COBBLED_DEEPSLATE, 8));
        infusion(output, "infusion/aspect_crystal", ItemInit.TREMOR_CRYSTAL.get(), "_infuser_from_lapis", aspectCrystalBase, 1, 40000, 1, s(Items.LAPIS_LAZULI, 4));

        infusion(output, "infusion/machines", BlockInit.EMBER_SMELTER_ITEM.get(), i(Items.BLAST_FURNACE), 1, 100000, 1, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 4), s(BlockInit.ALCHECRYSITE_ITEM.get(), 2), s(ItemInit.FLUXIUM_INGOT.get(), 4), s(ItemInit.EMBER_CRYSTAL.get(), 2));
        infusion(output, "infusion/machines", BlockInit.ENHANCED_MANA_NODE_ITEM.get(), i(BlockInit.MANA_NODE_ITEM.get()), 1, 100000, 1, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 2), s(BlockInit.ALCHECRYSITE_ITEM.get(), 1), s(ItemInit.FLUXIUM_INGOT.get(), 2));
        infusion(output, "infusion/machines", BlockInit.ENHANCED_MANA_RELAY_ITEM.get(), i(BlockInit.MANA_RELAY_ITEM.get()), 1, 100000, 1, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 2), s(BlockInit.ALCHECRYSITE_ITEM.get(), 1), s(ItemInit.FLUXIUM_INGOT.get(), 2));
        infusion(output, "infusion/machines", BlockInit.ENHANCED_MANA_VESSEL_ITEM.get(), i(BlockInit.MANA_VESSEL_ITEM.get()), 1, 100000, 1, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 4), s(BlockInit.ALCHECRYSITE_ITEM.get(), 4), s(ItemInit.FLUXIUM_INGOT.get(), 4));
        infusion(output, "infusion/machines", BlockInit.INFUSER_ITEM.get(), i(BlockInit.INFUSION_ALTAR_ITEM.get()), 1, 40000, 1, s(ItemInit.HIGH_PURITY_FLUORITE.get(), 8), s(BlockInit.ALCHECRYSITE_ITEM.get(), 4), s(ItemInit.FLUXIUM_INGOT.get(), 8));

        custom(output, "part_cutting", ItemInit.CATALYST.get(), "_part_cutting", new PartCuttingRecipe("", 1, stack(ItemInit.CATALYST.get(), 1)));
        custom(output, "part_cutting", ItemInit.CONDUCTOR.get(), "_part_cutting", new PartCuttingRecipe("", 2, stack(ItemInit.CONDUCTOR.get(), 1)));
        custom(output, "part_cutting", ItemInit.HANDGUARD.get(), "_part_cutting", new PartCuttingRecipe("", 1, stack(ItemInit.HANDGUARD.get(), 1)));
        custom(output, "part_cutting", ItemInit.HEAVY_BLADE.get(), "_part_cutting", new PartCuttingRecipe("", 1, stack(ItemInit.HEAVY_BLADE.get(), 1)));
        custom(output, "part_cutting", ItemInit.HEAVY_HANDLE.get(), "_part_cutting", new PartCuttingRecipe("", 2, stack(ItemInit.HEAVY_HANDLE.get(), 1)));
        custom(output, "part_cutting", ItemInit.LIGHT_BLADE.get(), "_part_cutting", new PartCuttingRecipe("", 2, stack(ItemInit.LIGHT_BLADE.get(), 1)));
        custom(output, "part_cutting", ItemInit.LIGHT_HANDLE.get(), "_part_cutting", new PartCuttingRecipe("", 1, stack(ItemInit.LIGHT_HANDLE.get(), 1)));
        custom(output, "part_cutting", ItemInit.PLATE.get(), "_part_cutting", new PartCuttingRecipe("", 2, stack(ItemInit.PLATE.get(), 1)));
        custom(output, "part_cutting", ItemInit.REINFORCED_ROD.get(), "_part_cutting", new PartCuttingRecipe("", 2, stack(ItemInit.REINFORCED_ROD.get(), 1)));
        custom(output, "part_cutting", ItemInit.SPIKE_HEAD.get(), "_part_cutting", new PartCuttingRecipe("", 2, stack(ItemInit.SPIKE_HEAD.get(), 1)));
        custom(output, "part_cutting", ItemInit.STRIKE_HEAD.get(), "_part_cutting", new PartCuttingRecipe("", 3, stack(ItemInit.STRIKE_HEAD.get(), 1)));
        custom(output, "part_cutting", ItemInit.TOOL_BINDING.get(), "_part_cutting", new PartCuttingRecipe("", 1, stack(ItemInit.TOOL_BINDING.get(), 1)));

        material(output, i(BlockInit.ALCHECRYSITE_ITEM.get()), MaterialInit.ALCHECRYSITE);
        material(output, i(Items.AMETHYST_SHARD), MaterialInit.AMETHYST);
        material(output, i(Items.BASALT), MaterialInit.BASALT);
        material(output, i(Items.BONE), MaterialInit.BONE);
        material(output, i(Items.BRICK), MaterialInit.BRICK);
        material(output, i(Items.CALCITE), MaterialInit.CALCITE);
        material(output, i(ItemInit.CITRINE.get()), MaterialInit.CITRINE);
        material(output, i(Items.COPPER_INGOT), MaterialInit.COPPER);
        material(output, i(Items.DEEPSLATE), MaterialInit.DEEPSLATE);
        material(output, i(Items.DIAMOND), MaterialInit.DIAMOND);
        material(output, i(Items.DRIPSTONE_BLOCK), MaterialInit.DRIPSTONE);
        material(output, i(Items.EMERALD), MaterialInit.EMERALD);
        material(output, i(ItemInit.ENDER_METAL_INGOT.get()), MaterialInit.ENDER_METAL);
        material(output, i(Items.END_STONE), MaterialInit.END_STONE);
        material(output, tag(ItemTagKeys.GEMS_FLUORITE), MaterialInit.FLUORITE);
        material(output, i(ItemInit.FLUXIUM_INGOT.get()), MaterialInit.FLUXIUM);
        material(output, i(Items.GLASS), MaterialInit.GLASS);
        material(output, i(Items.GLOWSTONE), MaterialInit.GLOWSTONE);
        material(output, i(Items.GOLD_INGOT), MaterialInit.GOLD);
        material(output, i(Items.HONEYCOMB), MaterialInit.HONEYCOMB);
        material(output, i(Items.IRON_INGOT), MaterialInit.IRON);
        material(output, i(Items.LAPIS_LAZULI), MaterialInit.LAPIS);
        material(output, i(Items.MOSS_BLOCK), MaterialInit.MOSS);
        material(output, i(Items.NETHERITE_INGOT), MaterialInit.NETHERITE);
        material(output, i(Items.NETHER_BRICK), MaterialInit.NETHER_BRICK);
        material(output, i(Items.OBSIDIAN), MaterialInit.OBSIDIAN);
        material(output, i(Items.PHANTOM_MEMBRANE), MaterialInit.PHANTOM_MEMBRANE);
        material(output, i(Items.QUARTZ), MaterialInit.QUARTZ);
        material(output, i(ItemInit.RADIANT_STEEL_INGOT.get()), MaterialInit.RADIANT_STEEL);
        material(output, tag(ItemTagKeys.GEMS_REDSTONE_CRYSTAL), MaterialInit.REDSTONE);
        material(output, i(Items.SANDSTONE), MaterialInit.SANDSTONE);
        material(output, i(Items.SLIME_BALL), MaterialInit.SLIME);
        material(output, i(Items.SNOW_BLOCK), MaterialInit.SNOW);
        material(output, i(Items.STONE), MaterialInit.STONE);
        material(output, i(ItemInit.SULFURIC_ACID_BATTERY.get()), MaterialInit.SULFURIC_ACID_BATTERY);
        material(output, tag(ItemTagKeys.GEMS_TOURMALINE), MaterialInit.TOURMALINE);
        material(output, tag(ItemTagKeys.STRIPPED_LOGS), MaterialInit.WOOD);
        material(output, tag(ItemTagKeys.INGOTS_ZINC), MaterialInit.ZINC);

        spell(output, i(Items.PHANTOM_MEMBRANE), SpellInit.ENERCRUX.get(), ItemInit.ALCHAEFABRIC.get());
        spell(output, tag(ItemTagKeys.GEMS_FLUORITE), SpellInit.ENERCRUX.get(), ItemInit.MANA_CHARGED_FLUORITE.get());
        spell(output, Ingredient.of(ItemTags.LOGS), SpellInit.ENERCRUX.get(), BlockInit.MYSTWOOD_LOG_ITEM.get());
        spell(output, tag(ItemTagKeys.STONES), SpellInit.ENERCRUX.get(), BlockInit.VESPERITE_ITEM.get());

        custom(output, "tool_assemble", ItemInit.AXE.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.TOOL_BINDING.get()), i(ItemInit.STRIKE_HEAD.get()), i(ItemInit.LIGHT_BLADE.get()), i(ItemInit.HEAVY_HANDLE.get())), stack(ItemInit.AXE.get(), 1)));
        custom(output, "tool_assemble", ItemInit.DAGGER.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.HANDGUARD.get()), i(ItemInit.LIGHT_BLADE.get()), i(ItemInit.LIGHT_HANDLE.get())), stack(ItemInit.DAGGER.get(), 1)));
        custom(output, "tool_assemble", ItemInit.HAMMER.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.TOOL_BINDING.get()), i(ItemInit.PLATE.get()), i(ItemInit.STRIKE_HEAD.get()), i(ItemInit.HEAVY_HANDLE.get())), stack(ItemInit.HAMMER.get(), 1)));
        custom(output, "tool_assemble", ItemInit.HEAVY_SWORD.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.HANDGUARD.get()), i(ItemInit.TOOL_BINDING.get()), i(ItemInit.HEAVY_BLADE.get()), i(ItemInit.LIGHT_HANDLE.get())), stack(ItemInit.HEAVY_SWORD.get(), 1)));
        custom(output, "tool_assemble", ItemInit.LIGHT_SWORD.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.TOOL_BINDING.get()), i(ItemInit.HANDGUARD.get()), i(ItemInit.LIGHT_BLADE.get()), i(ItemInit.LIGHT_HANDLE.get())), stack(ItemInit.LIGHT_SWORD.get(), 1)));
        custom(output, "tool_assemble", ItemInit.PICKAXE.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.TOOL_BINDING.get()), i(ItemInit.SPIKE_HEAD.get()), i(ItemInit.HEAVY_HANDLE.get())), stack(ItemInit.PICKAXE.get(), 1)));
        custom(output, "tool_assemble", ItemInit.SCYTHE.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.TOOL_BINDING.get()), i(ItemInit.HEAVY_BLADE.get()), i(ItemInit.HEAVY_HANDLE.get()), i(ItemInit.REINFORCED_ROD.get())), stack(ItemInit.SCYTHE.get(), 1)));
        custom(output, "tool_assemble", ItemInit.SHOVEL.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.TOOL_BINDING.get()), i(ItemInit.PLATE.get()), i(ItemInit.LIGHT_BLADE.get()), i(ItemInit.HEAVY_HANDLE.get())), stack(ItemInit.SHOVEL.get(), 1)));
        custom(output, "tool_assemble", ItemInit.STAFF.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.TOOL_BINDING.get()), i(ItemInit.CONDUCTOR.get()), i(ItemInit.HEAVY_HANDLE.get()), i(ItemInit.CATALYST.get())), stack(ItemInit.STAFF.get(), 1)));
        custom(output, "tool_assemble", ItemInit.WAND.get(), new ToolAssemblyRecipe("", List.of(i(ItemInit.TOOL_BINDING.get()), i(ItemInit.CONDUCTOR.get()), i(ItemInit.LIGHT_HANDLE.get()), i(ItemInit.CATALYST.get())), stack(ItemInit.WAND.get(), 1)));

        crucible(output, List.of(s(ItemInit.ALCHAEFABRIC.get(), 1), s(Items.OBSIDIAN, 1), s(ItemInit.GLACE_CRYSTAL.get(), 1), s(ItemInit.PHANTOM_CRYSTAL.get(), 1), s(Items.CHAIN, 1), s(Items.GOLD_INGOT, 2), s(Items.DIAMOND, 1)), Fluids.LAVA, 2000, 100000, stack(ItemInit.AEGIS_WEAVE.get(), 3), Optional.empty());
        crucible(output, List.of(s(Items.ENDER_PEARL, 1), s(Items.IRON_INGOT, 2), s(Items.GOLD_INGOT, 1), s(Items.COPPER_INGOT, 1), s(ItemInit.HOLLOW_CRYSTAL.get(), 1)), FluidInit.HOLLOW_POTION.get(), 250, 50000, stack(ItemInit.ENDER_METAL_INGOT.get(), 5), Optional.empty());
        crucible(output, List.of(s(tag(ItemTagKeys.GEMS_MANA_CHARGED_FLUORITE), 2), s(ItemInit.TOURMALINE.get(), 4), s(Items.GLASS, 4)), FluidInit.MANA_POTION.get(), 1000, 10000, stack(BlockInit.MANA_INSULATING_GLASS_ITEM.get(), 6), Optional.empty());
        crucible(output, List.of(s(ItemInit.MANA_BERRIES.get(), 2)), Fluids.WATER, 1000, 20000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.MANA_POTION.get(), 1000)));
        crucible(output, List.of(s(Items.NETHER_STAR, 1), s(Items.GUNPOWDER, 16), s(ItemInit.EMBER_CRYSTAL.get(), 8), s(ItemInit.MAGIC_CRYSTAL.get(), 8), s(ItemInit.TREMOR_CRYSTAL.get(), 8)), Fluids.LAVA, 2000, 10000, stack(ItemInit.NETHER_STAR_BRILLIANCE.get(), 4), Optional.empty());
        crucible(output, List.of(s(ItemInit.NETHER_STAR_BRILLIANCE.get(), 1), s(Items.GLOWSTONE, 1), s(Items.GLOW_INK_SAC, 8), s(Items.GOLD_INGOT, 1), s(Items.DIAMOND, 4), s(ItemInit.GLACE_CRYSTAL.get(), 4)), Fluids.WATER, 2000, 40000, stack(ItemInit.RADIANT_STEEL_INGOT.get(), 2), Optional.empty());
        crucible(output, List.of(s(BlockInit.CELIFERN_PLANKS_ITEM.get(), 1), s(tag(ItemTagKeys.GEMS_SULFUR), 2)), FluidInit.MANA_POTION.get(), 1000, 10000, stack(Items.CHARCOAL, 1), Optional.of(new FluidStack(FluidInit.SULFURIC_ACID.get(), 1000)));
        crucible(output, List.of(s(Items.BOOK, 1), s(Items.GOLD_INGOT, 1), s(tag(ItemTagKeys.GEMS_FLUORITE), 8), s(BlockInit.ALCHECRYSITE_ITEM.get(), 8), s(tag(ItemTagKeys.GEMS_TOURMALINE), 1)), Fluids.WATER, 2000, 40000, stack(ItemInit.THE_FIRE_THAT_THINKS.get(), 1), Optional.empty());
        crucible(output, List.of(s(ItemInit.AGGREGATED_FLUXIA.get(), 1), s(ItemInit.EMBER_CRYSTAL.get(), 1), s(ItemTags.COALS, 1), s(tag(ItemTagKeys.GEMS_CITRINE), 1)), FluidInit.MANA_POTION.get(), 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.EMBER_POTION.get(), 1000)));
        crucible(output, List.of(s(ItemInit.AGGREGATED_LUMINIS.get(), 1), s(ItemInit.FLOW_CRYSTAL.get(), 1), s(ItemTags.SAPLINGS, 1), s(Items.DRIPSTONE_BLOCK, 1)), FluidInit.MANA_POTION.get(), 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.FLOW_POTION.get(), 1000)));
        crucible(output, List.of(s(ItemInit.AGGREGATED_FLUXIA.get(), 1), s(ItemInit.GLACE_CRYSTAL.get(), 1), s(Items.CALCITE, 1), s(Items.QUARTZ, 1)), FluidInit.MANA_POTION.get(), 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.GLACE_POTION.get(), 1000)));
        crucible(output, List.of(s(ItemInit.AGGREGATED_FLUXIA.get(), 1), s(tag(ItemTagKeys.FOODS_RAW_MEAT), 1), s(ItemInit.MANA_BERRIES.get(), 1)), Fluids.WATER, 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.HEALING_POTION.get(), 1000)));
        crucible(output, List.of(s(ItemInit.AGGREGATED_NOCTIS.get(), 1), s(ItemInit.HOLLOW_CRYSTAL.get(), 1), s(Items.ENDER_PEARL, 1), s(Items.WARPED_FUNGUS, 1)), FluidInit.MANA_POTION.get(), 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.HOLLOW_POTION.get(), 1000)));
        crucible(output, List.of(s(ItemInit.AGGREGATED_FLUXIA.get(), 1), s(ItemInit.MAGIC_CRYSTAL.get(), 1), s(tag(ItemTagKeys.GEMS_AMETHYST), 1), s(tag(ItemTagKeys.INGOTS_GOLD), 1)), FluidInit.MANA_POTION.get(), 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.MAGIC_POTION.get(), 1000)));
        crucible(output, List.of(s(ItemInit.AGGREGATED_LUMINIS.get(), 1), s(ItemInit.PHANTOM_CRYSTAL.get(), 1), s(Items.PHANTOM_MEMBRANE, 1), s(tag(ItemTagKeys.GEMS_FLUORITE), 1)), FluidInit.MANA_POTION.get(), 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.PHANTOM_POTION.get(), 1000)));
        crucible(output, List.of(s(ItemInit.AGGREGATED_LUMINIS.get(), 1), s(ItemInit.SURGE_CRYSTAL.get(), 1), s(tag(ItemTagKeys.INGOTS_COPPER), 1), s(tag(ItemTagKeys.GEMS_REDSTONE_CRYSTAL), 1)), FluidInit.MANA_POTION.get(), 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.SURGE_POTION.get(), 1000)));
        crucible(output, List.of(s(ItemInit.AGGREGATED_NOCTIS.get(), 1), s(ItemInit.TREMOR_CRYSTAL.get(), 1), s(Items.DEEPSLATE, 1), s(tag(ItemTagKeys.GEMS_LAPIS), 1)), FluidInit.MANA_POTION.get(), 1000, 10000, ItemStack.EMPTY, Optional.of(new FluidStack(FluidInit.TREMOR_POTION.get(), 1000)));
    }

    private static void shaped(RecipeOutput output, ItemLike result, int count, java.util.Map<Character, Ingredient> keys, String... pattern) {
        shaped(output, result, "", count, RecipeCategory.MISC, "", keys, pattern);
    }

    private static void shaped(RecipeOutput output, ItemLike result, String suffix, int count, java.util.Map<Character, Ingredient> keys, String... pattern) {
        shaped(output, result, suffix, count, RecipeCategory.MISC, "", keys, pattern);
    }

    private static void shaped(RecipeOutput output, ItemLike result, int count, RecipeCategory category, String group, java.util.Map<Character, Ingredient> keys, String... pattern) {
        shaped(output, result, "", count, category, group, keys, pattern);
    }

    private static void shaped(RecipeOutput output, ItemLike result, String suffix, int count, RecipeCategory category, String group, java.util.Map<Character, Ingredient> keys, String... pattern) {
        var builder = new ShapedRecipeBuilder(category, new ItemStack(result, count)).group(group);
        for (String row : pattern) builder.pattern(row);
        keys.forEach(builder::define);
        builder.unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(keys.values().iterator().next())).save(output, recipeId("crafting", result, suffix));
    }

    private static void shapeless(RecipeOutput output, ItemStack result, Ingredient... ingredients) {
        shapeless(output, result, "", ingredients);
    }

    private static void shapeless(RecipeOutput output, ItemStack result, String suffix, Ingredient... ingredients) {
        var builder = new ShapelessRecipeBuilder(RecipeCategory.MISC, result);
        for (Ingredient ingredient : ingredients) builder.requires(ingredient);
        builder.unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ingredients[0])).save(output, recipeId("crafting", result.getItem(), suffix));
    }

    private static void cooking(RecipeOutput output, ItemLike result, Ingredient ingredient, float experience, int time, boolean blasting) {
        cooking(output, result, ingredient, experience, time, blasting, "", "");
    }

    private static void cooking(RecipeOutput output, ItemLike result, Ingredient ingredient, float experience, int time, boolean blasting, String group) {
        cooking(output, result, ingredient, experience, time, blasting, "", group);
    }

    private static void cooking(RecipeOutput output, ItemLike result, Ingredient ingredient, float experience, int time, boolean blasting, String suffix, String group) {
        var builder = blasting ? SimpleCookingRecipeBuilder.blasting(ingredient, RecipeCategory.MISC, result, experience, time) : SimpleCookingRecipeBuilder.smelting(ingredient, RecipeCategory.MISC, result, experience, time);
        builder.group(group);
        String folder = blasting ? "blasting" : "smelting";
        builder.unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ingredient)).save(output, recipeId(folder, result, suffix));
    }

    private static void infusion(RecipeOutput output, ItemLike result, Ingredient base, int baseCount, long mana, int resultCount, SizedIngredient... ingredients) {
        infusion(output, "infusion", result, "", base, baseCount, mana, resultCount, ingredients);
    }

    private static void infusion(RecipeOutput output, ItemLike result, String suffix, Ingredient base, int baseCount, long mana, int resultCount, SizedIngredient... ingredients) {
        infusion(output, "infusion", result, suffix, base, baseCount, mana, resultCount, ingredients);
    }

    private static void infusion(RecipeOutput output, String folder, ItemLike result, Ingredient base, int baseCount, long mana, int resultCount, SizedIngredient... ingredients) {
        infusion(output, folder, result, "", base, baseCount, mana, resultCount, ingredients);
    }

    private static void infusion(RecipeOutput output, String folder, ItemLike result, String suffix, Ingredient base, int baseCount, long mana, int resultCount, SizedIngredient... ingredients) {
        custom(output, recipeId(folder, result, suffix), new InfusionRecipe("", sized(base, baseCount), List.of(ingredients), mana, stack(result, resultCount)));
    }

    private static void material(RecipeOutput output, Ingredient ingredient, net.stln.magitech.registry.DeferredToolMaterial<?> material) {
        custom(output, Magitech.id("part_material/" + material.getId().getPath() + "_material"), new ToolMaterialRecipe("", ingredient, material.get()));
    }

    private static void spell(RecipeOutput output, Ingredient ingredient, net.stln.magitech.feature.magic.spell.ISpell spell, ItemLike result) {
        custom(output, "spell_conversion", result, new SpellConversionRecipe("", ingredient, spell, stack(result, 1)));
    }

    private static void crucible(RecipeOutput output, List<SizedIngredient> ingredients, Fluid fluid, int amount, long mana, ItemStack result, Optional<FluidStack> fluidResult) {
        Recipe<?> recipe = new ZardiusCrucibleRecipe("", ingredients, SizedFluidIngredient.of(fluid, amount), mana, result.isEmpty() ? Optional.empty() : Optional.of(result), fluidResult);
        ResourceLocation id = fluidResult.map(stack -> recipeId("zardius_crucible", stack.getFluid())).orElseGet(() -> recipeId("zardius_crucible", result.getItem()));
        custom(output, id, recipe);
    }

    private static void custom(RecipeOutput output, String folder, ItemLike result, Recipe<?> recipe) {
        custom(output, recipeId(folder, result), recipe);
    }

    private static void custom(RecipeOutput output, String folder, ItemLike result, String suffix, Recipe<?> recipe) {
        custom(output, recipeId(folder, result, suffix), recipe);
    }

    private static void custom(RecipeOutput output, ResourceLocation id, Recipe<?> recipe) {
        output.accept(id, recipe, null);
    }

    private static ResourceLocation recipeId(String folder, ItemLike result) {
        return Magitech.id(folder + "/" + net.minecraft.data.recipes.RecipeBuilder.getDefaultRecipeId(result).getPath());
    }

    private static ResourceLocation recipeId(String folder, ItemLike result, String suffix) {
        return recipeId(folder, result).withSuffix(suffix);
    }

    private static ResourceLocation recipeId(String folder, Fluid result) {
        return Magitech.id(folder + "/" + net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(result).getPath());
    }

    private static Ingredient i(ItemLike item) {
        return Ingredient.of(item);
    }

    private static Ingredient tag(TagKey<Item> tag) {
        return Ingredient.of(tag);
    }

    private static SizedIngredient s(ItemLike item, int count) {
        return new SizedIngredient(i(item), count);
    }

    private static SizedIngredient s(Ingredient ingredient, int count) {
        return new SizedIngredient(ingredient, count);
    }

    private static SizedIngredient s(TagKey<Item> tag, int count) {
        return s(tag(tag), count);
    }

    private static SizedIngredient sized(Ingredient ingredient, int count) {
        return new SizedIngredient(ingredient, count);
    }

    private static Ingredient component(ItemLike item, net.stln.magitech.feature.tool.material.ToolMaterial material, boolean strict) {
        return DataComponentIngredient.of(strict, ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(material), item);
    }

    private static ItemStack stack(ItemLike item, int count) {
        return new ItemStack(item, count);
    }

    private static java.util.Map<Character, Ingredient> keys(Object... values) {
        var result = new java.util.HashMap<Character, Ingredient>();
        for (int index = 0; index < values.length; index += 2) {
            result.put((Character) values[index], ingredient(values[index + 1]));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Ingredient ingredient(Object value) {
        if (value instanceof Ingredient ingredient) return ingredient;
        if (value instanceof TagKey<?> tag) return Ingredient.of((TagKey<Item>) tag);
        if (value instanceof ItemLike item) return i(item);
        throw new IllegalArgumentException("Unsupported recipe ingredient: " + value);
    }
}
