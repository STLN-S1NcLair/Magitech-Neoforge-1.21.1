package net.stln.magitech.item.tool;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.Items;
import net.stln.magitech.damage.ElementAffinityDictionary;
import net.stln.magitech.damage.EntityElementDictionary;
import net.stln.magitech.item.ItemInit;

public class MaterialInit {

    public static ToolMaterial COPPER = new ToolMaterial(null, "copper");
    public static ToolMaterial IRON = new ToolMaterial(null, "iron");
    public static ToolMaterial GOLD = new ToolMaterial(null, "gold");
    public static ToolMaterial DIAMOND = new ToolMaterial(null, "diamond");
    public static ToolMaterial ENDER_METAL = new ToolMaterial(null, "ender_metal");
    public static ToolMaterial FRIGIDITE = new ToolMaterial(null, "frigidite");

    public static void registerMaterials() {
        ToolMaterialDictionary.init();

        COPPER.addStats(ToolPart.LIGHT_BLADE, new ToolStats(2.5F, 0.5F, -2.5F, 2.5F, 0F, -0.3F, 0.7F, 57, Element.SURGE, MiningLevel.STONE));
        COPPER.addStats(ToolPart.HANDGUARD, new ToolStats(0F, 0.4F, -0.6F, 0F, 1.1F, 0F, 0F, 92, Element.SURGE, MiningLevel.STONE));
        COPPER.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0F, 0.2F, 0.1F, 1.3F, 0.3F, 0.1F, 1.6F, 115, Element.SURGE, MiningLevel.STONE));

        IRON.addStats(ToolPart.LIGHT_BLADE, new ToolStats(4F, 0F, -2F, 4.5F, 0F, 0F, 1F, 105, Element.NONE, MiningLevel.IRON));
        IRON.addStats(ToolPart.HANDGUARD, new ToolStats(0F, 0F, -0.7F, 0F, 2F, 0F, 0F, 200, Element.NONE, MiningLevel.IRON));
        IRON.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0F, 0F, 0.1F, 1.7F, 1F, 0F, 2F, 203, Element.NONE, MiningLevel.IRON));

        GOLD.addStats(ToolPart.LIGHT_BLADE, new ToolStats(0.5F, 2.5F, -1.9F, 9.5F, 0F, 0.3F, 1.5F, 32, Element.MAGIC, MiningLevel.STONE));
        GOLD.addStats(ToolPart.HANDGUARD, new ToolStats(-0.5F, 1.6F, -0.7F, 0F, 0.3F, 0F, 0F, 92, Element.MAGIC, MiningLevel.STONE));
        GOLD.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0F, 0.3F, 0.2F, 3.6F, 0.6F, 0F, 0.9F, 115, Element.NONE, MiningLevel.STONE));

        DIAMOND.addStats(ToolPart.LIGHT_BLADE, new ToolStats(6F, 0F, -1.5F, 8F, 0F, 0.5F, 2F, 306, Element.NONE, MiningLevel.DIAMOND));
        DIAMOND.addStats(ToolPart.HANDGUARD, new ToolStats(0F, 0F, -0.5F, 0F, 3F, 0F, 0F, 274, Element.NONE, MiningLevel.DIAMOND));
        DIAMOND.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0F, 0F, -0.3F, 3F, 2F, 0.2F, 2.4F, 498, Element.NONE, MiningLevel.DIAMOND));

        ENDER_METAL.addStats(ToolPart.LIGHT_BLADE, new ToolStats(1.7F, 2.5F, -2.3F, 5.2F, 0F, 1.9F, 0.9F, 120, Element.HOLLOW, MiningLevel.IRON));
        ENDER_METAL.addStats(ToolPart.HANDGUARD, new ToolStats(0F, 0.7F, -0.6F, 0F, 1.7F, 0F, 0F, 187, Element.HOLLOW, MiningLevel.IRON));
        ENDER_METAL.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0F, 0.3F, 0.3F, 1.1F, 0.8F, 0F, 1.5F, 218, Element.HOLLOW, MiningLevel.IRON));

        FRIGIDITE.addStats(ToolPart.LIGHT_BLADE, new ToolStats(2F, 6.5F, -1.5F, 8F, 0F, -0.3F, 1.1F, 496, Element.GLACE, MiningLevel.NETHERITE));
        FRIGIDITE.addStats(ToolPart.HANDGUARD, new ToolStats(0F, 3.2F, -0.5F, 0F, 5F, 0F, 0F, 518, Element.GLACE, MiningLevel.NETHERITE));
        FRIGIDITE.addStats(ToolPart.LIGHT_HANDLE, new ToolStats(0F, 0.3F, -0.3F, 3F, 3F, -0.2F, 0.5F, 604, Element.GLACE, MiningLevel.NETHERITE));
        ToolMaterialDictionary.registerId("copper", COPPER);
        ToolMaterialDictionary.registerItem(Items.COPPER_INGOT, COPPER);
        ToolMaterialDictionary.registerId("iron", IRON);
        ToolMaterialDictionary.registerItem(Items.IRON_INGOT, IRON);
        ToolMaterialDictionary.registerId("gold", GOLD);
        ToolMaterialDictionary.registerItem(Items.GOLD_INGOT, GOLD);
        ToolMaterialDictionary.registerId("diamond", DIAMOND);
        ToolMaterialDictionary.registerItem(Items.DIAMOND, DIAMOND);

        ToolMaterialDictionary.registerId("ender_metal", ENDER_METAL);
        ToolMaterialDictionary.registerItem(ItemInit.ENDER_METAL_INGOT.get(), ENDER_METAL);
        ToolMaterialDictionary.registerId("frigidite", FRIGIDITE);
        ToolMaterialDictionary.registerItem(ItemInit.POLISHED_FRIGIDITE.get(), FRIGIDITE);
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
