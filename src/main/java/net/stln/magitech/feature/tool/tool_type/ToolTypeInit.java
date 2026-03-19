package net.stln.magitech.feature.tool.tool_type;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.part.ToolPartInit;
import net.stln.magitech.feature.tool.property.ElementalAttributeToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.feature.tool.tool_group.ToolGroupInit;
import net.stln.magitech.registry.DeferredToolType;
import net.stln.magitech.registry.DeferredToolTypeRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToolTypeInit {
    public static final DeferredToolTypeRegister REGISTER = new DeferredToolTypeRegister(Magitech.MOD_ID);

    private static @NotNull DeferredToolType<ToolType> register(@NotNull String path, @NotNull ToolType toolType) {
        return REGISTER.register(path, () -> toolType);
    }

    public static final DeferredToolType<ToolType> DAGGER = register("dagger", new ToolType(
            new ToolProperties(ToolGroupInit.MELEE)
                    .set(ToolPropertyInit.DAMAGE, 2.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(3.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 3.0)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 2.0)
                    .set(ToolPropertyInit.REACH, 2.5)
                    .set(ToolPropertyInit.SWEEP, 1.0)
                    .set(ToolPropertyInit.DURATION, 288),
            List.of(ToolPartInit.LIGHT_HANDLE,
                    ToolPartInit.LIGHT_BLADE,
                    ToolPartInit.HANDGUARD)
            ));

    public static final DeferredToolType<ToolType> LIGHT_SWORD = register("light_sword", new ToolType(
            new ToolProperties(ToolGroupInit.MELEE)
                    .set(ToolPropertyInit.DAMAGE, 4.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(4.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 1.6)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 2.0)
                    .set(ToolPropertyInit.REACH, 3.0)
                    .set(ToolPropertyInit.SWEEP, 3.0)
                    .set(ToolPropertyInit.DURATION, 361),
            List.of(ToolPartInit.LIGHT_HANDLE,
                    ToolPartInit.LIGHT_BLADE,
                    ToolPartInit.HANDGUARD,
                    ToolPartInit.TOOL_BINDING)
            ));

    public static final DeferredToolType<ToolType> HEAVY_SWORD = register("heavy_sword", new ToolType(
            new ToolProperties(ToolGroupInit.MELEE)
                    .set(ToolPropertyInit.DAMAGE, 6.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(4.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 0.8)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 6.0)
                    .set(ToolPropertyInit.REACH, 3.0)
                    .set(ToolPropertyInit.SWEEP, 3.0)
                    .set(ToolPropertyInit.DURATION, 536),
            List.of(ToolPartInit.LIGHT_HANDLE,
                    ToolPartInit.HEAVY_BLADE,
                    ToolPartInit.HANDGUARD,
                    ToolPartInit.TOOL_BINDING)
            ));

    public static final DeferredToolType<ToolType> PICKAXE = register("pickaxe", new ToolType(
            new ToolProperties(ToolGroupInit.MELEE)
                    .set(ToolPropertyInit.DAMAGE, 2.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(1.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 2.4)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 1.0)
                    .set(ToolPropertyInit.REACH, 2.5)
                    .set(ToolPropertyInit.SWEEP, 1.5)
                    .set(ToolPropertyInit.DURATION, 319),
            List.of(ToolPartInit.HEAVY_HANDLE,
                    ToolPartInit.SPIKE_HEAD,
                    ToolPartInit.TOOL_BINDING)
            ));

    public static final DeferredToolType<ToolType> HAMMER = register("hammer", new ToolType(
            new ToolProperties(ToolGroupInit.MELEE)
                    .set(ToolPropertyInit.DAMAGE, 8.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(7.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 0.6)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 4.0)
                    .set(ToolPropertyInit.REACH, 2.5)
                    .set(ToolPropertyInit.SWEEP, 2.0)
                    .set(ToolPropertyInit.DURATION, 1013),
            List.of(ToolPartInit.HEAVY_HANDLE,
                    ToolPartInit.STRIKE_HEAD,
                    ToolPartInit.PLATE,
                    ToolPartInit.TOOL_BINDING)
            ));

    public static final DeferredToolType<ToolType> AXE = register("axe", new ToolType(
            new ToolProperties(ToolGroupInit.MELEE)
                    .set(ToolPropertyInit.DAMAGE, 5.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(3.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 1.0)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 5.0)
                    .set(ToolPropertyInit.REACH, 3.5)
                    .set(ToolPropertyInit.SWEEP, 3.0)
                    .set(ToolPropertyInit.DURATION, 325),
            List.of(ToolPartInit.HEAVY_HANDLE,
                    ToolPartInit.LIGHT_BLADE,
                    ToolPartInit.STRIKE_HEAD,
                    ToolPartInit.TOOL_BINDING)
            ));

    public static final DeferredToolType<ToolType> SHOVEL = register("shovel", new ToolType(
            new ToolProperties(ToolGroupInit.MELEE)
                    .set(ToolPropertyInit.DAMAGE, 3.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(3.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 2.0)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 6.0)
                    .set(ToolPropertyInit.REACH, 3.0)
                    .set(ToolPropertyInit.SWEEP, 4.0)
                    .set(ToolPropertyInit.DURATION, 401),
            List.of(ToolPartInit.HEAVY_HANDLE,
                    ToolPartInit.LIGHT_BLADE,
                    ToolPartInit.PLATE,
                    ToolPartInit.TOOL_BINDING)
            ));

    public static final DeferredToolType<ToolType> SCYTHE = register("scythe", new ToolType(
            new ToolProperties(ToolGroupInit.MELEE)
                    .set(ToolPropertyInit.DAMAGE, 3.0)
                    .set(ToolPropertyInit.ELEMENTAL_DAMAGE, ElementalAttributeToolProperty.flatValue(6.0))
                    .set(ToolPropertyInit.ATTACK_SPEED, 0.7)
                    .set(ToolPropertyInit.MINING_SPEED, 5.0)
                    .set(ToolPropertyInit.DEFENSE, 2.0)
                    .set(ToolPropertyInit.REACH, 4.5)
                    .set(ToolPropertyInit.SWEEP, 6.0)
                    .set(ToolPropertyInit.DURATION, 594),
            List.of(ToolPartInit.REINFORCED_ROD,
                    ToolPartInit.HEAVY_HANDLE,
                    ToolPartInit.HEAVY_BLADE,
                    ToolPartInit.TOOL_BINDING)
            ));

    public static final DeferredToolType<ToolType> WAND = register("wand", new ToolType(
            new ToolProperties(ToolGroupInit.CASTER)
                    .set(ToolPropertyInit.POWER, 1.0)
                    .set(ToolPropertyInit.ELEMENTAL_POWER, ElementalAttributeToolProperty.flatValue(1.0))
                    .set(ToolPropertyInit.CHARGE_SPEED, 1.0)
                    .set(ToolPropertyInit.COOLDOWN_SPEED, 1.0)
                    .set(ToolPropertyInit.DEFENSE, 1.0)
                    .set(ToolPropertyInit.PROJECTILE_SPEED, 1.0)
                    .set(ToolPropertyInit.MANA_EFFICIENCY, 1.0)
                    .set(ToolPropertyInit.DURATION, 378),
            List.of(ToolPartInit.REINFORCED_ROD,
                    ToolPartInit.HEAVY_HANDLE,
                    ToolPartInit.PLATE,
                    ToolPartInit.TOOL_BINDING)
            ));

    public static void registerToolTypes(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Types for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
