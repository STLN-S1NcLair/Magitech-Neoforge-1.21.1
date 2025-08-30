package net.stln.magitech.item.tool.register;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.util.Element;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.material.MiningLevel;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.util.Map2d;

import java.util.HashMap;
import java.util.Map;

public class ToolMaterialRegister {
    private static final Map<ResourceLocation, ToolMaterial> dictId = new HashMap<>();
    // ツールタイプとインデックスからパーツを取得する
    private static final Map2d<ToolType, Integer, ToolPart> componentPartId = new Map2d<>();

    private static final Map<ToolType, ToolStats> baseStats = new HashMap<>();

    private static final Map<ToolType, ToolStats> modStats = new HashMap<>();

    public static void registerId(ResourceLocation id, ToolMaterial toolMaterial) {
        dictId.put(id, toolMaterial);
    }

    public static ToolMaterial getMaterial(ResourceLocation id) {
        return dictId.get(id);
    }

    public static Map<ResourceLocation, ToolMaterial> getDictId() {
        return dictId;
    }

    public static ToolPart getToolPartFromIndex(ToolType toolType, int index) {
        return componentPartId.get(toolType, index);
    }

    public static ToolStats getBaseStats(ToolType toolType) {
        return baseStats.get(toolType);
    }

    public static ToolStats getModStats(ToolType toolType) {
        return modStats.get(toolType);
    }

    public static void init() {
        componentPartId.put(ToolType.DAGGER, 0, ToolPart.LIGHT_HANDLE);
        componentPartId.put(ToolType.DAGGER, 1, ToolPart.LIGHT_BLADE);
        componentPartId.put(ToolType.DAGGER, 2, ToolPart.HANDGUARD);

        componentPartId.put(ToolType.LIGHT_SWORD, 0, ToolPart.LIGHT_HANDLE);
        componentPartId.put(ToolType.LIGHT_SWORD, 1, ToolPart.LIGHT_BLADE);
        componentPartId.put(ToolType.LIGHT_SWORD, 2, ToolPart.HANDGUARD);
        componentPartId.put(ToolType.LIGHT_SWORD, 3, ToolPart.TOOL_BINDING);

        componentPartId.put(ToolType.HEAVY_SWORD, 0, ToolPart.LIGHT_HANDLE);
        componentPartId.put(ToolType.HEAVY_SWORD, 1, ToolPart.HEAVY_BLADE);
        componentPartId.put(ToolType.HEAVY_SWORD, 2, ToolPart.TOOL_BINDING);
        componentPartId.put(ToolType.HEAVY_SWORD, 3, ToolPart.HANDGUARD);

        componentPartId.put(ToolType.PICKAXE, 0, ToolPart.HEAVY_HANDLE);
        componentPartId.put(ToolType.PICKAXE, 1, ToolPart.SPIKE_HEAD);
        componentPartId.put(ToolType.PICKAXE, 2, ToolPart.TOOL_BINDING);

        componentPartId.put(ToolType.HAMMER, 0, ToolPart.HEAVY_HANDLE);
        componentPartId.put(ToolType.HAMMER, 1, ToolPart.STRIKE_HEAD);
        componentPartId.put(ToolType.HAMMER, 2, ToolPart.PLATE);
        componentPartId.put(ToolType.HAMMER, 3, ToolPart.TOOL_BINDING);

        componentPartId.put(ToolType.AXE, 0, ToolPart.HEAVY_HANDLE);
        componentPartId.put(ToolType.AXE, 1, ToolPart.LIGHT_BLADE);
        componentPartId.put(ToolType.AXE, 2, ToolPart.STRIKE_HEAD);
        componentPartId.put(ToolType.AXE, 3, ToolPart.TOOL_BINDING);

        componentPartId.put(ToolType.SHOVEL, 0, ToolPart.HEAVY_HANDLE);
        componentPartId.put(ToolType.SHOVEL, 1, ToolPart.LIGHT_BLADE);
        componentPartId.put(ToolType.SHOVEL, 2, ToolPart.PLATE);
        componentPartId.put(ToolType.SHOVEL, 3, ToolPart.TOOL_BINDING);

        componentPartId.put(ToolType.SCYTHE, 0, ToolPart.REINFORCED_STICK);
        componentPartId.put(ToolType.SCYTHE, 1, ToolPart.HEAVY_HANDLE);
        componentPartId.put(ToolType.SCYTHE, 2, ToolPart.HEAVY_BLADE);
        componentPartId.put(ToolType.SCYTHE, 3, ToolPart.TOOL_BINDING);

        componentPartId.put(ToolType.WAND, 0, ToolPart.CATALYST);
        componentPartId.put(ToolType.WAND, 1, ToolPart.LIGHT_HANDLE);
        componentPartId.put(ToolType.WAND, 2, ToolPart.CONDUCTOR);
        componentPartId.put(ToolType.WAND, 3, ToolPart.TOOL_BINDING);

        baseStats.put(ToolType.DAGGER, new ToolStats(2F, 3F, 3.0F, 5F, 2F, 1.5F, 1F, 288, Element.NONE, MiningLevel.NONE, 0));
        baseStats.put(ToolType.LIGHT_SWORD, new ToolStats(4F, 4F, 1.6F, 5F, 2F, 3F, 3F, 361, Element.NONE, MiningLevel.NONE, 0));
        baseStats.put(ToolType.HEAVY_SWORD, new ToolStats(6F, 4F, 0.8F, 5F, 6F, 3F, 3F, 536, Element.NONE, MiningLevel.NONE, 0));
        baseStats.put(ToolType.PICKAXE, new ToolStats(2F, 1F, 2.4F, 5F, 1F, 2F, 1.5F, 319, Element.NONE, MiningLevel.NONE, 0));
        baseStats.put(ToolType.HAMMER, new ToolStats(8F, 7F, 0.6F, 5F, 4F, 2.5F, 2F, 1013, Element.NONE, MiningLevel.NONE, 0));
        baseStats.put(ToolType.AXE, new ToolStats(5F, 3F, 1.0F, 5F, 5F, 3.5F, 3F, 325, Element.NONE, MiningLevel.NONE, 0));
        baseStats.put(ToolType.SHOVEL, new ToolStats(3F, 3F, 2.0F, 5F, 6F, 3F, 4F, 401, Element.NONE, MiningLevel.NONE, 0));
        baseStats.put(ToolType.SCYTHE, new ToolStats(3F, 6F, 0.7F, 5F, 2F, 4.5F, 6F, 594, Element.NONE, MiningLevel.NONE, 0));
        baseStats.put(ToolType.WAND, new ToolStats(1F, 1F, 1F, 1F, 1F, 1F, 0.2F, 378, Element.NONE, MiningLevel.NONE, 0));

        modStats.put(ToolType.WAND, new ToolStats(1F, 1F, 1F, 1F, 1F, 1F, 0.2F, 378, Element.NONE, MiningLevel.NONE, 0));
    }
}
