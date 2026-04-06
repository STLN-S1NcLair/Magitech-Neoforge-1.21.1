package net.stln.magitech.feature.magic.spell;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.property.SpellProperty;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public abstract class DamageSpell extends Spell {

    public DamageSpell(SpellConfig.Builder builder) {
        super(builder);
    }

    protected void hitTarget(Level level, LivingEntity caster, @Nullable ItemStack wand, Entity target, float damage, float damageMultiplier) {
        MagicPerformanceHelper.applyMagicDamage(caster, wand, this, damage * damageMultiplier, target);
        if (!level.isClientSide) {
            if (target instanceof ItemEntity item) {
                SpellHelper.applyEffectToItem(level, this, item);
            }
            applyEffectToTarget(level, caster, target);
        }
    }

    protected void hitTarget(Level level, LivingEntity caster, @Nullable ItemStack wand, float damage, Entity target) {
        hitTarget(level, caster, wand, target, damage, 1.0F);
    }

    protected void hitTarget(Level level, LivingEntity caster, @Nullable ItemStack wand, SpellProperty<? extends Number> key, Entity target) {
        hitTarget(level, caster, wand, target, this.getConfig().properties().get(key).floatValue(), 1.0F);
    }

    protected void hitTarget(Level level, LivingEntity caster, @Nullable ItemStack wand, Entity target) {
        hitTarget(level, caster, wand, target, this.getConfig().properties().get(SpellPropertyInit.DAMAGE), 1.0F);
    }

    // ヒット対象に与える効果
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {

    }

}
