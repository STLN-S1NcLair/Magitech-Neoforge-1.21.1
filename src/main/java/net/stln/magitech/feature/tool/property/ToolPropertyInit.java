package net.stln.magitech.feature.tool.property;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.registry.DeferredToolPart;
import net.stln.magitech.registry.DeferredToolPartRegister;
import net.stln.magitech.registry.DeferredToolProperty;
import net.stln.magitech.registry.DeferredToolPropertyRegister;
import org.jetbrains.annotations.NotNull;

import javax.tools.Tool;
import java.awt.*;

public class ToolPropertyInit {

    public static final DeferredToolPropertyRegister REGISTER = new DeferredToolPropertyRegister(Magitech.MOD_ID);

    private static @NotNull DeferredToolProperty<IToolProperty<?>> register(@NotNull String path, @NotNull IToolProperty<?> toolProperty) {
        return REGISTER.register(path, () -> toolProperty);
    }

    // ツールのプロパティ: 素材と特性によって決まる性能値のリスト (現在耐久値や現在熟練度などの使用によって変化しうるものは除外、初期値は含む)
    public static final DeferredToolProperty<IToolProperty<?>> TIER = register("tier", new InitialIntegerToolProperty(new Color(0xFFFFFF)));
    public static final DeferredToolProperty<IToolProperty<?>> UPGRADE_POINT = register("upgrade_point", new InitialIntegerToolProperty(new Color(0xC0FF40)));
    public static final DeferredToolProperty<IToolProperty<?>> DEFENSE = register("defense", new SimpleAttributeToolProperty(Attributes.ARMOR, new Color(0x8080A0)));
    public static final DeferredToolProperty<IToolProperty<?>> DURATION = register("duration", new InitialIntegerToolProperty(new Color(0xFFFFFF)));

    // 近接武器
    public static final DeferredToolProperty<IToolProperty<?>> DAMAGE = register("damage", new SimpleAttributeToolProperty(Attributes.ATTACK_DAMAGE, new Color(0xFF4040)));
    public static final DeferredToolProperty<IToolProperty<?>> ELEMENTAL_DAMAGE = register("elemental_damage", new ElementalAttributeToolProperty(AttributeInit.ELEMENTAL_DAMAGE, new Color(0xFFFFFF)));
    public static final DeferredToolProperty<IToolProperty<?>> ATTACK_SPEED = register("attack_speed", new SimpleAttributeToolProperty(Attributes.ATTACK_SPEED, new Color(0xFFE040)));
    public static final DeferredToolProperty<IToolProperty<?>> MINING_SPEED = register("mining_speed", new SimpleAttributeToolProperty(AttributeInit.MINING_SPEED, new Color(0x40A0FF)));
    public static final DeferredToolProperty<IToolProperty<?>> REACH = register("reach", new SimpleAttributeToolProperty(Attributes.ENTITY_INTERACTION_RANGE, new Color(0x8040FF)));
    public static final DeferredToolProperty<IToolProperty<?>> SWEEP = register("sweep", new SimpleAttributeToolProperty(AttributeInit.SWEEP_RANGE, new Color(0x40FFA0)));
    public static final DeferredToolProperty<IToolProperty<?>> MINING_LEVEL = register("mining_level", new MiningLevelToolProperty(new Color(0xFFFFFF)));

    // 魔法武器
    public static final DeferredToolProperty<IToolProperty<?>> POWER = register("power", new SimpleAttributeToolProperty(Attributes.ATTACK_DAMAGE, new Color(0xFF4040)));
    public static final DeferredToolProperty<IToolProperty<?>> ELEMENTAL_POWER = register("elemental_power", new ElementalAttributeToolProperty(AttributeInit.ELEMENTAL_DAMAGE, new Color(0xFFFFFF)));
    public static final DeferredToolProperty<IToolProperty<?>> CHARGE_SPEED = register("charge_speed", new SimpleAttributeToolProperty(AttributeInit.CHARGE_SPEED, new Color(0xFFE040)));
    public static final DeferredToolProperty<IToolProperty<?>> COOLDOWN_SPEED = register("cooldown_speed", new SimpleAttributeToolProperty(AttributeInit.COOLDOWN_SPEED, new Color(0x40A0FF)));
    public static final DeferredToolProperty<IToolProperty<?>> PROJECTILE_SPEED = register("projectile_speed", new SimpleAttributeToolProperty(AttributeInit.PROJECTILE_SPEED, new Color(0x8040FF)));
    public static final DeferredToolProperty<IToolProperty<?>> MANA_EFFICIENCY = register("mana_efficiency", new SimpleAttributeToolProperty(AttributeInit.MANA_EFFICIENCY, new Color(0x40FFA0)));

    public static void registerToolProperties(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Properties for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
