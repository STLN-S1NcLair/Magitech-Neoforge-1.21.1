package net.stln.magitech.item.tool.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.warden.Warden;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.ElementAffinityRegister;
import net.stln.magitech.damage.EntityElementRegister;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.trait.*;

public class MaterialInit {

    public static ToolMaterial WOOD = new ToolMaterial(new AdaptationTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wood"));
    public static ToolMaterial STONE = new ToolMaterial(new GeoMendingTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "stone"));
    public static ToolMaterial DEEPSLATE = new ToolMaterial(new HardmineTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "deepslate"));
    public static ToolMaterial COPPER = new ToolMaterial(new ConductanceTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "copper"));
    public static ToolMaterial BONE = new ToolMaterial(new FossilizationTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "bone"));
    public static ToolMaterial IRON = new ToolMaterial(new DuranceTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "iron"));
    public static ToolMaterial GOLD = new ToolMaterial(new CatalysisTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "gold"));
    public static ToolMaterial AMETHYST = new ToolMaterial(new ShatterforceTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "amethyst"));
    public static ToolMaterial REDSTONE = new ToolMaterial(new SignalRushTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "redstone"));
    public static ToolMaterial FLUORITE = new ToolMaterial(new OverchargedTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluorite"));
    public static ToolMaterial DIAMOND = new ToolMaterial(new LightweightTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "diamond"));
    public static ToolMaterial ENDER_METAL = new ToolMaterial(new EnderDrawTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ender_metal"));
    public static ToolMaterial NETHERITE = new ToolMaterial(new LavaforgedTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "netherite"));
    public static ToolMaterial FRIGIDITE = new ToolMaterial(new ShatterpiercerTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "frigidite"));
    public static ToolMaterial TRANSLUCIUM = new ToolMaterial(new PhantomSlayerTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "translucium"));
    public static ToolMaterial RESONITE = new ToolMaterial(new BlindResonanceTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "resonite"));
    public static ToolMaterial ABYSSITE = new ToolMaterial(new PhaseVacuumCollapseTrait(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "abyssite"));

    public static void registerMaterials() {
        ToolMaterialRegister.init();

        WOOD.addStats(new ToolStats(0.4F, 0.4F, 1.1F, 0.4F, 0.5F, 0.9F, 0.7F, 0.3F, Element.FLOW, MiningLevel.NONE));
        WOOD.addSpellCasterStats(new ToolStats(0.4F, 0.4F, 1.1F, 0.4F, 0.5F, 0.9F, 0.7F, 0.3F, Element.FLOW, MiningLevel.NONE));

        STONE.addStats(new ToolStats(1F, 0F, 0.85F, 0.8F, 1.5F, 1F, 0.8F, 0.5F, Element.NONE, MiningLevel.STONE));
        STONE.addSpellCasterStats(new ToolStats(1F, 0F, 0.85F, 0.8F, 1.5F, 1F, 0.8F, 0.5F, Element.NONE, MiningLevel.STONE));

        DEEPSLATE.addStats(new ToolStats(0.7F, 0.35F, 0.7F, 0.9F, 2F, 1F, 0.8F, 0.6F, Element.TREMOR, MiningLevel.STONE));
        DEEPSLATE.addSpellCasterStats(new ToolStats(0.7F, 0.35F, 0.7F, 0.9F, 2F, 1F, 0.8F, 0.6F, Element.TREMOR, MiningLevel.STONE));

        COPPER.addStats(new ToolStats(0.8F, 0.3F, 0.8F, 1F, 0.8F, 0.8F, 0.9F, 0.8F, Element.SURGE, MiningLevel.STONE));
        COPPER.addSpellCasterStats(new ToolStats(0.8F, 0.3F, 0.8F, 1F, 0.8F, 0.8F, 0.9F, 0.8F, Element.SURGE, MiningLevel.STONE));

        BONE.addStats(new ToolStats(1.1F, 0F, 1.05F, 0.6F, 1.2F, 0.7F, 0.8F, 0.7F, Element.NONE, MiningLevel.STONE));
        BONE.addSpellCasterStats(new ToolStats(1.1F, 0F, 1.05F, 0.6F, 1.2F, 0.7F, 0.8F, 0.7F, Element.NONE, MiningLevel.STONE));

        IRON.addStats(new ToolStats(1.2F, 0F, 1F, 1.2F, 1F, 1F, 1F, 1F, Element.NONE, MiningLevel.IRON));
        IRON.addSpellCasterStats(new ToolStats(1.2F, 0F, 1F, 1.2F, 1F, 1F, 1F, 1F, Element.NONE, MiningLevel.IRON));

        GOLD.addStats(new ToolStats(0.1F, 1.2F, 0.75F, 2.4F, 0.6F, 0.9F, 0.7F, 0.2F, Element.MAGIC, MiningLevel.STONE));

        GOLD.addSpellCasterStats(new ToolStats(0.1F, 1.2F, 0.75F, 2.4F, 0.6F, 0.9F, 0.7F, 0.2F, Element.MAGIC, MiningLevel.STONE));
        AMETHYST.addStats(new ToolStats(0.9F, 0.25F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.MAGIC, MiningLevel.STONE));
        AMETHYST.addSpellCasterStats(new ToolStats(0.9F, 0.25F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.MAGIC, MiningLevel.STONE));

        REDSTONE.addStats(new ToolStats(0.5F, 0.8F, 0.65F, 1.1F, 0.3F, 1F, 1F, 1.2F, Element.SURGE, MiningLevel.IRON));
        REDSTONE.addSpellCasterStats(new ToolStats(0.5F, 0.8F, 0.65F, 1.1F, 0.3F, 1F, 1F, 1.2F, Element.SURGE, MiningLevel.IRON));

        FLUORITE.addStats(new ToolStats(0F, 1.4F, 1.1F, 0.9F, 0.1F, 0.7F, 1.2F, 0.5F, Element.PHANTOM, MiningLevel.STONE));
        FLUORITE.addSpellCasterStats(new ToolStats(0.3F, 1.2F, 1.1F, 0.9F, 0.1F, 0.7F, 2.0F, 0.5F, Element.PHANTOM, MiningLevel.STONE));

        DIAMOND.addStats(new ToolStats(1.5F, 0F, 1.1F, 1.6F, 0.8F, 1F, 1.2F, 4.0F, Element.NONE, MiningLevel.DIAMOND));
        DIAMOND.addSpellCasterStats(new ToolStats(1.5F, 0F, 1.1F, 1.6F, 0.8F, 1F, 1.2F, 4.0F, Element.NONE, MiningLevel.DIAMOND));

        ENDER_METAL.addStats(new ToolStats(0.8F, 0.6F, 0.5F, 1F, 0.7F, 1.4F, 0.6F, 2.7F, Element.HOLLOW, MiningLevel.IRON));
        ENDER_METAL.addSpellCasterStats(new ToolStats(0.8F, 0.6F, 0.5F, 1F, 0.7F, 1.4F, 0.6F, 2.7F, Element.HOLLOW, MiningLevel.IRON));

        NETHERITE.addStats(new ToolStats(1.25F, 0.5F, 0.9F, 1.8F, 1.1F, 1.1F, 1.1F, 5.0F, Element.EMBER, MiningLevel.NETHERITE));
        NETHERITE.addSpellCasterStats(new ToolStats(1.25F, 0.5F, 0.9F, 1.8F, 1.1F, 1.1F, 1.1F, 5.0F, Element.EMBER, MiningLevel.NETHERITE));

        FRIGIDITE.addStats(new ToolStats(1F, 1F, 1.3F, 1.7F, 0.9F, 0.7F, 0.6F, 6.1F, Element.GLACE, MiningLevel.NETHERITE));
        FRIGIDITE.addSpellCasterStats(new ToolStats(1F, 1F, 1.3F, 1.7F, 0.9F, 0.7F, 0.6F, 6.1F, Element.GLACE, MiningLevel.NETHERITE));

        TRANSLUCIUM.addStats(new ToolStats(1.5F, 0.7F, 0.9F, 1.7F, 1.2F, 1.1F, 1.2F, 6.3F, Element.PHANTOM, MiningLevel.NETHERITE));
        TRANSLUCIUM.addSpellCasterStats(new ToolStats(1.5F, 0.7F, 0.9F, 1.7F, 1.2F, 1.1F, 1.2F, 6.3F, Element.PHANTOM, MiningLevel.NETHERITE));

        RESONITE.addStats(new ToolStats(1.3F, 0.8F, 0.8F, 1.7F, 2.6F, 1.2F, 1F, 6.7F, Element.TREMOR, MiningLevel.NETHERITE));
        RESONITE.addSpellCasterStats(new ToolStats(1.3F, 0.8F, 0.8F, 1.7F, 2.6F, 1.2F, 1F, 6.7F, Element.TREMOR, MiningLevel.NETHERITE));

        ABYSSITE.addStats(new ToolStats(0.5F, 1.7F, 1F, 1.7F, 0.4F, 1.3F, 0.9F, 5.8F, Element.HOLLOW, MiningLevel.NETHERITE));
        ABYSSITE.addSpellCasterStats(new ToolStats(0.5F, 1.7F, 1F, 1.7F, 0.4F, 1.3F, 0.9F, 5.8F, Element.HOLLOW, MiningLevel.NETHERITE));
    }

    public static void registerElements() {
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
        ElementAffinityRegister.registerAffinity(Element.SURGE, ElementAffinityRegister.EFFICIENT, Element.TREMOR);
        ElementAffinityRegister.registerAffinity(Element.TREMOR, ElementAffinityRegister.EFFICIENT, Element.HOLLOW);
        ElementAffinityRegister.registerAffinity(Element.HOLLOW, ElementAffinityRegister.EFFICIENT, Element.PHANTOM);
        ElementAffinityRegister.registerAffinity(Element.PHANTOM, ElementAffinityRegister.EFFICIENT, Element.FLOW);
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
