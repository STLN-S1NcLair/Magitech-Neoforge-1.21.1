package net.stln.magitech.feature.tool.property;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.registry.DeferredToolProperty;
import net.stln.magitech.registry.DeferredToolPropertyRegister;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ToolPropertyInit {

    public static final DeferredToolPropertyRegister REGISTER = new DeferredToolPropertyRegister(Magitech.MOD_ID);

    private static <T extends IToolProperty<?>> @NotNull DeferredToolProperty<T> register(@NotNull String path, @NotNull T toolProperty) {
        return REGISTER.register(path, () -> toolProperty);
    }

    // ツールのプロパティ: 素材と特性によって決まる性能値のリスト (現在耐久値や現在熟練度などの使用によって変化しうるものは除外、初期値は含む)
    public static final DeferredToolProperty<TierToolProperty> TIER = register("tier", new TierToolProperty());
    public static final DeferredToolProperty<ProgressionToolProperty> PROGRESSION = register("progression", new ProgressionToolProperty());
    public static final DeferredToolProperty<DoubleToolProperty> MAX_PROGRESSION_COEFFICIENT = register("max_progression_coefficient", new DoubleToolProperty(new Color(0xFFFFFF)));
    public static final DeferredToolProperty<PointToolProperty> UPGRADE_POINT = register("upgrade_point", new PointToolProperty(ComponentInit.UPGRADE_POINT_COMPONENT, new Color(0xC0FF40)));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> DEFENSE = register("defense", new SimpleAttributeToolProperty(Attributes.ARMOR, ToolPropertyCategory.DEFENCE));
    public static final DeferredToolProperty<DurationToolProperty> DURATION = register("duration", new DurationToolProperty(ToolPropertyCategory.DURATION));

    // 近接武器
    public static final DeferredToolProperty<SimpleAttributeToolProperty> DAMAGE = register("damage", new SimpleAttributeToolProperty(Attributes.ATTACK_DAMAGE, ToolPropertyCategory.ATTACK));
    public static final DeferredToolProperty<ElementalAttributeToolProperty> ELEMENTAL_DAMAGE = register("elemental_damage", new ElementalAttributeToolProperty(AttributeInit.ELEMENTAL_DAMAGE, ToolPropertyCategory.ELEMENT));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> ATTACK_SPEED = register("attack_speed", new SimpleAttributeToolProperty(Attributes.ATTACK_SPEED, ToolPropertyCategory.HANDLING));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> MINING_SPEED = register("mining_speed", new SimpleAttributeToolProperty(AttributeInit.MINING_SPEED, ToolPropertyCategory.CONTINUITY));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> REACH = register("reach", new SimpleAttributeToolProperty(Attributes.ENTITY_INTERACTION_RANGE, ToolPropertyCategory.RANGE));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> SWEEP = register("sweep", new SimpleAttributeToolProperty(AttributeInit.SWEEP_RANGE, ToolPropertyCategory.UNIQUE));
    public static final DeferredToolProperty<MiningLevelToolProperty> MINING_LEVEL = register("mining_level", new MiningLevelToolProperty(new Color(0xFFFFFF)));

    // 魔法武器
    public static final DeferredToolProperty<SimpleAttributeToolProperty> POWER = register("power", new SimpleAttributeToolProperty(Attributes.ATTACK_DAMAGE, ToolPropertyCategory.ATTACK));
    public static final DeferredToolProperty<SpellElementalAttributeToolProperty> ELEMENTAL_POWER = register("elemental_power", new SpellElementalAttributeToolProperty(ToolPropertyCategory.ELEMENT));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> CHARGE_SPEED = register("charge_speed", new SimpleAttributeToolProperty(AttributeInit.CHARGE_SPEED, ToolPropertyCategory.HANDLING));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> COOLDOWN_SPEED = register("cooldown_speed", new SimpleAttributeToolProperty(AttributeInit.COOLDOWN_SPEED, ToolPropertyCategory.CONTINUITY));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> LAUNCH = register("launch", new SimpleAttributeToolProperty(AttributeInit.LAUNCH, ToolPropertyCategory.RANGE));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> MANA_EFFICIENCY = register("mana_efficiency", new SimpleAttributeToolProperty(AttributeInit.MANA_EFFICIENCY, ToolPropertyCategory.UNIQUE));

    public static void registerToolProperties(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Properties for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
