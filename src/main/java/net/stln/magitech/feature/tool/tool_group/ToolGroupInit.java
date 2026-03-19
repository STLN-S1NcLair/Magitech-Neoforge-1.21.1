package net.stln.magitech.feature.tool.tool_group;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.registry.DeferredToolGroup;
import net.stln.magitech.registry.DeferredToolGroupRegister;
import net.stln.magitech.registry.DeferredToolPart;
import net.stln.magitech.registry.DeferredToolPartRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToolGroupInit {
    public static final DeferredToolGroupRegister REGISTER = new DeferredToolGroupRegister(Magitech.MOD_ID);

    private static @NotNull DeferredToolGroup<ToolGroup> register(@NotNull String path, @NotNull ToolGroup toolGroup) {
        return REGISTER.register(path, () -> toolGroup);
    }

    // 近接武器のプロパティ一覧
    public static final DeferredToolGroup<ToolGroup> MELEE = register("melee", new ToolGroup(List.of(
            ToolPropertyInit.TIER,
            ToolPropertyInit.UPGRADE_POINT,
            ToolPropertyInit.DAMAGE,
            ToolPropertyInit.ELEMENTAL_DAMAGE,
            ToolPropertyInit.ATTACK_SPEED,
            ToolPropertyInit.MINING_SPEED,
            ToolPropertyInit.DEFENSE,
            ToolPropertyInit.REACH,
            ToolPropertyInit.SWEEP,
            ToolPropertyInit.DURATION
    )));

    // 魔法武器のプロパティ一覧
    public static final DeferredToolGroup<ToolGroup> CASTER = register("caster", new ToolGroup(List.of(
            ToolPropertyInit.TIER,
            ToolPropertyInit.UPGRADE_POINT,
            ToolPropertyInit.POWER,
            ToolPropertyInit.ELEMENTAL_POWER,
            ToolPropertyInit.CHARGE_SPEED,
            ToolPropertyInit.COOLDOWN_SPEED,
            ToolPropertyInit.DEFENSE,
            ToolPropertyInit.PROJECTILE_SPEED,
            ToolPropertyInit.MANA_EFFICIENCY,
            ToolPropertyInit.DURATION
    )));

    public static void registerToolParts(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Groups for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
