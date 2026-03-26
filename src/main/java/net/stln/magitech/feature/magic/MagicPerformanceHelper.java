package net.stln.magitech.feature.magic;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.property.SpellProperty;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.DataMapHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MagicPerformanceHelper {

    // Warning: 引数costはデフォルトマナコストを入れる
    // 除算で求める速度系(チャージ速度、クールダウン速度)、マナ効率などに使用
    public static float getEffectiveSpellPropertyWithDivide(LivingEntity caster, @Nullable ItemStack wand, float cost, float value, Holder<Attribute> speedAttribute) {
        double coefficient = caster.getAttributeValue(speedAttribute);
        return (float) (value / coefficient);
    }

    public static float getEffectiveSpellProperty(LivingEntity caster, @Nullable ItemStack wand, float cost, float value, Holder<Attribute> coefficientAttribute) {
        double coefficient = caster.getAttributeValue(coefficientAttribute);
        return (float) (value * coefficient);
    }

    public static float getEffectiveSpellProperty(LivingEntity caster, @Nullable ItemStack wand, ISpell spell, SpellProperty<Float> key, Holder<Attribute> coefficientAttribute) {
        SpellConfig config = spell.getConfig();
        return getEffectiveSpellProperty(caster, wand, config.cost(), config.properties().get(key), coefficientAttribute);
    }

    // 敵の属性倍率を考慮した実効ダメージ値
    public static float getEffectiveMagicDamage(LivingEntity caster, @Nullable ItemStack wand, float cost, float damage, Element element, Entity target) {
        float powered = getOutgoingMagicDamage(caster, wand, cost, damage, element);
        powered *= DataMapHelper.getElementMultiplier(target, element);
        return powered;
    }

    public static float getEffectiveMagicDamage(LivingEntity caster, @Nullable ItemStack wand, ISpell spell, SpellProperty<Float> key, LivingEntity target) {
        SpellConfig config = spell.getConfig();
        return getEffectiveMagicDamage(caster, wand, config.cost(), config.properties().get(key), config.element(), target);
    }

    public static float getEffectiveMagicDamage(LivingEntity caster, @Nullable ItemStack wand, ISpell spell, LivingEntity target) {
        SpellConfig config = spell.getConfig();
        return getEffectiveMagicDamage(caster, wand, config.cost(), config.properties().get(SpellPropertyInit.DAMAGE), config.element(), target);
    }

    // 魔法威力を考慮した出力ダメージ値(EFFECT_STRENGTHなどでも使用する)
    public static float getOutgoingMagicDamage(LivingEntity caster, @Nullable ItemStack wand, float cost, float damage, Element element) {
        double power = caster.getAttributeValue(AttributeInit.SPELL_POWER);
        double elementPower = 0;
        elementPower += element.getPowerAttribute().map((caster::getAttributeValue)).orElse(1.0);
        return (float) (damage * power + damage * (elementPower - 1));
    }

    public static <T extends Number> float getOutgoingMagicDamage(LivingEntity caster, @Nullable ItemStack wand, SpellProperty<T> key, ISpell spell) {
        SpellConfig config = spell.getConfig();
        return getOutgoingMagicDamage(caster, wand, config.cost(), config.properties().get(key).floatValue(), config.element());
    }

    public static void applyEffectiveMagicDamage(LivingEntity caster, @Nullable ItemStack wand, ISpell spell, LivingEntity target) {
        SpellConfig config = spell.getConfig();
        applyMagicDamage(caster, wand, config.cost(), config.properties().get(SpellPropertyInit.DAMAGE), config.element(), target);
    }

    public static void applyMagicDamage(LivingEntity caster, @Nullable ItemStack wand, ISpell spell, float damage, Entity target) {
        SpellConfig config = spell.getConfig();
        applyMagicDamage(caster, wand, config.cost(), damage, config.element(), target);
    }

    public static void applyMagicDamage(LivingEntity caster, @Nullable ItemStack wand, float cost, float damage, Element element, Entity target) {
        float effectiveDamage = getEffectiveMagicDamage(caster, wand, cost, damage, element, target);
        ResourceKey<DamageType> damageType = element.getDamageType();
        DamageSource elementalDamageSource = caster.damageSources().source(damageType, caster);
        applyRawMagicDamage(caster, wand, target, elementalDamageSource, effectiveDamage);
    }

    public static void applyRawMagicDamage(@Nullable LivingEntity caster, @Nullable ItemStack wand, Entity target, DamageSource source, float effectiveDamage) {

        if (target != null && target.isAttackable()) {

            if (target.invulnerableTime < 10) {
                if (wand != null && wand.getItem() instanceof SpellCasterItem spellCasterItem) {
                    if (caster instanceof Player player) {
                        spellCasterItem.callTraitDamageEntity(caster.level(), player, target, wand);
                    }
                }
                if (!target.isInvulnerableTo(source) && target instanceof LivingEntity living) {
                    float targetHealth = living.getHealth();
                    living.setLastHurtByMob(caster);
                    if (caster instanceof Player player) {
                        player.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - living.getHealth()) * 10));
                    }
                }
                target.hurt(source, effectiveDamage);
            }
            if (caster != null) {
                caster.setLastHurtMob(target);
            }
        }
    }

    // 実際のPropertyヘルパー

    public static float getEffectiveCost(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        float cost = config.cost();
        return MagicPerformanceHelper.getEffectiveSpellPropertyWithDivide(caster, wand, cost, cost, AttributeInit.MANA_EFFICIENCY);
    }

    public static float getEffectiveContinuousCost(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.continuous()) return 0;
        float cost = config.cost();
        Optional<Float> continuousCost = config.costPerTick();
        return MagicPerformanceHelper.getEffectiveSpellPropertyWithDivide(caster, wand, cost, continuousCost.get(), AttributeInit.MANA_EFFICIENCY);
    }

    public static float getEffectiveChargeTime(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.hasCharge()) return 0;
        float cost = config.cost();
        Optional<Integer> chargeTime = config.chargeTime();
        return MagicPerformanceHelper.getEffectiveSpellPropertyWithDivide(caster, wand, cost, chargeTime.get(), AttributeInit.CHARGE_SPEED);
    }

    public static int getEffectiveCooldown(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        float cost = config.cost();
        int cooldownTime = config.cooldown();
        return (int) MagicPerformanceHelper.getEffectiveSpellPropertyWithDivide(caster, wand, cost, cooldownTime, AttributeInit.COOLDOWN_SPEED);
    }

    public static float getEffectiveProjectileSpeed(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.properties().contains(SpellPropertyInit.PROJECTILE_SPEED)) return 0;
        float cost = config.cost();
        Optional<Float> projectileSpeed = config.properties().getOptional(SpellPropertyInit.PROJECTILE_SPEED);
        return MagicPerformanceHelper.getEffectiveSpellProperty(caster, wand, cost, projectileSpeed.get(), AttributeInit.LAUNCH);
    }

    public static float getEffectiveMaxRange(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.properties().contains(SpellPropertyInit.MAX_RANGE)) return 0;
        return MagicPerformanceHelper.getOutgoingMagicDamage(caster, wand, SpellPropertyInit.MAX_RANGE, spell);
    }

    public static float getEffectiveEffectStrength(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.properties().contains(SpellPropertyInit.EFFECT_STRENGTH)) return 0;
        return MagicPerformanceHelper.getOutgoingMagicDamage(caster, wand, SpellPropertyInit.EFFECT_STRENGTH, spell);
    }

    public static int getEffectiveDurationTime(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.properties().contains(SpellPropertyInit.DURATION_TIME)) return 0;
        return (int) MagicPerformanceHelper.getOutgoingMagicDamage(caster, wand, SpellPropertyInit.DURATION_TIME, spell);
    }

    public static float getEffectiveHealAmount(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.properties().contains(SpellPropertyInit.HEALING_AMOUNT)) return 0;
        return MagicPerformanceHelper.getOutgoingMagicDamage(caster, wand, SpellPropertyInit.HEALING_AMOUNT, spell);
    }

    public static float getEffectiveBeamRadius(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.properties().contains(SpellPropertyInit.BEAM_RADIUS)) return 0;
        return config.properties().get(SpellPropertyInit.BEAM_RADIUS);
    }

    public static float getEffectiveExplosionRadius(LivingEntity caster, @Nullable ItemStack wand, ISpell spell) {
        SpellConfig config = spell.getConfig();
        if (!config.properties().contains(SpellPropertyInit.EXPLOSION_RADIUS)) return 0;
        return config.properties().get(SpellPropertyInit.EXPLOSION_RADIUS);
    }

}
