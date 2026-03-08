package net.stln.magitech.feature.magic.spell.property;

import net.stln.magitech.feature.magic.spell.SpellHelper;

public class SpellPropertyInit {

    // スペルのプロパティ
    public static final SpellPropertyKey<Float> DAMAGE = new SpellPropertyKey<>("damage", SpellHelper::createDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> CONTINUOUS_DAMAGE = new SpellPropertyKey<>("continuous_damage", SpellHelper::createContinuousDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> PROJECTILE_SPEED = new SpellPropertyKey<>("projectile_speed", SpellHelper::createDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> MAX_RANGE = new SpellPropertyKey<>("max_range", SpellHelper::createRangePropertyTooltip);
    public static final SpellPropertyKey<Integer> DURATION_TIME = new SpellPropertyKey<>("duration_time", SpellHelper::createDurationPropertyTooltip);
    public static final SpellPropertyKey<Float> HEALING_AMOUNT = new SpellPropertyKey<>("healing_amount", SpellHelper::createDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> EFFECT_STRENGTH = new SpellPropertyKey<>("effect_strength", SpellHelper::createDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> BEAM_RADIUS = new SpellPropertyKey<>("beam_radius", ((spell, livingEntity, stack, floatSpellPropertyKey) -> SpellHelper.createBasicPropertyValueTooltip(spell, floatSpellPropertyKey)));
    public static final SpellPropertyKey<Float> EXPLOSION_RADIUS = new SpellPropertyKey<>("explosion_radius", ((spell, livingEntity, stack, floatSpellPropertyKey) -> SpellHelper.createBasicPropertyValueTooltip(spell, floatSpellPropertyKey)));
}
