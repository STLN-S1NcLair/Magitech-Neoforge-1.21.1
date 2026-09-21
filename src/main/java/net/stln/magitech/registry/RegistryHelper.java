package net.stln.magitech.registry;

import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Comparator;

public class RegistryHelper {

    public static @NotNull @Unmodifiable Collection<ToolType> registeredToolTypes() {
        return MagitechRegistries.TOOL_TYPE.stream().sorted(Comparator.comparingDouble(ToolType::order)).toList();
    }

    public static @NotNull @Unmodifiable Collection<ToolPart> registeredToolParts() {
        return MagitechRegistries.TOOL_PART.stream().sorted(Comparator.comparingDouble(ToolPart::order)).toList();
    }

    public static @NotNull @Unmodifiable Collection<ToolMaterial> registeredToolMaterials() {
        return MagitechRegistries.TOOL_MATERIAL.stream().sorted(Comparator.comparingDouble(ToolMaterial::order)).toList();
    }

    public static @NotNull @Unmodifiable Collection<? extends IToolProperty<?>> registeredToolProperties() {
        return MagitechRegistries.TOOL_PROPERTY.stream().sorted(Comparator.comparingDouble(IToolProperty::order)).toList();
    }

}
