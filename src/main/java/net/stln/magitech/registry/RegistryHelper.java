package net.stln.magitech.registry;

import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperty;
import net.stln.magitech.feature.tool.tool_type.ToolType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RegistryHelper {

    public static List<ToolType> registeredToolTypes() {
        return MagitechRegistries.TOOL_TYPE.entrySet().stream().map(Map.Entry::getValue).sorted(Comparator.comparingDouble(ToolType::order)).toList();
    }

    public static List<ToolPart> registeredToolParts() {
        return MagitechRegistries.TOOL_PART.entrySet().stream().map(Map.Entry::getValue).sorted(Comparator.comparingDouble(ToolPart::order)).toList();
    }

    public static List<ToolMaterial> registeredToolMaterials() {
        return MagitechRegistries.TOOL_MATERIAL.entrySet().stream().map(Map.Entry::getValue).sorted(Comparator.comparingDouble(ToolMaterial::order)).toList();
    }

    public static List<? extends IToolProperty<?>> registeredToolProperties() {
        return MagitechRegistries.TOOL_PROPERTY.entrySet().stream().map(Map.Entry::getValue).sorted(Comparator.comparingDouble(IToolProperty::order)).toList();
    }

}
