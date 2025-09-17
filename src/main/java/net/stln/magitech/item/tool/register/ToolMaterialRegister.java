package net.stln.magitech.item.tool.register;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.material.MiningLevel;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.util.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ToolMaterialRegister {
    private static final Map<ResourceLocation, ToolMaterial> dictId = new HashMap<>();
    // ツールタイプとインデックスからパーツを取得する
    private static Table<ToolType, Integer, ToolPart> componentPartTable;

    private static final Map<ToolType, ToolStats> baseStats = new HashMap<>();

    private static final Map<ToolType, ToolStats> modStats = new HashMap<>();

    public static void registerId(@NotNull ResourceLocation id, @NotNull ToolMaterial toolMaterial) {
        dictId.put(id, toolMaterial);
    }

    public static @Nullable ToolMaterial getMaterial(@NotNull ResourceLocation id) {
        return dictId.get(id);
    }

    public static @NotNull Map<ResourceLocation, ToolMaterial> getDictId() {
        return dictId;
    }

    public static @Nullable ToolPart getToolPartFromIndex(@NotNull ToolType toolType, int index) {
        return componentPartTable == null ? null : componentPartTable.get(toolType, index);
    }

    public static @Nullable ToolStats getBaseStats(@NotNull ToolType toolType) {
        return baseStats.get(toolType);
    }

    public static @Nullable ToolStats getModStats(@NotNull ToolType toolType) {
        return modStats.get(toolType);
    }

    public static void init() {
        var builder = ImmutableTable.<ToolType, Integer, ToolPart>builder();
        builder.put(ToolType.DAGGER, 0, ToolPart.LIGHT_HANDLE);
        builder.put(ToolType.DAGGER, 1, ToolPart.LIGHT_BLADE);
        builder.put(ToolType.DAGGER, 2, ToolPart.HANDGUARD);

        builder.put(ToolType.LIGHT_SWORD, 0, ToolPart.LIGHT_HANDLE);
        builder.put(ToolType.LIGHT_SWORD, 1, ToolPart.LIGHT_BLADE);
        builder.put(ToolType.LIGHT_SWORD, 2, ToolPart.HANDGUARD);
        builder.put(ToolType.LIGHT_SWORD, 3, ToolPart.TOOL_BINDING);

        builder.put(ToolType.HEAVY_SWORD, 0, ToolPart.LIGHT_HANDLE);
        builder.put(ToolType.HEAVY_SWORD, 1, ToolPart.HEAVY_BLADE);
        builder.put(ToolType.HEAVY_SWORD, 2, ToolPart.TOOL_BINDING);
        builder.put(ToolType.HEAVY_SWORD, 3, ToolPart.HANDGUARD);

        builder.put(ToolType.PICKAXE, 0, ToolPart.HEAVY_HANDLE);
        builder.put(ToolType.PICKAXE, 1, ToolPart.SPIKE_HEAD);
        builder.put(ToolType.PICKAXE, 2, ToolPart.TOOL_BINDING);

        builder.put(ToolType.HAMMER, 0, ToolPart.HEAVY_HANDLE);
        builder.put(ToolType.HAMMER, 1, ToolPart.STRIKE_HEAD);
        builder.put(ToolType.HAMMER, 2, ToolPart.PLATE);
        builder.put(ToolType.HAMMER, 3, ToolPart.TOOL_BINDING);

        builder.put(ToolType.AXE, 0, ToolPart.HEAVY_HANDLE);
        builder.put(ToolType.AXE, 1, ToolPart.LIGHT_BLADE);
        builder.put(ToolType.AXE, 2, ToolPart.STRIKE_HEAD);
        builder.put(ToolType.AXE, 3, ToolPart.TOOL_BINDING);

        builder.put(ToolType.SHOVEL, 0, ToolPart.HEAVY_HANDLE);
        builder.put(ToolType.SHOVEL, 1, ToolPart.LIGHT_BLADE);
        builder.put(ToolType.SHOVEL, 2, ToolPart.PLATE);
        builder.put(ToolType.SHOVEL, 3, ToolPart.TOOL_BINDING);

        builder.put(ToolType.SCYTHE, 0, ToolPart.REINFORCED_STICK);
        builder.put(ToolType.SCYTHE, 1, ToolPart.HEAVY_HANDLE);
        builder.put(ToolType.SCYTHE, 2, ToolPart.HEAVY_BLADE);
        builder.put(ToolType.SCYTHE, 3, ToolPart.TOOL_BINDING);

        builder.put(ToolType.WAND, 0, ToolPart.CATALYST);
        builder.put(ToolType.WAND, 1, ToolPart.LIGHT_HANDLE);
        builder.put(ToolType.WAND, 2, ToolPart.CONDUCTOR);
        builder.put(ToolType.WAND, 3, ToolPart.TOOL_BINDING);
        
        componentPartTable = builder.build();

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
