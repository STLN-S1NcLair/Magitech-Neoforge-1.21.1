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
import java.util.function.Function;

public class ToolPropertyInit {

    public static final DeferredToolPropertyRegister REGISTER = new DeferredToolPropertyRegister(Magitech.MOD_ID);

    private static float order = 0F;

    private static <T extends IToolProperty<?>> @NotNull DeferredToolProperty<T> register(@NotNull String path, @NotNull Function<Float, T> toolProperty) {
        return REGISTER.register(path, () -> toolProperty.apply(order++));
    }

    // ツールのプロパティ: 素材と特性によって決まる性能値のリスト (現在耐久値や現在熟練度などの使用によって変化しうるものは除外、初期値は含む)
    public static final DeferredToolProperty<TierToolProperty> TIER = register("tier", TierToolProperty::new);
    public static final DeferredToolProperty<ProgressionToolProperty> PROGRESSION = register("progression", ProgressionToolProperty::new);
    public static final DeferredToolProperty<RationalToolProperty> MAX_PROGRESSION_COEFFICIENT = register("max_progression_coefficient", f -> new RationalToolProperty(f, new Color(0xFFFFFF)));
    public static final DeferredToolProperty<PointToolProperty> UPGRADE_POINT = register("upgrade_point", f -> new PointToolProperty(f, ComponentInit.UPGRADE_POINT_COMPONENT, new Color(0xC0FF40)));

    // 近接武器
    public static final DeferredToolProperty<SimpleAttributeToolProperty> DAMAGE = register("damage", f -> new SimpleAttributeToolProperty(f, Attributes.ATTACK_DAMAGE, ToolPropertyCategory.ATTACK));
    public static final DeferredToolProperty<ElementalAttributeToolProperty> ELEMENTAL_DAMAGE = register("elemental_damage", f -> new ElementalAttributeToolProperty(f, AttributeInit.ELEMENTAL_DAMAGE, ToolPropertyCategory.ELEMENT));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> ATTACK_SPEED = register("attack_speed", f -> new SimpleAttributeToolProperty(f, Attributes.ATTACK_SPEED, ToolPropertyCategory.HANDLING));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> MINING_SPEED = register("mining_speed", f -> new SimpleAttributeToolProperty(f, AttributeInit.MINING_SPEED, ToolPropertyCategory.CONTINUITY));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> REACH = register("reach", f -> new SimpleAttributeToolProperty(f, Attributes.ENTITY_INTERACTION_RANGE, ToolPropertyCategory.RANGE));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> SWEEP = register("sweep", f -> new SimpleAttributeToolProperty(f, AttributeInit.SWEEP_RANGE, ToolPropertyCategory.UNIQUE));
    public static final DeferredToolProperty<MiningLevelToolProperty> MINING_LEVEL = register("mining_level", f -> new MiningLevelToolProperty(f, new Color(0xFFFFFF)));

    // 魔法武器
    public static final DeferredToolProperty<SimpleAttributeToolProperty> POWER = register("power", f -> new SimpleAttributeToolProperty(f, AttributeInit.SPELL_POWER, ToolPropertyCategory.ATTACK));
    public static final DeferredToolProperty<SpellElementalAttributeToolProperty> ELEMENTAL_POWER = register("elemental_power", f -> new SpellElementalAttributeToolProperty(f, ToolPropertyCategory.ELEMENT));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> CHARGE_SPEED = register("charge_speed", f -> new SimpleAttributeToolProperty(f, AttributeInit.CHARGE_SPEED, ToolPropertyCategory.HANDLING));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> COOLDOWN_SPEED = register("cooldown_speed", f -> new SimpleAttributeToolProperty(f, AttributeInit.COOLDOWN_SPEED, ToolPropertyCategory.CONTINUITY));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> LAUNCH = register("launch", f -> new SimpleAttributeToolProperty(f, AttributeInit.LAUNCH, ToolPropertyCategory.RANGE));
    public static final DeferredToolProperty<SimpleAttributeToolProperty> MANA_EFFICIENCY = register("mana_efficiency", f -> new SimpleAttributeToolProperty(f, AttributeInit.MANA_EFFICIENCY, ToolPropertyCategory.UNIQUE));

    // 一般プロパティ
    public static final DeferredToolProperty<SimpleAttributeToolProperty> DEFENSE = register("defense", f -> new SimpleAttributeToolProperty(f, Attributes.ARMOR, ToolPropertyCategory.DEFENCE));
    public static final DeferredToolProperty<DurationToolProperty> DURABILITY = register("durability", f -> new DurationToolProperty(f, ToolPropertyCategory.DURABILITY));

    public static void registerToolProperties(IEventBus bus) {
        Magitech.LOGGER.info("Registering Tool Properties for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
