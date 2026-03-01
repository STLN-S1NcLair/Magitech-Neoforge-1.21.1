package net.stln.magitech.feature.magic.spell.property;

import net.stln.magitech.feature.magic.spell.SpellHelper;

public class SpellPropertyInit {

    // スペルのプロパティ
    public static final SpellPropertyKey<Float> DAMAGE = new SpellPropertyKey<>("damage", SpellHelper::createDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> CONTINUOUS_DAMAGE = new SpellPropertyKey<>("continuous_damage", SpellHelper::createContinuousDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> PROJECTILE_SPEED = new SpellPropertyKey<>("projectile_speed", SpellHelper::createDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> MAX_RANGE = new SpellPropertyKey<>("max_range", SpellHelper::createDamagePropertyTooltip);
    public static final SpellPropertyKey<Integer> DURATION_TIME = new SpellPropertyKey<>("duration_time", ((spell, livingEntity, stack, integerSpellPropertyKey) -> SpellHelper.createDamagePropertyTooltip(spell, livingEntity, stack, integerSpellPropertyKey, 0)));
    public static final SpellPropertyKey<Float> HEALING_AMOUNT = new SpellPropertyKey<>("healing_amount", SpellHelper::createDamagePropertyTooltip);
    public static final SpellPropertyKey<Float> EFFECT_STRENGTH = new SpellPropertyKey<>("effect_strength", SpellHelper::createDamagePropertyTooltip);
}
