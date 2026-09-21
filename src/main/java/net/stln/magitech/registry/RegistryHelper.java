package net.stln.magitech.registry;

import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.stream.Stream;

public class RegistryHelper {

    public static @NotNull Stream<ToolType> registeredToolTypes() {
        return MagitechRegistries.TOOL_TYPE.stream().sorted(Comparator.comparingDouble(ToolType::order));
    }

    public static @NotNull Stream<ToolPart> registeredToolParts() {
        return MagitechRegistries.TOOL_PART.stream().sorted(Comparator.comparingDouble(ToolPart::order));
    }

    public static @NotNull Stream<ToolMaterial> registeredToolMaterials() {
        return MagitechRegistries.TOOL_MATERIAL.stream().sorted(Comparator.comparingDouble(ToolMaterial::order));
    }

    public static @NotNull Stream<? extends IToolProperty<?>> registeredToolProperties() {
        return MagitechRegistries.TOOL_PROPERTY.stream().sorted(Comparator.comparingDouble(IToolProperty::order));
    }

}
