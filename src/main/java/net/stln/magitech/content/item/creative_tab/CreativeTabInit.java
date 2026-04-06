package net.stln.magitech.content.item.creative_tab;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ThreadboundGenerator;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolGenerator;
import net.stln.magitech.feature.magic.spell.SpellInit;
import net.stln.magitech.feature.magic.spell.SpellLike;
import net.stln.magitech.feature.tool.material.MaterialInit;
import net.stln.magitech.feature.tool.material.ToolMaterialLike;
import net.stln.magitech.feature.tool.part.ToolPartInit;
import net.stln.magitech.feature.tool.part.ToolPartLike;
import net.stln.magitech.feature.tool.tool_type.ToolTypeInit;
import net.stln.magitech.feature.tool.tool_type.ToolTypeLike;
import net.stln.magitech.registry.RegistryHelper;

import java.util.List;
import java.util.Map;

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
                output.accept(BlockInit.PEDESTAL_PYLON_ITEM.get());
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
            .icon(() -> SynthesisedToolGenerator.generateTool(ToolTypeInit.LIGHT_SWORD.asToolType(), MaterialInit.WOOD, MaterialInit.FLUORITE, MaterialInit.DEEPSLATE, MaterialInit.GOLD))
            .withTabsBefore(MAGITECH_BLOCK_TAB.getKey())
            .displayItems((parameters, output) -> {
                for (ToolTypeLike type : RegistryHelper.registeredToolTypes()) {
                    for (ToolMaterialLike material : RegistryHelper.registeredToolMaterials()) {
                        output.accept(SynthesisedToolGenerator.generateTool(type.asToolType(), material.asToolMaterial()));
                    }
                }
            }).build());


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_PART_TAB = CREATIVE_MODE_TABS.register("magitech_part_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech_part"))
            .icon(() -> SynthesisedToolGenerator.generatePart(ToolPartInit.LIGHT_BLADE.asToolPart(), MaterialInit.FLUORITE))
            .withTabsBefore(MAGITECH_TOOL_TAB.getKey())
            .displayItems((parameters, output) -> {
                for (ToolPartLike part : RegistryHelper.registeredToolParts()) {
                    for (ToolMaterialLike material : RegistryHelper.registeredToolMaterials()) {
                        output.accept(SynthesisedToolGenerator.generatePart(part.asToolPart(), material.asToolMaterial()));
                    }
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
