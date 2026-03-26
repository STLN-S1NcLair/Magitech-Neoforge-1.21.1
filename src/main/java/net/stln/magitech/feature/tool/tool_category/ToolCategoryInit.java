package net.stln.magitech.feature.tool.tool_category;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.registry.DeferredToolCategory;
import net.stln.magitech.registry.DeferredToolCategoryRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ToolCategoryInit {
    public static final DeferredToolCategoryRegister REGISTER = new DeferredToolCategoryRegister(Magitech.MOD_ID);

    private static @NotNull DeferredToolCategory<ToolCategory> register(@NotNull String path, @NotNull ToolCategory toolGroup) {
        return REGISTER.register(path, () -> toolGroup);
    }

    // 全プロパティ
    public static final DeferredToolCategory<ToolCategory> ALL = register("all", new ToolCategory(MagitechRegistries.TOOL_PROPERTY.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toUnmodifiableList())));

    public static final DeferredToolCategory<ToolCategory> NONE = register("none", new ToolCategory(List.of()));

    // 近接武器のプロパティ一覧
    public static final DeferredToolCategory<ToolCategory> MELEE = register("melee", new ToolCategory(List.of(
            ToolPropertyInit.TIER,
            ToolPropertyInit.PROGRESSION,
            ToolPropertyInit.UPGRADE_POINT,
            ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT,
            ToolPropertyInit.DAMAGE,
            ToolPropertyInit.ELEMENTAL_DAMAGE,
            ToolPropertyInit.ATTACK_SPEED,
            ToolPropertyInit.MINING_SPEED,
            ToolPropertyInit.DEFENSE,
            ToolPropertyInit.REACH,
            ToolPropertyInit.SWEEP,
            ToolPropertyInit.MINING_LEVEL,
            ToolPropertyInit.DURATION
    )));

    // 魔法武器のプロパティ一覧
    public static final DeferredToolCategory<ToolCategory> CASTER = register("caster", new ToolCategory(List.of(
            ToolPropertyInit.TIER,
            ToolPropertyInit.PROGRESSION,
            ToolPropertyInit.UPGRADE_POINT,
            ToolPropertyInit.MAX_PROGRESSION_COEFFICIENT,
            ToolPropertyInit.POWER,
            ToolPropertyInit.ELEMENTAL_POWER,
            ToolPropertyInit.CHARGE_SPEED,
            ToolPropertyInit.COOLDOWN_SPEED,
            ToolPropertyInit.DEFENSE,
            ToolPropertyInit.LAUNCH,
            ToolPropertyInit.MANA_EFFICIENCY,
            ToolPropertyInit.DURATION
    )));

    public static void registerToolCategories(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Categories for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
