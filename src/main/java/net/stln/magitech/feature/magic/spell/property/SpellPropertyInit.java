package net.stln.magitech.feature.magic.spell.property;

import net.stln.magitech.feature.magic.spell.SpellHelper;

public class SpellPropertyInit {

    // スペルのプロパティ
    public static final SpellProperty<Float> DAMAGE = new SpellProperty<>("damage", SpellHelper::createDamagePropertyTooltip);
    public static final SpellProperty<Float> CONTINUOUS_DAMAGE = new SpellProperty<>("continuous_damage", SpellHelper::createContinuousDamagePropertyTooltip);
    public static final SpellProperty<Float> PROJECTILE_SPEED = new SpellProperty<>("projectile_speed", SpellHelper::createDamagePropertyTooltip);
    public static final SpellProperty<Float> MAX_RANGE = new SpellProperty<>("max_range", SpellHelper::createRangePropertyTooltip);
    public static final SpellProperty<Integer> DURATION_TIME = new SpellProperty<>("duration_time", SpellHelper::createDurationPropertyTooltip);
    public static final SpellProperty<Float> HEALING_AMOUNT = new SpellProperty<>("healing_amount", SpellHelper::createDamagePropertyTooltip);
    public static final SpellProperty<Float> EFFECT_STRENGTH = new SpellProperty<>("effect_strength", SpellHelper::createDamagePropertyTooltip);
    public static final SpellProperty<Float> BEAM_RADIUS = new SpellProperty<>("beam_radius", ((spell, livingEntity, stack, floatSpellPropertyKey) -> SpellHelper.createBasicPropertyValueTooltip(spell, floatSpellPropertyKey)));
    public static final SpellProperty<Float> EXPLOSION_RADIUS = new SpellProperty<>("explosion_radius", ((spell, livingEntity, stack, floatSpellPropertyKey) -> SpellHelper.createBasicPropertyValueTooltip(spell, floatSpellPropertyKey)));
}
