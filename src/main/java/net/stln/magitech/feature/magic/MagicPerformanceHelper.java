package net.stln.magitech.feature.magic;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyKey;
import net.stln.magitech.helper.DataMapHelper;
import org.jetbrains.annotations.Nullable;

public class MagicPerformanceHelper {

    // 除算で求める速度系(チャージ速度、クールダウン速度)、マナ効率などに使用
    public static float getEffectiveSpellPropertyWithDivide(LivingEntity caster, @Nullable ItemStack wand, long cost, float value, Holder<Attribute> speedAttribute) {
        double coefficient = caster.getAttributeValue(speedAttribute);
        return (float) (value / coefficient);
    }

    public static float getEffectiveSpellProperty(LivingEntity caster, @Nullable ItemStack wand, long cost, float value, Holder<Attribute> coefficientAttribute) {
        double coefficient = caster.getAttributeValue(coefficientAttribute);
        return (float) (value * coefficient);
    }

    public static float getEffectiveSpellProperty(LivingEntity caster, @Nullable ItemStack wand, ISpell spell, SpellPropertyKey<Float> key, Holder<Attribute> coefficientAttribute) {
        SpellConfig config = spell.getConfig();
        return getEffectiveSpellProperty(caster, wand, config.cost(), config.properties().get(key), coefficientAttribute);
    }

    // 敵の属性倍率を考慮した実効ダメージ値
    public static float getEffectiveMagicDamage(LivingEntity caster, @Nullable ItemStack wand, long cost, float damage, Element element, LivingEntity target) {
        float powered = getOutgoingMagicDamage(caster, wand, cost, damage, element);
        powered *= DataMapHelper.getElementMultiplier(target, element);
        return powered;
    }

    public static float getEffectiveMagicDamage(LivingEntity caster, @Nullable ItemStack wand, ISpell spell, SpellPropertyKey<Float> key, LivingEntity target) {
        SpellConfig config = spell.getConfig();
        return getEffectiveMagicDamage(caster, wand, config.cost(), config.properties().get(key), config.element(), target);
    }

    // 魔法威力を考慮した出力ダメージ値(EFFECT_STRENGTHなどでも使用する)
    public static float getOutgoingMagicDamage(LivingEntity caster, @Nullable ItemStack wand, long cost, float damage, Element element) {
        double power = caster.getAttributeValue(AttributeInit.SPELL_POWER);
        double elementPower = 0;
        elementPower += element.getPowerAttribute().map((caster::getAttributeValue)).orElse(1.0);
        return (float) (damage * power + damage * (elementPower - 1));
    }

    public static <T extends Number> float getOutgoingMagicDamage(LivingEntity caster, @Nullable ItemStack wand, SpellPropertyKey<T> key, ISpell spell) {
        SpellConfig config = spell.getConfig();
        return getOutgoingMagicDamage(caster, wand, config.cost(), config.properties().get(key).floatValue(), config.element());
    }

    public static void applyMagicDamage(LivingEntity caster, @Nullable ItemStack wand, long cost, float damage, Element element, LivingEntity target) {
        float effectiveDamage = getEffectiveMagicDamage(caster, wand, cost, damage, element, target);
        ResourceKey<DamageType> damageType = element.getDamageType();

        DamageSource elementalDamageSource = caster.damageSources().source(damageType, caster);
        if (target.isAttackable()) {

            if (target instanceof LivingEntity livingTarget && livingTarget.invulnerableTime < 10) {
                if (wand != null && wand.getItem() instanceof SpellCasterItem spellCasterItem) {
                    if (caster instanceof Player player) {
                        spellCasterItem.callTraitSpellHitEntity(caster.level(), player, target, wand);
                    }
                }
                if (!target.isInvulnerableTo(elementalDamageSource)) {
                    float targetHealth = livingTarget.getHealth();
                    livingTarget.setLastHurtByMob(caster);
                    if (caster instanceof Player player) {
                        player.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingTarget.getHealth()) * 10));
                    }
                }
                target.hurt(elementalDamageSource, effectiveDamage);
            }
            caster.setLastHurtMob(target);
        }
    }
    public static void applyEffectiveMagicDamage(LivingEntity caster, @Nullable ItemStack wand, ISpell spell, LivingEntity target) {
        SpellConfig config = spell.getConfig();
        applyMagicDamage(caster, wand, config.cost(), config.properties().get(SpellPropertyInit.DAMAGE), config.element(), target);
    }

}
