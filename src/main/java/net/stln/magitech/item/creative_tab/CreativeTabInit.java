package net.stln.magitech.item.creative_tab;

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
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.ThreadboundGenerator;
import net.stln.magitech.item.tool.material.MaterialInit;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.item.tool.toolitem.PartToolGenerator;
import net.stln.magitech.magic.spell.SpellInit;
import net.stln.magitech.magic.spell.SpellLike;

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
                output.accept(ThreadboundGenerator.generateThreadbound(ItemInit.THE_FIRE_THAT_THINKS.get(), allSpells));
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
                output.accept(ItemInit.MANA_DEEXCITER_CORE.get());
                output.accept(ItemInit.ASPECT_COLLECTOR.get());
                output.accept(ItemInit.BOOTS_FRAME.get());
                output.accept(ItemInit.MANA_BERRIES.get());
                output.accept(ItemInit.MANA_PIE.get());
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
                output.accept(BlockInit.ZARDIUS_CRUCIBLE_ITEM.get());
                output.accept(BlockInit.ALCHEMETRIC_PYLON_ITEM.get());
                output.accept(BlockInit.ATHANOR_PILLAR_ITEM.get());
                output.accept(BlockInit.MANA_NODE_ITEM.get());
                output.accept(BlockInit.MANA_VESSEL_ITEM.get());
                output.accept(BlockInit.FLUORITE_ORE_ITEM.get());
                output.accept(BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM.get());
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
                output.accept(BlockInit.SCORCHED_GRASS_SOIL_ITEM.get());
                output.accept(BlockInit.SCORCHED_SOIL_ITEM.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_TOOL_TAB = CREATIVE_MODE_TABS.register("magitech_tool_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech_tool"))
            .icon(() -> PartToolGenerator.generateLightSword(MaterialInit.FLUORITE))
            .withTabsBefore(MAGITECH_BLOCK_TAB.getKey())
            .displayItems((parameters, output) -> {
                output.accept(PartToolGenerator.generateDagger(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generateDagger(MaterialInit.ABYSSITE));

                output.accept(PartToolGenerator.generateLightSword(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generateLightSword(MaterialInit.ABYSSITE));

                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generateHeavySword(MaterialInit.ABYSSITE));

                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.STONE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.BONE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.IRON));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generatePickaxe(MaterialInit.ABYSSITE));

                output.accept(PartToolGenerator.generateHammer(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generateHammer(MaterialInit.ABYSSITE));

                output.accept(PartToolGenerator.generateAxe(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generateAxe(MaterialInit.ABYSSITE));

                output.accept(PartToolGenerator.generateShovel(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generateShovel(MaterialInit.ABYSSITE));

                output.accept(PartToolGenerator.generateScythe(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generateScythe(MaterialInit.ABYSSITE));

                output.accept(PartToolGenerator.generateWand(MaterialInit.WOOD));
                output.accept(PartToolGenerator.generateWand(MaterialInit.STONE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.DEEPSLATE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.COPPER));
                output.accept(PartToolGenerator.generateWand(MaterialInit.BONE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.MOSS));
                output.accept(PartToolGenerator.generateWand(MaterialInit.IRON));
                output.accept(PartToolGenerator.generateWand(MaterialInit.GOLD));
                output.accept(PartToolGenerator.generateWand(MaterialInit.AMETHYST));
                output.accept(PartToolGenerator.generateWand(MaterialInit.CITRINE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.REDSTONE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.LAPIS));
                output.accept(PartToolGenerator.generateWand(MaterialInit.CALCITE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.DRIPSTONE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.FLUORITE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.DIAMOND));
                output.accept(PartToolGenerator.generateWand(MaterialInit.EMERALD));
                output.accept(PartToolGenerator.generateWand(MaterialInit.ENDER_METAL));
                output.accept(PartToolGenerator.generateWand(MaterialInit.QUARTZ));
                output.accept(PartToolGenerator.generateWand(MaterialInit.GLOWSTONE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.NETHERITE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.RADIANT_STEEL));
                output.accept(PartToolGenerator.generateWand(MaterialInit.FRIGIDITE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.TRANSLUCIUM));
                output.accept(PartToolGenerator.generateWand(MaterialInit.RESONITE));
                output.accept(PartToolGenerator.generateWand(MaterialInit.ABYSSITE));
            }).build());


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGITECH_PART_TAB = CREATIVE_MODE_TABS.register("magitech_part_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.magitech.magitech_part"))
            .icon(() -> PartToolGenerator.generatePart((PartItem) ItemInit.LIGHT_BLADE.get(), MaterialInit.FLUORITE))
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
                        ItemInit.REINFORCED_STICK.get(),
                        ItemInit.PLATE.get(),
                        ItemInit.CATALYST.get(),
                        ItemInit.CONDUCTOR.get()
                );
                for (Item item : partList) {
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.WOOD));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.STONE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.DEEPSLATE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.COPPER));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.BONE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.MOSS));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.IRON));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.GOLD));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.AMETHYST));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.CITRINE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.REDSTONE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.LAPIS));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.CALCITE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.DRIPSTONE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.FLUORITE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.DIAMOND));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.EMERALD));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.ENDER_METAL));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.QUARTZ));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.GLOWSTONE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.NETHERITE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.RADIANT_STEEL));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.FRIGIDITE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.TRANSLUCIUM));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.RESONITE));
                    output.accept(PartToolGenerator.generatePart((PartItem) item, MaterialInit.ABYSSITE));
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

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FRIGALA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.CRYOLUXA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.NIVALUNE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.GLISTELDA));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.VOLTARIS));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FULGENZA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.SPARKION));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ARCLUME));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.MIRAZIEN));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.PHANTASTRA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.VEILMIST));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FADANCEA));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.TREMIVOX));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.OSCILBEAM));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.SONISTORM));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.QUAVERIS));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ARCALETH));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.MYSTAVEN));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.GLYMORA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ENVISTRA));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.AELTHERIN));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.FLUVINAE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.MISTRELUNE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.SYLLAEZE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.NYMPHORA));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.NULLIXIS));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.VOIDLANCE));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.TENEBRISOL));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.DISPARUNDRA));
                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.TENEBPORT));

                output.accept(ThreadboundGenerator.generateThreadPage(SpellInit.ENERCRUX));

            }).build());

    public static void registerCreativeTabs(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Creative Tabs for" + Magitech.MOD_ID);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
