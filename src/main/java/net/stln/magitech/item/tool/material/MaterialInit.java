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
    public static ToolMaterial IRON = new ToolMaterial(new DuranceTrait(), "iron");
    public static ToolMaterial GOLD = new ToolMaterial(new CatalysisTrait(), "gold");
    public static ToolMaterial DIAMOND = new ToolMaterial(null, "diamond");
    public static ToolMaterial ENDER_METAL = new ToolMaterial(null, "ender_metal");
    public static ToolMaterial FRIGIDITE = new ToolMaterial(null, "frigidite");

    public static void registerMaterials() {
        ToolMaterialRegister.init();

        WOOD.addStats(ToolPart.LIGHT_BLADE , new ToolStats(0.5F, 0.3F, -1.4F, 0.7F, 0.0F, -0.2F, 1.6F, 26, Element.FLOW, MiningLevel.NONE));
        WOOD.addStats(ToolPart.HEAVY_BLADE , new ToolStats(2.3F, 0.4F, -2.1F, 0.7F, 1.3F, -0.2F, 1.6F, 79, Element.FLOW, MiningLevel.NONE));
        WOOD.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0.7F, 0.0F, -0.2F, 1.3F, 0.3F,  0.2F, 0.4F, 53, Element.FLOW, MiningLevel.NONE));
        WOOD.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 1.2F,  0.0F, 0.0F, 0.0F,  0.0F, 0.0F, 107, Element.FLOW, MiningLevel.NONE));
        WOOD.addStats(ToolPart.HANDGUARD   , new ToolStats(0.3F, 0.0F, -0.6F, 0.0F, 0.7F,  0.0F, 0.0F, 17, Element.FLOW, MiningLevel.NONE));

        STONE.addStats(ToolPart.LIGHT_BLADE , new ToolStats(2.5F, 0.0F, -2.0F, 2.3F, 0.0F, 0.3F, 1.9F, 42, Element.NONE, MiningLevel.STONE));
        STONE.addStats(ToolPart.HEAVY_BLADE , new ToolStats(5.9F, 0.0F, -2.8F, 2.3F, 2.5F, 0.3F, 1.9F, 103, Element.NONE, MiningLevel.STONE));
        STONE.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(1.0F, 0.0F, -0.4F, 1.7F, 1.0F, 0.1F, 0.5F, 59, Element.NONE, MiningLevel.STONE));
        STONE.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 0.0F,  0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 83, Element.NONE, MiningLevel.STONE));
        STONE.addStats(ToolPart.HANDGUARD   , new ToolStats(0.5F, 0.0F, -0.2F, 0.0F, 1.5F, 0.0F, 0.0F, 52, Element.NONE, MiningLevel.STONE));

        DEEPSLATE.addStats(ToolPart.LIGHT_BLADE , new ToolStats(1.7F, 0.7F, -1.6F, 2.9F, 0.0F,  0.1F, 1.6F, 56, Element.TREMOR, MiningLevel.STONE));
        DEEPSLATE.addStats(ToolPart.HEAVY_BLADE , new ToolStats(4.2F, 1.2F, -2.5F, 2.9F, 3.2F,  0.1F, 1.6F, 154, Element.TREMOR, MiningLevel.STONE));
        DEEPSLATE.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0.7F, 0.0F, -0.7F, 1.8F, 0.5F, -0.4F, 0.7F, 72, Element.TREMOR, MiningLevel.STONE));
        DEEPSLATE.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 0.8F,  0.0F, 0.0F, 0.0F,  0.0F, 0.0F, 67, Element.TREMOR, MiningLevel.STONE));
        DEEPSLATE.addStats(ToolPart.HANDGUARD   , new ToolStats(0.6F, 0.0F, -0.3F, 0.0F, 2.0F,  0.0F, 0.0F, 61, Element.TREMOR, MiningLevel.STONE));

        COPPER.addStats(ToolPart.LIGHT_BLADE , new ToolStats(1.0F, 0.5F, -1.7F, 2.0F, 0.0F, 0.1F, 2.2F, 74, Element.SURGE, MiningLevel.STONE));
        COPPER.addStats(ToolPart.HEAVY_BLADE , new ToolStats(3.6F, 0.7F, -2.2F, 2.0F, 1.6F, 0.1F, 2.2F, 173, Element.SURGE, MiningLevel.STONE));
        COPPER.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0.5F, 0.0F, -0.6F, 3.0F, 0.3F, 0.0F, 0.9F, 69, Element.SURGE, MiningLevel.STONE));
        COPPER.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 1.0F,  0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 82, Element.SURGE, MiningLevel.STONE));
        COPPER.addStats(ToolPart.HANDGUARD   , new ToolStats(1.5F, 0.0F, -0.5F, 0.0F, 0.8F, 0.0F, 0.0F, 63, Element.SURGE, MiningLevel.STONE));

        IRON.addStats(ToolPart.LIGHT_BLADE , new ToolStats(2.5F, 0.0F, -1.6F, 3.0F, 0.0F, 0.2F, 2.5F, 96, Element.NONE, MiningLevel.IRON));
        IRON.addStats(ToolPart.HEAVY_BLADE , new ToolStats(6.5F, 0.0F, -2.4F, 3.0F, 2.0F, 0.2F, 2.5F, 277, Element.NONE, MiningLevel.IRON));
        IRON.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(1.0F, 0.0F, -0.4F, 3.0F, 1.0F, 0.1F, 1.0F, 83, Element.NONE, MiningLevel.IRON));
        IRON.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 0.0F,  0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 103, Element.NONE, MiningLevel.IRON));
        IRON.addStats(ToolPart.HANDGUARD   , new ToolStats(1.5F, 0.0F, -0.4F, 0.0F, 1.5F, 0.0F, 0.0F, 75, Element.NONE, MiningLevel.IRON));

        GOLD.addStats(ToolPart.LIGHT_BLADE , new ToolStats(0.0F, 1.8F, -1.1F, 7.8F, 0.0F,  0.2F, 1.9F, 16, Element.MAGIC, MiningLevel.STONE));
        GOLD.addStats(ToolPart.HEAVY_BLADE , new ToolStats(4.3F, 0.6F, -1.7F, 7.8F, 2.1F,  0.2F, 1.9F, 134, Element.MAGIC, MiningLevel.STONE));
        GOLD.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0.3F, 0.0F, -0.5F, 4.2F, 0.3F, -0.3F, 1.0F, 57, Element.MAGIC, MiningLevel.STONE));
        GOLD.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 2.1F,  0.0F, 0.0F, 0.0F,  0.0F, 0.0F, 62, Element.NONE, MiningLevel.STONE));
        GOLD.addStats(ToolPart.HANDGUARD   , new ToolStats(0.8F, 0.0F, -1.1F, 0.0F, 0.6F,  0.0F, 0.0F, 75, Element.MAGIC, MiningLevel.STONE));

        DIAMOND.addStats(ToolPart.LIGHT_BLADE , new ToolStats(3.0F, 0.0F, -1.5F, 4.5F, 0.0F, 0.3F, 3.0F, 407, Element.NONE, MiningLevel.DIAMOND));
        DIAMOND.addStats(ToolPart.HEAVY_BLADE , new ToolStats(8.0F, 0.0F, -2.4F, 4.5F, 3.0F, 0.3F, 3.0F, 791, Element.NONE, MiningLevel.DIAMOND));
        DIAMOND.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(1.0F, 0.0F, -0.3F, 3.5F, 1.6F, 0.2F, 1.5F, 392, Element.NONE, MiningLevel.DIAMOND));
        DIAMOND.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 0.0F,  0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 509, Element.NONE, MiningLevel.DIAMOND));
        DIAMOND.addStats(ToolPart.HANDGUARD   , new ToolStats(2.0F, 0.0F, -0.3F, 0.0F, 2.4F, 0.0F, 0.0F, 354, Element.NONE, MiningLevel.DIAMOND));

        ENDER_METAL.addStats(ToolPart.LIGHT_BLADE , new ToolStats(2.2F, 0.6F, -1.8F, 3.2F, 0.0F, 1.2F, 1.1F, 197, Element.HOLLOW, MiningLevel.IRON));
        ENDER_METAL.addStats(ToolPart.HEAVY_BLADE, new ToolStats(7.2F, 0.9F, -2.3F, 3.2F, 2.6F, 1.2F, 1.1F, 395, Element.HOLLOW, MiningLevel.IRON));
        ENDER_METAL.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0.7F, 0.0F, -0.7F, 2.5F, 1.0F, 0.5F, 0.7F, 206, Element.HOLLOW, MiningLevel.IRON));
        ENDER_METAL.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 0.8F,  0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 374, Element.HOLLOW, MiningLevel.IRON));
        ENDER_METAL.addStats(ToolPart.HANDGUARD   , new ToolStats(1.3F, 0.0F, -0.4F, 0.0F, 1.5F, 0.0F, 0.0F, 126, Element.HOLLOW, MiningLevel.IRON));

        FRIGIDITE.addStats(ToolPart.LIGHT_BLADE , new ToolStats(2.1F, 3.5F, -1.8F, 5.6F, 0.0F, -0.3F, 1.7F, 621, Element.GLACE, MiningLevel.NETHERITE));
        FRIGIDITE.addStats(ToolPart.HEAVY_BLADE, new ToolStats(6.9F, 3.6F, -2.6F, 5.6F, 2.5F, -0.3F, 1.7F, 1054, Element.GLACE, MiningLevel.NETHERITE));
        FRIGIDITE.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0.4F, 0.0F, -0.2F, 4.3F, 0.5F, -0.1F, 0.2F, 584, Element.GLACE, MiningLevel.NETHERITE));
        FRIGIDITE.addStats(ToolPart.TOOL_BINDING, new ToolStats(0.0F, 1.4F,  0.0F, 0.0F, 0.0F,  0.0F, 0.0F, 690, Element.GLACE, MiningLevel.NETHERITE));
        FRIGIDITE.addStats(ToolPart.HANDGUARD   , new ToolStats(1.6F, 0.0F, -0.1F, 0.0F, 2.9F,  0.0F, 0.0F, 436, Element.GLACE, MiningLevel.NETHERITE));
        ToolMaterialRegister.registerId("wood", WOOD);
        ToolMaterialRegister.registerId("stone", STONE);
        ToolMaterialRegister.registerId("deepslate", DEEPSLATE);
        ToolMaterialRegister.registerId("copper", COPPER);
        ToolMaterialRegister.registerId("iron", IRON);
        ToolMaterialRegister.registerId("gold", GOLD);
        ToolMaterialRegister.registerId("diamond", DIAMOND);
        ToolMaterialRegister.registerId("ender_metal", ENDER_METAL);
        ToolMaterialRegister.registerId("frigidite", FRIGIDITE);
    }

    public static void registerMaterialItems() {
        ToolMaterialRegister.registerItem(Items.STRIPPED_OAK_LOG, WOOD);
        ToolMaterialRegister.registerItem(Items.STONE, STONE);
        ToolMaterialRegister.registerItem(Items.DEEPSLATE, DEEPSLATE);
        ToolMaterialRegister.registerItem(Items.COPPER_INGOT, COPPER);
        ToolMaterialRegister.registerItem(Items.IRON_INGOT, IRON);
        ToolMaterialRegister.registerItem(Items.GOLD_INGOT, GOLD);
        ToolMaterialRegister.registerItem(Items.DIAMOND, DIAMOND);
        ToolMaterialRegister.registerItem(ItemInit.ENDER_METAL_INGOT.get(), ENDER_METAL);
        ToolMaterialRegister.registerItem(ItemInit.POLISHED_FRIGIDITE.get(), FRIGIDITE);
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
