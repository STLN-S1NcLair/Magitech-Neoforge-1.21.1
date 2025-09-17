package net.stln.magitech.item.tool.register;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.element.Element;
import net.stln.magitech.item.tool.material.MiningLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ToolMaterialRegister {
    // ツールタイプとインデックスからパーツを取得する
    private static final @NotNull Table<ToolType, Integer, ToolPart> componentPartTable;
    private static final @NotNull Map<ToolType, ToolStats> baseStats;
    private static final @NotNull Map<ToolType, ToolStats> modStats;

    public static @Nullable ToolPart getToolPartFromIndex(@NotNull ToolType toolType, int index) {
        return componentPartTable.get(toolType, index);
    }

    public static @Nullable ToolStats getBaseStats(@NotNull ToolType toolType) {
        return baseStats.get(toolType);
    }

    public static @Nullable ToolStats getModStats(@NotNull ToolType toolType) {
        return modStats.get(toolType);
    }

    public static void init() {}

    static {
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

        ImmutableMap.Builder<ToolType, ToolStats> baseStatsBuilder = ImmutableMap.builder();
        baseStatsBuilder.put(ToolType.DAGGER, new ToolStats(2F, 3F, 3.0F, 5F, 2F, 1.5F, 1F, 288, Element.NONE, MiningLevel.NONE, 0));
        baseStatsBuilder.put(ToolType.LIGHT_SWORD, new ToolStats(4F, 4F, 1.6F, 5F, 2F, 3F, 3F, 361, Element.NONE, MiningLevel.NONE, 0));
        baseStatsBuilder.put(ToolType.HEAVY_SWORD, new ToolStats(6F, 4F, 0.8F, 5F, 6F, 3F, 3F, 536, Element.NONE, MiningLevel.NONE, 0));
        baseStatsBuilder.put(ToolType.PICKAXE, new ToolStats(2F, 1F, 2.4F, 5F, 1F, 2F, 1.5F, 319, Element.NONE, MiningLevel.NONE, 0));
        baseStatsBuilder.put(ToolType.HAMMER, new ToolStats(8F, 7F, 0.6F, 5F, 4F, 2.5F, 2F, 1013, Element.NONE, MiningLevel.NONE, 0));
        baseStatsBuilder.put(ToolType.AXE, new ToolStats(5F, 3F, 1.0F, 5F, 5F, 3.5F, 3F, 325, Element.NONE, MiningLevel.NONE, 0));
        baseStatsBuilder.put(ToolType.SHOVEL, new ToolStats(3F, 3F, 2.0F, 5F, 6F, 3F, 4F, 401, Element.NONE, MiningLevel.NONE, 0));
        baseStatsBuilder.put(ToolType.SCYTHE, new ToolStats(3F, 6F, 0.7F, 5F, 2F, 4.5F, 6F, 594, Element.NONE, MiningLevel.NONE, 0));
        baseStatsBuilder.put(ToolType.WAND, new ToolStats(1F, 1F, 1F, 1F, 1F, 1F, 0.2F, 378, Element.NONE, MiningLevel.NONE, 0));
        baseStats = baseStatsBuilder.build();

        ImmutableMap.Builder<ToolType, ToolStats> modStatsBuilder = ImmutableMap.builder();
        modStatsBuilder.put(ToolType.WAND, new ToolStats(1F, 1F, 1F, 1F, 1F, 1F, 0.2F, 378, Element.NONE, MiningLevel.NONE, 0));
        modStats = modStatsBuilder.build();
    }
}
