package net.stln.magitech.item.tool.material;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.Items;
import net.stln.magitech.damage.ElementAffinityDictionary;
import net.stln.magitech.damage.EntityElementDictionary;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.trait.*;

public class MaterialInit {

    public static ToolMaterial WOOD = new ToolMaterial(new AdaptationTrait(), "wood");
    public static ToolMaterial STONE = new ToolMaterial(new GeoMendingTrait(), "stone");
    public static ToolMaterial DEEPSLATE = new ToolMaterial(new HardmineTrait(), "deepslate");
    public static ToolMaterial COPPER = new ToolMaterial(new ConductanceTrait(), "copper");
    public static ToolMaterial BONE = new ToolMaterial(new FossilizationTrait(), "bone");
    public static ToolMaterial IRON = new ToolMaterial(new DuranceTrait(), "iron");
    public static ToolMaterial GOLD = new ToolMaterial(new CatalysisTrait(), "gold");
    public static ToolMaterial AMETHYST = new ToolMaterial(new ShatterforceTrait(), "amethyst");
    public static ToolMaterial REDSTONE = new ToolMaterial(new SignalRushTrait(), "redstone");
    public static ToolMaterial DIAMOND = new ToolMaterial(new LightweightTrait(), "diamond");
    public static ToolMaterial ENDER_METAL = new ToolMaterial(new EnderDrawTrait(), "ender_metal");
    public static ToolMaterial NETHERITE = new ToolMaterial(new LavaforgedTrait(), "netherite");
    public static ToolMaterial FRIGIDITE = new ToolMaterial(new ShatterpiercerTrait(), "frigidite");
    public static ToolMaterial TRANSLUCIUM = new ToolMaterial(new PhantomSlayerTrait(), "translucium");

    public static void registerMaterials() {
        ToolMaterialRegister.init();

        WOOD.addStats(new ToolStats(0.4F, 0.4F, 1.1F, 0.4F, 0.5F, 0.9F, 0.7F, 0.3F, Element.FLOW, MiningLevel.NONE));

        STONE.addStats(new ToolStats(1F, 0F, 0.85F, 0.8F, 1.5F, 1F, 0.8F, 0.5F, Element.NONE, MiningLevel.STONE));

        DEEPSLATE.addStats(new ToolStats(0.7F, 0.35F, 0.7F, 0.9F, 2F, 1F, 0.8F, 0.6F, Element.TREMOR, MiningLevel.STONE));

        COPPER.addStats(new ToolStats(0.8F, 0.3F, 0.8F, 1F, 0.8F, 0.8F, 0.9F, 0.8F, Element.SURGE, MiningLevel.STONE));

        BONE.addStats(new ToolStats(1.1F, 0F, 1.05F, 0.6F, 1.2F, 0.7F, 0.8F, 0.7F, Element.NONE, MiningLevel.STONE));

        IRON.addStats(new ToolStats(1.2F, 0F, 1F, 1.2F, 1F, 1F, 1F, 1F, Element.NONE, MiningLevel.IRON));

        GOLD.addStats(new ToolStats(0.1F, 1.2F, 0.75F, 2.4F, 0.6F, 0.9F, 0.7F, 0.2F, Element.MAGIC, MiningLevel.STONE));

        AMETHYST.addStats(new ToolStats(0.9F, 0.25F, 0.9F, 1F, 0.75F, 1.1F, 1.1F, 0.8F, Element.MAGIC, MiningLevel.STONE));

        REDSTONE.addStats(new ToolStats(0.5F, 0.8F, 0.65F, 1.1F, 0.3F, 1F, 1F, 1.2F, Element.SURGE, MiningLevel.IRON));

        DIAMOND.addStats(new ToolStats(1.5F, 0F, 1.1F, 1.6F, 0.8F, 1F, 1.2F, 2.5F, Element.NONE, MiningLevel.DIAMOND));

        ENDER_METAL.addStats(new ToolStats(0.8F, 0.6F, 0.5F, 1F, 0.7F, 1.4F, 0.6F, 1.7F, Element.HOLLOW, MiningLevel.IRON));

        NETHERITE.addStats(new ToolStats(1.25F, 0.5F, 0.9F, 1.8F, 1.1F, 1.1F, 1.1F, 3F, Element.EMBER, MiningLevel.NETHERITE));

        FRIGIDITE.addStats(new ToolStats(1F, 1F, 1.3F, 1.7F, 0.9F, 0.7F, 0.6F, 3.5F, Element.GLACE, MiningLevel.NETHERITE));

        TRANSLUCIUM.addStats(new ToolStats(1.5F, 0.7F, 0.9F, 1.7F, 1.2F, 1.1F, 1.2F, 3.5F, Element.PHANTOM, MiningLevel.NETHERITE));
        ToolMaterialRegister.registerId("wood", WOOD);
        ToolMaterialRegister.registerId("stone", STONE);
        ToolMaterialRegister.registerId("deepslate", DEEPSLATE);
        ToolMaterialRegister.registerId("copper", COPPER);
        ToolMaterialRegister.registerId("bone", BONE);
        ToolMaterialRegister.registerId("iron", IRON);
        ToolMaterialRegister.registerId("gold", GOLD);
        ToolMaterialRegister.registerId("amethyst", AMETHYST);
        ToolMaterialRegister.registerId("redstone", REDSTONE);
        ToolMaterialRegister.registerId("diamond", DIAMOND);
        ToolMaterialRegister.registerId("ender_metal", ENDER_METAL);
        ToolMaterialRegister.registerId("netherite", NETHERITE);
        ToolMaterialRegister.registerId("frigidite", FRIGIDITE);
        ToolMaterialRegister.registerId("translucium", TRANSLUCIUM);
    }

    public static void registerMaterialItems() {
        ToolMaterialRegister.registerItem(Items.STRIPPED_OAK_LOG, WOOD);
        ToolMaterialRegister.registerItem(Items.STONE, STONE);
        ToolMaterialRegister.registerItem(Items.DEEPSLATE, DEEPSLATE);
        ToolMaterialRegister.registerItem(Items.COPPER_INGOT, COPPER);
        ToolMaterialRegister.registerItem(Items.BONE, BONE);
        ToolMaterialRegister.registerItem(Items.IRON_INGOT, IRON);
        ToolMaterialRegister.registerItem(Items.GOLD_INGOT, GOLD);
        ToolMaterialRegister.registerItem(Items.AMETHYST_SHARD, AMETHYST);
        ToolMaterialRegister.registerItem(Items.DIAMOND, DIAMOND);
        ToolMaterialRegister.registerItem(Items.NETHERITE_INGOT, NETHERITE);
        ToolMaterialRegister.registerItem(ItemInit.ENDER_METAL_INGOT.get(), ENDER_METAL);
        ToolMaterialRegister.registerItem(ItemInit.POLISHED_FRIGIDITE.get(), FRIGIDITE);
        ToolMaterialRegister.registerItem(ItemInit.POLISHED_TRANSLUCIUM.get(), TRANSLUCIUM);
    }

    public static void registerElements() {
        ElementAffinityDictionary.registerAffinity(Element.EMBER, ElementAffinityDictionary.INEFFICIENT, Element.EMBER);
        ElementAffinityDictionary.registerAffinity(Element.GLACE, ElementAffinityDictionary.INEFFICIENT, Element.GLACE);
        ElementAffinityDictionary.registerAffinity(Element.SURGE, ElementAffinityDictionary.INEFFICIENT, Element.SURGE);
        ElementAffinityDictionary.registerAffinity(Element.PHANTOM, ElementAffinityDictionary.INEFFICIENT, Element.PHANTOM);
        ElementAffinityDictionary.registerAffinity(Element.TREMOR, ElementAffinityDictionary.INEFFICIENT, Element.TREMOR);
        ElementAffinityDictionary.registerAffinity(Element.MAGIC, ElementAffinityDictionary.INEFFICIENT, Element.MAGIC);
        ElementAffinityDictionary.registerAffinity(Element.FLOW, ElementAffinityDictionary.INEFFICIENT, Element.FLOW);
        ElementAffinityDictionary.registerAffinity(Element.HOLLOW, ElementAffinityDictionary.INEFFICIENT, Element.HOLLOW);

        ElementAffinityDictionary.registerAffinity(Element.EMBER, ElementAffinityDictionary.EFFICIENT, Element.SURGE);
        ElementAffinityDictionary.registerAffinity(Element.SURGE, ElementAffinityDictionary.EFFICIENT, Element.TREMOR);
        ElementAffinityDictionary.registerAffinity(Element.TREMOR, ElementAffinityDictionary.EFFICIENT, Element.GLACE);
        ElementAffinityDictionary.registerAffinity(Element.GLACE, ElementAffinityDictionary.EFFICIENT, Element.PHANTOM);
        ElementAffinityDictionary.registerAffinity(Element.PHANTOM, ElementAffinityDictionary.EFFICIENT, Element.MAGIC);
        ElementAffinityDictionary.registerAffinity(Element.MAGIC, ElementAffinityDictionary.EFFICIENT, Element.FLOW);
        ElementAffinityDictionary.registerAffinity(Element.FLOW, ElementAffinityDictionary.EFFICIENT, Element.HOLLOW);
        ElementAffinityDictionary.registerAffinity(Element.HOLLOW, ElementAffinityDictionary.EFFICIENT, Element.EMBER);

        ElementAffinityDictionary.registerAffinity(Element.EMBER, ElementAffinityDictionary.EFFICIENT, Element.GLACE);
        ElementAffinityDictionary.registerAffinity(Element.GLACE, ElementAffinityDictionary.EFFICIENT, Element.MAGIC);
        ElementAffinityDictionary.registerAffinity(Element.MAGIC, ElementAffinityDictionary.EFFICIENT, Element.SURGE);
        ElementAffinityDictionary.registerAffinity(Element.SURGE, ElementAffinityDictionary.EFFICIENT, Element.TREMOR);
        ElementAffinityDictionary.registerAffinity(Element.TREMOR, ElementAffinityDictionary.EFFICIENT, Element.HOLLOW);
        ElementAffinityDictionary.registerAffinity(Element.HOLLOW, ElementAffinityDictionary.EFFICIENT, Element.PHANTOM);
        ElementAffinityDictionary.registerAffinity(Element.PHANTOM, ElementAffinityDictionary.EFFICIENT, Element.FLOW);
        ElementAffinityDictionary.registerAffinity(Element.FLOW, ElementAffinityDictionary.EFFICIENT, Element.EMBER);

        EntityElementDictionary.registerEntityElement(Blaze.class, Element.EMBER);
        EntityElementDictionary.registerEntityElement(MagmaCube.class, Element.EMBER);
        EntityElementDictionary.registerEntityElement(Stray.class, Element.GLACE);
        EntityElementDictionary.registerEntityElement(Phantom.class, Element.PHANTOM);
        EntityElementDictionary.registerEntityElement(Vex.class, Element.PHANTOM);
        EntityElementDictionary.registerEntityElement(Warden.class, Element.TREMOR);
        EntityElementDictionary.registerEntityElement(Witch.class, Element.MAGIC);
        EntityElementDictionary.registerEntityElement(Evoker.class, Element.MAGIC);
        EntityElementDictionary.registerEntityElement(Drowned.class, Element.FLOW);
        EntityElementDictionary.registerEntityElement(Guardian.class, Element.FLOW);
        EntityElementDictionary.registerEntityElement(ElderGuardian.class, Element.FLOW);
        EntityElementDictionary.registerEntityElement(Breeze.class, Element.FLOW);
        EntityElementDictionary.registerEntityElement(EnderMan.class, Element.HOLLOW);
        EntityElementDictionary.registerEntityElement(EnderDragon.class, Element.HOLLOW);
        EntityElementDictionary.registerEntityElement(Endermite.class, Element.HOLLOW);
        EntityElementDictionary.registerEntityElement(Shulker.class, Element.HOLLOW);
    }
}
