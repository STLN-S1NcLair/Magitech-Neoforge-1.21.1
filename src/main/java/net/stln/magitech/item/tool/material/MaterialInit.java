package net.stln.magitech.item.tool.material;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.warden.Warden;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.ElementAffinityRegister;
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.trait.*;
import net.stln.magitech.util.Element;

public class MaterialInit {

    public static ToolMaterial WOOD = new ToolMaterial(new AdaptationTrait(), Magitech.id("wood"));
    public static ToolMaterial STONE = new ToolMaterial(new GeoMendingTrait(), Magitech.id("stone"));
    public static ToolMaterial DEEPSLATE = new ToolMaterial(new HardmineTrait(), Magitech.id("deepslate"));
    public static ToolMaterial COPPER = new ToolMaterial(new ConductanceTrait(), Magitech.id("copper"));
    public static ToolMaterial BONE = new ToolMaterial(new FossilizationTrait(), Magitech.id("bone"));
    public static ToolMaterial MOSS = new ToolMaterial(new GrowthTrait(), Magitech.id("moss"));
    public static ToolMaterial IRON = new ToolMaterial(new DuranceTrait(), Magitech.id("iron"));
    public static ToolMaterial GOLD = new ToolMaterial(new CatalysisTrait(), Magitech.id("gold"));
    public static ToolMaterial AMETHYST = new ToolMaterial(new ShatterforceTrait(), Magitech.id("amethyst"));
    public static ToolMaterial CITRINE = new ToolMaterial(new HeatTreatmentTrait(), Magitech.id("citrine"));
    public static ToolMaterial REDSTONE = new ToolMaterial(new SignalRushTrait(), Magitech.id("redstone"));
    public static ToolMaterial LAPIS = new ToolMaterial(new InclusionTrait(), Magitech.id("lapis"));
    public static ToolMaterial CALCITE = new ToolMaterial(new BirefringenceTrait(), Magitech.id("calcite"));
    public static ToolMaterial DRIPSTONE = new ToolMaterial(new PrecipitationTrait(), Magitech.id("dripstone"));
    public static ToolMaterial FLUORITE = new ToolMaterial(new OverchargedTrait(), Magitech.id("fluorite"));
    public static ToolMaterial DIAMOND = new ToolMaterial(new LightweightTrait(), Magitech.id("diamond"));
    public static ToolMaterial EMERALD = new ToolMaterial(new ConcentrationTrait(), Magitech.id("emerald"));
    public static ToolMaterial ENDER_METAL = new ToolMaterial(new EnderDrawTrait(), Magitech.id("ender_metal"));
    public static ToolMaterial QUARTZ = new ToolMaterial(new SmoothTrait(), Magitech.id("quartz"));
    public static ToolMaterial GLOWSTONE = new ToolMaterial(new IlluminationTrait(), Magitech.id("glowstone"));
    public static ToolMaterial NETHERITE = new ToolMaterial(new LavaforgedTrait(), Magitech.id("netherite"));
    public static ToolMaterial RADIANT_STEEL = new ToolMaterial(new BrillianceTrait(), Magitech.id("radiant_steel"));
    public static ToolMaterial FRIGIDITE = new ToolMaterial(new ShatterpiercerTrait(), Magitech.id("frigidite"));
    public static ToolMaterial TRANSLUCIUM = new ToolMaterial(new PhantomSlayerTrait(), Magitech.id("translucium"));
    public static ToolMaterial RESONITE = new ToolMaterial(new BlindResonanceTrait(), Magitech.id("resonite"));
    public static ToolMaterial ABYSSITE = new ToolMaterial(new PhaseVacuumCollapseTrait(), Magitech.id("abyssite"));

    public static void registerMaterials() {
        Magitech.LOGGER.info("Registering Mateirals for" + Magitech.MOD_ID);
        ToolMaterialRegister.init();

        WOOD.addStats(new ToolStats(0.4F, 0.4F, 1.1F, 0.4F, 0.5F, 0.9F, 0.7F, 0.3F, Element.FLOW, MiningLevel.NONE, 0));
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
        ABYSSITE.addSpellCasterStats(new ToolStats(0.5F, 1.7F, 1F, 1.1F, 0.4F, 1.3F, 0.9F, 5.8F, Element.HOLLOW, MiningLevel.NETHERITE, 4));
    }

    public static void registerElements() {
        Magitech.LOGGER.info("Registering Elements for" + Magitech.MOD_ID);
        ElementAffinityRegister.registerAffinity(Element.EMBER, ElementAffinityRegister.INEFFICIENT, Element.EMBER);
        ElementAffinityRegister.registerAffinity(Element.GLACE, ElementAffinityRegister.INEFFICIENT, Element.GLACE);
        ElementAffinityRegister.registerAffinity(Element.SURGE, ElementAffinityRegister.INEFFICIENT, Element.SURGE);
        ElementAffinityRegister.registerAffinity(Element.PHANTOM, ElementAffinityRegister.INEFFICIENT, Element.PHANTOM);
        ElementAffinityRegister.registerAffinity(Element.TREMOR, ElementAffinityRegister.INEFFICIENT, Element.TREMOR);
        ElementAffinityRegister.registerAffinity(Element.MAGIC, ElementAffinityRegister.INEFFICIENT, Element.MAGIC);
        ElementAffinityRegister.registerAffinity(Element.FLOW, ElementAffinityRegister.INEFFICIENT, Element.FLOW);
        ElementAffinityRegister.registerAffinity(Element.HOLLOW, ElementAffinityRegister.INEFFICIENT, Element.HOLLOW);

        ElementAffinityRegister.registerAffinity(Element.EMBER, ElementAffinityRegister.EFFICIENT, Element.SURGE);
        ElementAffinityRegister.registerAffinity(Element.SURGE, ElementAffinityRegister.EFFICIENT, Element.TREMOR);
        ElementAffinityRegister.registerAffinity(Element.TREMOR, ElementAffinityRegister.EFFICIENT, Element.GLACE);
        ElementAffinityRegister.registerAffinity(Element.GLACE, ElementAffinityRegister.EFFICIENT, Element.PHANTOM);
        ElementAffinityRegister.registerAffinity(Element.PHANTOM, ElementAffinityRegister.EFFICIENT, Element.MAGIC);
        ElementAffinityRegister.registerAffinity(Element.MAGIC, ElementAffinityRegister.EFFICIENT, Element.FLOW);
        ElementAffinityRegister.registerAffinity(Element.FLOW, ElementAffinityRegister.EFFICIENT, Element.HOLLOW);
        ElementAffinityRegister.registerAffinity(Element.HOLLOW, ElementAffinityRegister.EFFICIENT, Element.EMBER);

        ElementAffinityRegister.registerAffinity(Element.EMBER, ElementAffinityRegister.EFFICIENT, Element.GLACE);
        ElementAffinityRegister.registerAffinity(Element.GLACE, ElementAffinityRegister.EFFICIENT, Element.MAGIC);
        ElementAffinityRegister.registerAffinity(Element.MAGIC, ElementAffinityRegister.EFFICIENT, Element.SURGE);
        ElementAffinityRegister.registerAffinity(Element.SURGE, ElementAffinityRegister.EFFICIENT, Element.PHANTOM);
        ElementAffinityRegister.registerAffinity(Element.PHANTOM, ElementAffinityRegister.EFFICIENT, Element.HOLLOW);
        ElementAffinityRegister.registerAffinity(Element.HOLLOW, ElementAffinityRegister.EFFICIENT, Element.TREMOR);
        ElementAffinityRegister.registerAffinity(Element.TREMOR, ElementAffinityRegister.EFFICIENT, Element.FLOW);
        ElementAffinityRegister.registerAffinity(Element.FLOW, ElementAffinityRegister.EFFICIENT, Element.EMBER);

        EntityElementRegister.registerEntityElement(Blaze.class, Element.EMBER);
        EntityElementRegister.registerEntityElement(MagmaCube.class, Element.EMBER);
        EntityElementRegister.registerEntityElement(Stray.class, Element.GLACE);
        EntityElementRegister.registerEntityElement(Phantom.class, Element.PHANTOM);
        EntityElementRegister.registerEntityElement(Vex.class, Element.PHANTOM);
        EntityElementRegister.registerEntityElement(Warden.class, Element.TREMOR);
        EntityElementRegister.registerEntityElement(Witch.class, Element.MAGIC);
        EntityElementRegister.registerEntityElement(Evoker.class, Element.MAGIC);
        EntityElementRegister.registerEntityElement(Drowned.class, Element.FLOW);
        EntityElementRegister.registerEntityElement(Guardian.class, Element.FLOW);
        EntityElementRegister.registerEntityElement(ElderGuardian.class, Element.FLOW);
        EntityElementRegister.registerEntityElement(Breeze.class, Element.FLOW);
        EntityElementRegister.registerEntityElement(EnderMan.class, Element.HOLLOW);
        EntityElementRegister.registerEntityElement(EnderDragon.class, Element.HOLLOW);
        EntityElementRegister.registerEntityElement(Endermite.class, Element.HOLLOW);
        EntityElementRegister.registerEntityElement(Shulker.class, Element.HOLLOW);
    }
}
