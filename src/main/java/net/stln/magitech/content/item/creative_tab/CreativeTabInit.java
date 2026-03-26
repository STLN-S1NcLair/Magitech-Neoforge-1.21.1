package net.stln.magitech.content.item.creative_tab;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ThreadboundGenerator;
import net.stln.magitech.content.item.tool.partitem.PartItem;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolGenerator;
import net.stln.magitech.feature.magic.spell.SpellInit;
import net.stln.magitech.feature.magic.spell.SpellLike;
import net.stln.magitech.feature.tool.material.MaterialInit;

import java.util.List;

public class CreativeTabInit {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Magitech.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_TAB = CREATIVE_MODE_TABS.register("magitech_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech"))
            .icon(() -> ItemInit.GLISTENING_LEXICON.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                var registries = parameters.holders();
                var spellLookup = registries.lookupOrThrow(MagitechRegistries.Keys.SPELL);
                List<SpellLike> allSpells = spellLookup.listElements().map(Holder::value).map(spell -> (SpellLike) spell).toList();

                output.accept(ThreadboundGenerator.generateThreadbound(ItemInit.GLISTENING_LEXICON.get(), allSpells));
                output.accept(ThreadboundGenerator.generateThreadbound(ItemInit.MATERIALS_AND_TOOLCRAFT_DESIGN.get(), allSpells));
                output.accept(ThreadboundGenerator.generateThreadbound(ItemInit.THE_FIRE_THAT_THINKS.get(), allSpells));
                output.accept(ThreadboundGenerator.generateThreadbound(ItemInit.APPLIED_ARCANE_CIRCUITRY.get(), allSpells));
                output.accept(ThreadboundGenerator.generateThreadbound(ItemInit.ARCANE_ENGINEERING_COMPENDIUM.get(), allSpells));
                output.accept(ItemInit.AETHER_LIFTER.get());
                output.accept(ItemInit.FLAMGLIDE_STRIDER.get());
                output.accept(ItemInit.MANA_RING.get());
                output.accept(ItemInit.GALEVENT_RING.get());
                output.accept(ItemInit.CHARGEBIND_RING.get());
                output.accept(ItemInit.TORSION_RING.get());
                output.accept(ItemInit.UMBRAL_RING.get());
                output.accept(ItemInit.DAWN_RING.get());
                output.accept(ItemInit.FLUXBOUND_RING.get());
                output.accept(ItemInit.TOOL_BELT.get());
                output.accept(ItemInit.ALCHAEFABRIC.get());
                output.accept(ItemInit.AEGIS_WEAVE.get());
                output.accept(ItemInit.FLUORITE.get());
                output.accept(ItemInit.MANA_CHARGED_FLUORITE.get());
                output.accept(ItemInit.TOURMALINE.get());
                output.accept(ItemInit.EMBER_CRYSTAL.get());
                output.accept(ItemInit.GLACE_CRYSTAL.get());
                output.accept(ItemInit.SURGE_CRYSTAL.get());
                output.accept(ItemInit.PHANTOM_CRYSTAL.get());
                output.accept(ItemInit.TREMOR_CRYSTAL.get());
                output.accept(ItemInit.MAGIC_CRYSTAL.get());
                output.accept(ItemInit.FLOW_CRYSTAL.get());
                output.accept(ItemInit.HOLLOW_CRYSTAL.get());
                output.accept(ItemInit.AGGREGATED_NOCTIS.get());
                output.accept(ItemInit.AGGREGATED_LUMINIS.get());
                output.accept(ItemInit.AGGREGATED_FLUXIA.get());
                output.accept(ItemInit.CITRINE.get());
                output.accept(ItemInit.RAW_ZINC.get());
                output.accept(ItemInit.ZINC_INGOT.get());
                output.accept(ItemInit.CHROMIUM_INGOT.get());
                output.accept(ItemInit.REDSTONE_CRYSTAL.get());
                output.accept(ItemInit.POLISHED_REDSTONE_CRYSTAL.get());
                output.accept(ItemInit.SULFUR.get());
                output.accept(ItemInit.ENDER_METAL_INGOT.get());
                output.accept(ItemInit.NETHER_STAR_BRILLIANCE.get());
                output.accept(ItemInit.RADIANT_STEEL_INGOT.get());
                output.accept(ItemInit.FRIGIDITE.get());
                output.accept(ItemInit.POLISHED_FRIGIDITE.get());
                output.accept(ItemInit.TRANSLUCIUM.get());
                output.accept(ItemInit.POLISHED_TRANSLUCIUM.get());
                output.accept(ItemInit.RESONITE.get());
                output.accept(ItemInit.POLISHED_RESONITE.get());
                output.accept(ItemInit.ABYSSITE.get());
                output.accept(ItemInit.POLISHED_ABYSSITE.get());
                output.accept(ItemInit.MANA_INSULATING_GLASS.get());
                output.accept(ItemInit.SULFURIC_ACID_BATTERY.get());
                output.accept(ItemInit.MANA_DEEXCITER_CORE.get());
                output.accept(ItemInit.ASPECT_COLLECTOR.get());
                output.accept(ItemInit.BOOTS_FRAME.get());
                output.accept(ItemInit.MANA_CELL.get());
                output.accept(ItemInit.MANA_BERRIES.get());
                output.accept(ItemInit.MANA_PIE.get());
                output.accept(ItemInit.ALCHEMICAL_FLASK.get());
                output.accept(ItemInit.WATER_FLASK.get());
                output.accept(ItemInit.LAVA_FLASK.get());
                output.accept(ItemInit.SULFURIC_ACID_FLASK.get());
                output.accept(ItemInit.MANA_POTION_FLASK.get());
                output.accept(ItemInit.HEALING_POTION_FLASK.get());
                output.accept(ItemInit.EMBER_POTION_FLASK.get());
                output.accept(ItemInit.GLACE_POTION_FLASK.get());
                output.accept(ItemInit.SURGE_POTION_FLASK.get());
                output.accept(ItemInit.PHANTOM_POTION_FLASK.get());
                output.accept(ItemInit.TREMOR_POTION_FLASK.get());
                output.accept(ItemInit.MAGIC_POTION_FLASK.get());
                output.accept(ItemInit.FLOW_POTION_FLASK.get());
                output.accept(ItemInit.HOLLOW_POTION_FLASK.get());
                output.accept(ItemInit.CELIFERN_BOAT.get());
                output.accept(ItemInit.CELIFERN_CHEST_BOAT.get());
                output.accept(ItemInit.CHARCOAL_BIRCH_BOAT.get());
                output.accept(ItemInit.CHARCOAL_BIRCH_CHEST_BOAT.get());
                output.accept(ItemInit.MYSTWOOD_BOAT.get());
                output.accept(ItemInit.MYSTWOOD_CHEST_BOAT.get());
                output.accept(ItemInit.WEAVER_SPAWN_EGG.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_BLOCK_TAB = CREATIVE_MODE_TABS.register("magitech_block_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech_block"))
            .icon(() -> BlockInit.ALCHECRYSITE_ITEM.get().getDefaultInstance())
            .withTabsBefore(MAGITECH_TAB.getKey())
            .displayItems((parameters, output) -> {
                output.accept(BlockInit.ENGINEERING_WORKBENCH_ITEM.get());
                output.accept(BlockInit.ASSEMBLY_WORKBENCH_ITEM.get());
                output.accept(BlockInit.REPAIRING_WORKBENCH_ITEM.get());
                output.accept(BlockInit.UPGRADE_WORKBENCH_ITEM.get());
                output.accept(BlockInit.TOOL_HANGER_ITEM.get());
                output.accept(BlockInit.ZARDIUS_CRUCIBLE_ITEM.get());
                output.accept(BlockInit.ALCHEMETRIC_PYLON_ITEM.get());
                output.accept(BlockInit.ATHANOR_PILLAR_ITEM.get());
                output.accept(BlockInit.MANA_NODE_ITEM.get());
                output.accept(BlockInit.MANA_RELAY_ITEM.get());
                output.accept(BlockInit.MANA_VESSEL_ITEM.get());
                output.accept(BlockInit.MANA_STRANDER_ITEM.get());
                output.accept(BlockInit.MANA_RECEIVER_ITEM.get());
                output.accept(BlockInit.MANA_COLLECTOR_ITEM.get());
                output.accept(BlockInit.MANA_JUNCTION_ITEM.get());
                output.accept(BlockInit.INFUSION_ALTAR_ITEM.get());
                output.accept(BlockInit.FLUORITE_ORE_ITEM.get());
                output.accept(BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM.get());
                output.accept(BlockInit.ZINC_ORE_ITEM.get());
                output.accept(BlockInit.DEEPSLATE_ZINC_ORE_ITEM.get());
                output.accept(BlockInit.RAW_ZINC_BLOCK_ITEM.get());
                output.accept(BlockInit.TOURMALINE_ORE_ITEM.get());
                output.accept(BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM.get());
                output.accept(BlockInit.FLUORITE_CRYSTAL_CLUSTER_ITEM.get());
                output.accept(BlockInit.REDSTONE_CRYSTAL_CLUSTER_ITEM.get());
                output.accept(BlockInit.SULFUR_CRYSTAL_CLUSTER_ITEM.get());
                output.accept(BlockInit.SULFUR_BLOCK_ITEM.get());
                output.accept(BlockInit.MISTALIA_PETALS_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_STAIRS_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_SLAB_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_WALL_ITEM.get());
                output.accept(BlockInit.POLISHED_ALCHECRYSITE_ITEM.get());
                output.accept(BlockInit.POLISHED_ALCHECRYSITE_STAIRS_ITEM.get());
                output.accept(BlockInit.POLISHED_ALCHECRYSITE_SLAB_ITEM.get());
                output.accept(BlockInit.POLISHED_ALCHECRYSITE_WALL_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_BRICKS_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_BRICK_STAIRS_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_BRICK_SLAB_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_BRICK_WALL_ITEM.get());
                output.accept(BlockInit.ALCHECRYSITE_TILES_ITEM.get());
                output.accept(BlockInit.VESPERITE_ITEM.get());
                output.accept(BlockInit.VESPERITE_STAIRS_ITEM.get());
                output.accept(BlockInit.VESPERITE_SLAB_ITEM.get());
                output.accept(BlockInit.VESPERITE_WALL_ITEM.get());
                output.accept(BlockInit.FLUORITE_BLOCK_ITEM.get());
                output.accept(BlockInit.FLUORITE_BRICKS_ITEM.get());
                output.accept(BlockInit.FLUORITE_BRICK_STAIRS_ITEM.get());
                output.accept(BlockInit.FLUORITE_BRICK_SLAB_ITEM.get());
                output.accept(BlockInit.FLUORITE_BRICK_WALL_ITEM.get());
                output.accept(BlockInit.CELIFERN_LOG_ITEM.get());
                output.accept(BlockInit.CELIFERN_WOOD_ITEM.get());
                output.accept(BlockInit.STRIPPED_CELIFERN_LOG_ITEM.get());
                output.accept(BlockInit.STRIPPED_CELIFERN_WOOD_ITEM.get());
                output.accept(BlockInit.CELIFERN_PLANKS_ITEM.get());
                output.accept(BlockInit.CELIFERN_STAIRS_ITEM.get());
                output.accept(BlockInit.CELIFERN_SLAB_ITEM.get());
                output.accept(BlockInit.CELIFERN_FENCE_ITEM.get());
                output.accept(BlockInit.CELIFERN_FENCE_GATE_ITEM.get());
                output.accept(BlockInit.CELIFERN_DOOR_ITEM.get());
                output.accept(BlockInit.CELIFERN_TRAPDOOR_ITEM.get());
                output.accept(BlockInit.CELIFERN_PRESSURE_PLATE_ITEM.get());
                output.accept(BlockInit.CELIFERN_BUTTON_ITEM.get());
                output.accept(BlockInit.CELIFERN_LEAVES_ITEM.get());
                output.accept(BlockInit.CELIFERN_SAPLING_ITEM.get());
                output.accept(BlockInit.CELIFERN_SIGN_ITEM.get());
                output.accept(BlockInit.CELIFERN_HANGING_SIGN_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_LOG_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_WOOD_ITEM.get());
                output.accept(BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM.get());
                output.accept(BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_PLANKS_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_STAIRS_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_SLAB_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_FENCE_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_FENCE_GATE_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_DOOR_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_TRAPDOOR_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_PRESSURE_PLATE_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_BUTTON_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_LEAVES_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_SAPLING_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_SIGN_ITEM.get());
                output.accept(BlockInit.CHARCOAL_BIRCH_HANGING_SIGN_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_LOG_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_WOOD_ITEM.get());
                output.accept(BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM.get());
                output.accept(BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_PLANKS_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_STAIRS_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_SLAB_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_FENCE_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_FENCE_GATE_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_DOOR_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_TRAPDOOR_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_PRESSURE_PLATE_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_BUTTON_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_SIGN_ITEM.get());
                output.accept(BlockInit.MYSTWOOD_HANGING_SIGN_ITEM.get());
                output.accept(BlockInit.SCORCHED_GRASS_SOIL_ITEM.get());
                output.accept(BlockInit.SCORCHED_SOIL_ITEM.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_TOOL_TAB = CREATIVE_MODE_TABS.register("magitech_tool_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech_tool"))
            .icon(() -> SynthesisedToolGenerator.generateLightSword(MaterialInit.FLUORITE))
            .withTabsBefore(MAGITECH_BLOCK_TAB.getKey())
            .displayItems((parameters, output) -> {
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generateDagger(MaterialInit.ABYSSITE));

                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generateLightSword(MaterialInit.ABYSSITE));

                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generateHeavySword(MaterialInit.ABYSSITE));

                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generatePickaxe(MaterialInit.ABYSSITE));

                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generateHammer(MaterialInit.ABYSSITE));

                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generateAxe(MaterialInit.ABYSSITE));

                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generateShovel(MaterialInit.ABYSSITE));

                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generateScythe(MaterialInit.ABYSSITE));

                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.WOOD));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.STONE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.DEEPSLATE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.COPPER));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.ZINC));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.BONE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.GLASS));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.SANDSTONE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.MOSS));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.IRON));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.GOLD));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.AMETHYST));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.CITRINE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.REDSTONE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.LAPIS));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.CALCITE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.DRIPSTONE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.FLUORITE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.TOURMALINE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.DIAMOND));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.EMERALD));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.ENDER_METAL));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.QUARTZ));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.GLOWSTONE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.SULFURIC_ACID_BATTERY));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.NETHERITE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.RADIANT_STEEL));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.FRIGIDITE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.TRANSLUCIUM));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.RESONITE));
                output.accept(SynthesisedToolGenerator.generateWand(MaterialInit.ABYSSITE));
            }).build());


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_PART_TAB = CREATIVE_MODE_TABS.register("magitech_part_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech_part"))
            .icon(() -> SynthesisedToolGenerator.generatePart((PartItem) ItemInit.LIGHT_BLADE.get(), MaterialInit.FLUORITE))
            .withTabsBefore(MAGITECH_TOOL_TAB.getKey())
            .displayItems((parameters, output) -> {
                List<Item> partList = List.of(
                        ItemInit.LIGHT_BLADE.get(),
                        ItemInit.HEAVY_BLADE.get(),
                        ItemInit.LIGHT_HANDLE.get(),
                        ItemInit.HEAVY_HANDLE.get(),
                        ItemInit.TOOL_BINDING.get(),
                        ItemInit.HANDGUARD.get(),
                        ItemInit.STRIKE_HEAD.get(),
                        ItemInit.SPIKE_HEAD.get(),
                        ItemInit.REINFORCED_ROD.get(),
                        ItemInit.PLATE.get(),
                        ItemInit.CATALYST.get(),
                        ItemInit.CONDUCTOR.get()
                );
                for (Item item : partList) {
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.WOOD));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.STONE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.DEEPSLATE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.COPPER));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.ZINC));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.BONE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.GLASS));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.SANDSTONE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.MOSS));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.IRON));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.GOLD));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.AMETHYST));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.CITRINE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.REDSTONE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.LAPIS));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.CALCITE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.DRIPSTONE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.FLUORITE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.TOURMALINE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.DIAMOND));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.EMERALD));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.ENDER_METAL));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.QUARTZ));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.GLOWSTONE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.SULFURIC_ACID_BATTERY));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.NETHERITE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.RADIANT_STEEL));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.FRIGIDITE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.TRANSLUCIUM));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.RESONITE));
                    output.accept(SynthesisedToolGenerator.generatePart((PartItem) item, MaterialInit.ABYSSITE));
                }
            }).build());


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_SPELL_TAB = CREATIVE_MODE_TABS.register("magitech_spell_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech_spell"))
            .icon(() -> ThreadboundGenerator.generateThreadPage(SpellInit.ENERCRUX))
            .withTabsBefore(MAGITECH_PART_TAB.getKey())
            .displayItems((parameters, output) -> {
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.IGNISCA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.PYROLUX));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FLUVALEN));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.BLAZEWEND));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.VOLKARIN));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ARDOVITAE));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FRIGALA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.CRYOLUXA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.NIVALUNE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.GLISTELDA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FROSBLAST));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.VOLTARIS));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FULGENZA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.SPARKION));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ARCLUME));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ELECTROIDE));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.MIRAZIEN));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.PHANTASTRA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.VEILMIST));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FADANCEA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ILLUSFLARE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.LUXGRAIL));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.TREMIVOX));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.OSCILBEAM));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.SONISTORM));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.QUAVERIS));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.SHOCKVANE));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ARCALETH));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.MYSTAVEN));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.GLYMORA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ENVISTRA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.HEXFLARE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.MYSTPHEL));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.AELTHERIN));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FLUVINAE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.MISTRELUNE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.SYLLAEZE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.HYDRELUX));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.NYMPHORA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.HYDRAERUN));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.NULLIXIS));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.VOIDLANCE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.TENEBRISOL));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.DISPARUNDRA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.NIHILFLARE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.TENEBPORT));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ENERCRUX));

            }).build());

    public static void registerCreativeTabs(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Creative Tabs for" + Magitech.MOD_ID);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
