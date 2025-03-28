package net.stln.magitech.item.tool;

import net.minecraft.world.item.Item;
import net.stln.magitech.util.Map2d;

import java.util.HashMap;
import java.util.Map;

public class ToolMaterialDictionary {
    private static final Map<Item, ToolMaterial> dictItem = new HashMap<>();
    private static final Map<String, ToolMaterial> dictId = new HashMap<>();
    // ツールタイプとインデックスからパーツを取得する
    private static final Map2d<ToolType, Integer, ToolPart> componentPartId = new Map2d<>();

    public static void registerItem(Item id, ToolMaterial toolMaterial) {
        dictItem.put(id, toolMaterial);
        toolMaterial.addMaterialItem(id);
    }

    public static void registerId(String id, ToolMaterial toolMaterial) {
        dictId.put(id, toolMaterial);
    }

    public static ToolMaterial getMaterial(Item id) {
        return dictItem.get(id);
    }

    public static ToolMaterial getMaterial(String id) {
        return dictId.get(id);
    }

    public static Map<String, ToolMaterial> getDictId() {
        return dictId;
    }

    public static ToolPart getToolPartFromIndex(ToolType toolType, int index) {
        return componentPartId.get(toolType, index);
    }

    public static void init() {
        componentPartId.put(ToolType.LIGHT_SWORD, 0, ToolPart.LIGHT_BLADE);
        componentPartId.put(ToolType.LIGHT_SWORD, 1, ToolPart.LIGHT_HANDLE);
        componentPartId.put(ToolType.LIGHT_SWORD, 2, ToolPart.HANDGUARD);
        componentPartId.put(ToolType.LIGHT_SWORD, 3, ToolPart.TOOL_BINDING);
    }
}
