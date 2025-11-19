package net.stln.magitech.item.tool.material;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.trait.*;
import net.stln.magitech.registry.DeferredToolMaterial;
import net.stln.magitech.registry.DeferredToolMaterialRegister;

public class MaterialInit {

    public static final DeferredToolMaterialRegister REGISTER = new DeferredToolMaterialRegister(Magitech.MOD_ID);
    public static final DeferredToolMaterial<ToolMaterial> WOOD = register("wood", ToolStatsInit.WOOD, SpellToolStatsInit.WOOD, new AdaptationTrait());
    public static final DeferredToolMaterial<ToolMaterial> STONE = register("stone", ToolStatsInit.STONE, SpellToolStatsInit.STONE, new GeoMendingTrait());
    public static final DeferredToolMaterial<ToolMaterial> DEEPSLATE = register("deepslate", ToolStatsInit.DEEPSLATE, SpellToolStatsInit.DEEPSLATE, new HardmineTrait());
    public static final DeferredToolMaterial<ToolMaterial> COPPER = register("copper", ToolStatsInit.COPPER, SpellToolStatsInit.COPPER, new ConductanceTrait());
    public static final DeferredToolMaterial<ToolMaterial> ZINC = register("zinc", ToolStatsInit.ZINC, SpellToolStatsInit.ZINC, new ElectrostaticChargeTrait());
    public static final DeferredToolMaterial<ToolMaterial> BONE = register("bone", ToolStatsInit.BONE, SpellToolStatsInit.BONE, new FossilizationTrait());
    public static final DeferredToolMaterial<ToolMaterial> GLASS = register("glass", ToolStatsInit.GLASS, SpellToolStatsInit.GLASS, new FragileTrait());
    public static final DeferredToolMaterial<ToolMaterial> SANDSTONE = register("sandstone", ToolStatsInit.SANDSTONE, SpellToolStatsInit.SANDSTONE, new TemperatureDifferenceTrait());
    public static final DeferredToolMaterial<ToolMaterial> MOSS = register("moss", ToolStatsInit.MOSS, SpellToolStatsInit.MOSS, new GrowthTrait());
    public static final DeferredToolMaterial<ToolMaterial> IRON = register("iron", ToolStatsInit.IRON, SpellToolStatsInit.IRON, new DuranceTrait());
    public static final DeferredToolMaterial<ToolMaterial> GOLD = register("gold", ToolStatsInit.GOLD, SpellToolStatsInit.GOLD, new CatalysisTrait());
    public static final DeferredToolMaterial<ToolMaterial> AMETHYST = register("amethyst", ToolStatsInit.AMETHYST, SpellToolStatsInit.AMETHYST, new ShatterforceTrait());
    public static final DeferredToolMaterial<ToolMaterial> CITRINE = register("citrine", ToolStatsInit.CITRINE, SpellToolStatsInit.CITRINE, new HeatTreatmentTrait());
    public static final DeferredToolMaterial<ToolMaterial> REDSTONE = register("redstone", ToolStatsInit.REDSTONE, SpellToolStatsInit.REDSTONE, new SignalRushTrait());
    public static final DeferredToolMaterial<ToolMaterial> LAPIS = register("lapis", ToolStatsInit.LAPIS, SpellToolStatsInit.LAPIS, new InclusionTrait());
    public static final DeferredToolMaterial<ToolMaterial> CALCITE = register("calcite", ToolStatsInit.CALCITE, SpellToolStatsInit.CALCITE, new BirefringenceTrait());
    public static final DeferredToolMaterial<ToolMaterial> DRIPSTONE = register("dripstone", ToolStatsInit.DRIPSTONE, SpellToolStatsInit.DRIPSTONE, new PrecipitationTrait());
    public static final DeferredToolMaterial<ToolMaterial> FLUORITE = register("fluorite", ToolStatsInit.FLUORITE, SpellToolStatsInit.FLUORITE, new OverchargedTrait());
    public static final DeferredToolMaterial<ToolMaterial> TOURMALINE = register("tourmaline", ToolStatsInit.TOURMALINE, SpellToolStatsInit.TOURMALINE, new ElectricalBoostTrait());
    public static final DeferredToolMaterial<ToolMaterial> DIAMOND = register("diamond", ToolStatsInit.DIAMOND, SpellToolStatsInit.DIAMOND, new LightweightTrait());
    public static final DeferredToolMaterial<ToolMaterial> EMERALD = register("emerald", ToolStatsInit.EMERALD, SpellToolStatsInit.EMERALD, new ConcentrationTrait());
    public static final DeferredToolMaterial<ToolMaterial> ENDER_METAL = register("ender_metal", ToolStatsInit.ENDER_METAL, SpellToolStatsInit.ENDER_METAL, new EnderDrawTrait());
    public static final DeferredToolMaterial<ToolMaterial> QUARTZ = register("quartz", ToolStatsInit.QUARTZ, SpellToolStatsInit.QUARTZ, new SmoothTrait());
    public static final DeferredToolMaterial<ToolMaterial> GLOWSTONE = register("glowstone", ToolStatsInit.GLOWSTONE, SpellToolStatsInit.GLOWSTONE, new IlluminationTrait());
    public static final DeferredToolMaterial<ToolMaterial> SULFURIC_ACID_BATTERY = register("sulfuric_acid_battery", ToolStatsInit.SULFURIC_ACID_BATTERY, SpellToolStatsInit.SULFURIC_ACID_BATTERY, new SparkTrait());
    public static final DeferredToolMaterial<ToolMaterial> NETHERITE = register("netherite", ToolStatsInit.NETHERITE, SpellToolStatsInit.NETHERITE, new LavaforgedTrait());
    public static final DeferredToolMaterial<ToolMaterial> RADIANT_STEEL = register("radiant_steel", ToolStatsInit.RADIANT_STEEL, SpellToolStatsInit.RADIANT_STEEL, new BrillianceTrait());
    public static final DeferredToolMaterial<ToolMaterial> FRIGIDITE = register("frigidite", ToolStatsInit.FRIGIDITE, SpellToolStatsInit.FRIGIDITE, new ShatterpiercerTrait());
    public static final DeferredToolMaterial<ToolMaterial> TRANSLUCIUM = register("translucium", ToolStatsInit.TRANSLUCIUM, SpellToolStatsInit.TRANSLUCIUM, new PhantomSlayerTrait());
    public static final DeferredToolMaterial<ToolMaterial> RESONITE = register("resonite", ToolStatsInit.RESONITE, SpellToolStatsInit.RESONITE, new BlindResonanceTrait());
    public static final DeferredToolMaterial<ToolMaterial> ABYSSITE = register("abyssite", ToolStatsInit.ABYSSITE, SpellToolStatsInit.ABYSSITE, new PhaseVacuumCollapseTrait());

    private static DeferredToolMaterial<ToolMaterial> register(String name, ToolStats stats, ToolStats spellCasterStats, Trait trait) {
        return REGISTER.register(name, () -> new ToolMaterial(stats, spellCasterStats, trait));
    }

    public static void registerMaterials(IEventBus bus) {
        Magitech.LOGGER.info("Registering Materials for" + Magitech.MOD_ID);
        REGISTER.register(bus);

        /*WOOD.addStats(new ToolStats(0.4F, 0.4F, 1.1F, 0.4F, 0.5F, 0.9F, 0.7F, 0.3F, Element.FLOW, MiningLevel.NONE, 0));
        WOOD.addSpellCasterStats(new ToolStats(0.4F, 0.4F, 1.1F, 1.2F, 0.5F, 0.9F, 0.7F, 0.3F, Element.FLOW, MiningLevel.NONE, 0));

        STONE.addStats(new ToolStats(1F, 0F, 0.85F, 0.8F, 1.5F, 1F, 0.8F, 0.5F, Element.NONE, MiningLevel.STONE, 0));
        STONE.addSpellCasterStats(new ToolStats(1F, 0F, 0.85F, 0.8F, 1.5F, 1F, 0.8F, 0.5F, Element.NONE, MiningLevel.STONE, 0));

        DEEPSLATE.addStats(new ToolStats(0.7F, 0.35F, 0.7F, 0.9F, 2F, 1F, 0.8F, 0.6F, Element.TREMOR, MiningLevel.STONE, 0));
        DEEPSLATE.addSpellCasterStats(new ToolStats(0.7F, 0.35F, 0.7F, 0.9F, 2F, 1F, 0.8F, 0.6F, Element.TREMOR, MiningLevel.STONE, 0 ));

        COPPER.addStats(new ToolStats(0.8F, 0.3F, 0.8F, 1F, 0.8F, 0.8F, 0.9F, 0.8F, Element.SURGE, MiningLevel.STONE, 0));
        COPPER.addSpellCasterStats(new ToolStats(0.9F, 0.3F, 0.8F, 1F, 0.8F, 0.8F, 0.9F, 0.8F, Element.SURGE, MiningLevel.STONE, 0));

        BONE.addStats(new ToolStats(1.1F, 0F, 1.05F, 0.6F, 1.2F, 0.7F, 0.8F, 0.7F, Element.NONE, MiningLevel.STONE, 0));
        BONE.addSpellCasterStats(new ToolStats(1.1F, 0F, 1.05F, 1.9F, 1.2F, 0.7F, 0.8F, 0.7F, Element.NONE, MiningLevel.STONE, 0));

        MOSS.addStats(new ToolStats(0F, 1F, 1.2F, 0.5F, 0.2F, 0.5F, 0.5F, 0.2F, Element.FLOW, MiningLevel.NONE, 0));
        MOSS.addSpellCasterStats(new ToolStats(0.3F, 1F, 1.2F, 0.5F, 0.2F, 0.5F, 0.5F, 0.2F, Element.FLOW, MiningLevel.NONE, 0));

        IRON.addStats(new ToolStats(1.2F, 0F, 1F, 1.2F, 1F, 1F, 1F, 1F, Element.NONE, MiningLevel.IRON, 1));
        IRON.addSpellCasterStats(new ToolStats(1.2F, 0F, 1F, 1.2F, 1F, 1F, 1F, 1F, Element.NONE, MiningLevel.IRON, 1));

        GOLD.addStats(new ToolStats(0.1F, 1.2F, 0.75F, 2.4F, 0.6F, 0.9F, 0.7F, 0.2F, Element.MAGIC, MiningLevel.STONE, 1));
        GOLD.addSpellCasterStats(new ToolStats(0.1F, 1.2F, 0.75F, 2.4F, 0.6F, 0.9F, 0.7F, 0.2F, Element.MAGIC, MiningLevel.STONE, 1));

        AMETHYST.addStats(new ToolStats(0.9F, 0.25F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.MAGIC, MiningLevel.STONE, 1));
        AMETHYST.addSpellCasterStats(new ToolStats(0.9F, 0.35F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.MAGIC, MiningLevel.STONE, 1));

        CITRINE.addStats(new ToolStats(0.9F, 0.25F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.EMBER, MiningLevel.STONE, 1));
        CITRINE.addSpellCasterStats(new ToolStats(0.9F, 0.35F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.EMBER, MiningLevel.STONE, 1));

        REDSTONE.addStats(new ToolStats(0.5F, 0.8F, 0.65F, 1.1F, 0.3F, 1F, 1F, 1.2F, Element.SURGE, MiningLevel.IRON, 1));
        REDSTONE.addSpellCasterStats(new ToolStats(0.5F, 0.9F, 0.65F, 1.1F, 0.3F, 1F, 1F, 1.2F, Element.SURGE, MiningLevel.IRON, 1));

        LAPIS.addStats(new ToolStats(0.8F, 0.5F, 1F, 1F, 1.9F, 0.8F, 0.7F, 0.1F, Element.TREMOR, MiningLevel.STONE, 1));
        LAPIS.addSpellCasterStats(new ToolStats(0.8F, 0.6F, 1F, 1F, 1.9F, 0.8F, 0.7F, 0.1F, Element.TREMOR, MiningLevel.STONE, 1));

        CALCITE.addStats(new ToolStats(0.7F, 0.4F, 0.8F, 0.9F, 0.4F, 0.9F, 0.9F, 0.6F, Element.GLACE, MiningLevel.STONE, 1));
        CALCITE.addSpellCasterStats(new ToolStats(0.7F, 0.6F, 0.8F, 0.9F, 0.4F, 0.9F, 0.9F, 0.6F, Element.GLACE, MiningLevel.STONE, 1));

        DRIPSTONE.addStats(new ToolStats(0.5F, 0.6F, 0.7F, 0.75F, 0.8F, 1F, 0.75F, 0.4F, Element.FLOW, MiningLevel.STONE, 1));
        DRIPSTONE.addSpellCasterStats(new ToolStats(0.8F, 0.6F, 0.7F, 0.75F, 0.8F, 1F, 0.75F, 0.4F, Element.FLOW, MiningLevel.STONE, 1));

        FLUORITE.addStats(new ToolStats(0F, 1.0F, 1.1F, 0.9F, 0.1F, 0.7F, 1.2F, 0.5F, Element.PHANTOM, MiningLevel.STONE, 1));
        FLUORITE.addSpellCasterStats(new ToolStats(0.3F, 1.2F, 1.1F, 0.9F, 0.1F, 0.7F, 2.0F, 0.5F, Element.PHANTOM, MiningLevel.STONE, 1));

        DIAMOND.addStats(new ToolStats(1.5F, 0F, 1.1F, 1.6F, 0.8F, 1F, 0.8F, 4.0F, Element.NONE, MiningLevel.DIAMOND, 2));
        DIAMOND.addSpellCasterStats(new ToolStats(1.5F, 0F, 1.1F, 1.0F, 0.8F, 1F, 0.8F, 4.0F, Element.NONE, MiningLevel.DIAMOND, 2));

        EMERALD.addStats(new ToolStats(0.9F, 0.6F, 0.9F, 1.3F, 1.2F, 1.1F, 0.7F, 3.6F, Element.FLOW, MiningLevel.DIAMOND, 2));
        EMERALD.addSpellCasterStats(new ToolStats(0.9F, 0.8F, 0.9F, 1.3F, 1.2F, 1.1F, 0.7F, 3.6F, Element.FLOW, MiningLevel.DIAMOND, 2));

        ENDER_METAL.addStats(new ToolStats(0.8F, 0.9F, 0.5F, 1F, 0.7F, 1.4F, 0.6F, 3.2F, Element.HOLLOW, MiningLevel.IRON, 2));
        ENDER_METAL.addSpellCasterStats(new ToolStats(0.9F, 0.9F, 0.5F, 1F, 0.7F, 1.4F, 0.6F, 3.2F, Element.HOLLOW, MiningLevel.IRON, 2));

        QUARTZ.addStats(new ToolStats(1.1F, 0.2F, 1F, 1.2F, 0.9F, 1F, 1.2F, 2.5F, Element.GLACE, MiningLevel.STONE, 2));
        QUARTZ.addSpellCasterStats(new ToolStats(1.1F, 0.2F, 1F, 1.2F, 0.9F, 1F, 1.2F, 2.5F, Element.GLACE, MiningLevel.STONE, 2));

        GLOWSTONE.addStats(new ToolStats(0.8F, 0.6F, 0.8F, 1.1F, 1.1F, 0.8F, 1F, 2.6F, Element.PHANTOM, MiningLevel.STONE, 2));
        GLOWSTONE.addSpellCasterStats(new ToolStats(0.8F, 0.8F, 0.8F, 1.1F, 1.1F, 0.8F, 1F, 2.6F, Element.PHANTOM, MiningLevel.STONE, 2));

        NETHERITE.addStats(new ToolStats(1.25F, 0.5F, 0.9F, 1.8F, 1.1F, 1.1F, 1.1F, 5.0F, Element.EMBER, MiningLevel.NETHERITE, 3));
        NETHERITE.addSpellCasterStats(new ToolStats(1.25F, 0.5F, 0.9F, 0.8F, 1.1F, 1.1F, 1.1F, 5.0F, Element.EMBER, MiningLevel.NETHERITE, 3));

        RADIANT_STEEL.addStats(new ToolStats(0.7F, 1.2F, 0.8F, 2F, 0F, 1.2F, 0.9F, 5.5F, Element.GLACE, MiningLevel.NETHERITE, 3));
        RADIANT_STEEL.addSpellCasterStats(new ToolStats(0.7F, 1.5F, 0.8F, 2F, 0F, 1.2F, 0.9F, 5.5F, Element.GLACE, MiningLevel.NETHERITE, 3));

        FRIGIDITE.addStats(new ToolStats(1F, 1F, 1.3F, 1.7F, 0.9F, 0.7F, 0.6F, 6.1F, Element.GLACE, MiningLevel.NETHERITE, 4));
        FRIGIDITE.addSpellCasterStats(new ToolStats(1F, 1F, 1.3F, 1.7F, 0.9F, 0.7F, 0.6F, 6.1F, Element.GLACE, MiningLevel.NETHERITE, 4));

        TRANSLUCIUM.addStats(new ToolStats(1.5F, 0.7F, 0.9F, 1.7F, 1.2F, 1.1F, 1.2F, 6.3F, Element.PHANTOM, MiningLevel.NETHERITE, 4));
        TRANSLUCIUM.addSpellCasterStats(new ToolStats(1.5F, 0.7F, 0.9F, 1.5F, 1.2F, 1.1F, 1.2F, 6.3F, Element.PHANTOM, MiningLevel.NETHERITE, 4));

        RESONITE.addStats(new ToolStats(1.3F, 0.8F, 0.8F, 1.7F, 2.6F, 1.2F, 1F, 6.7F, Element.TREMOR, MiningLevel.NETHERITE, 4));
        RESONITE.addSpellCasterStats(new ToolStats(1.3F, 0.8F, 0.8F, 1.0F, 2.6F, 1.2F, 1F, 6.7F, Element.TREMOR, MiningLevel.NETHERITE, 4));

        ABYSSITE.addStats(new ToolStats(0.5F, 1.7F, 1F, 1.7F, 0.4F, 1.3F, 0.9F, 5.8F, Element.HOLLOW, MiningLevel.NETHERITE, 4));
        ABYSSITE.addSpellCasterStats(new ToolStats(0.5F, 1.7F, 1F, 1.1F, 0.4F, 1.3F, 0.9F, 5.8F, Element.HOLLOW, MiningLevel.NETHERITE, 4));*/
    }
}
