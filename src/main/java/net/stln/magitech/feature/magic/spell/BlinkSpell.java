package net.stln.magitech.feature.magic.spell;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BlinkSpell extends DamageSpell {

    public BlinkSpell(SpellConfig.Builder builder) {
        super(builder);
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        Vec3 start = caster.position();
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        float maxRange = MagicPerformanceHelper.getEffectiveMaxRange(caster, wand, this);
        Vec3 hitPos = CombatHelper.raycast(caster, maxRange).subtract(forward.scale(caster.getBbWidth() / 2 + 0.1));

        // ダメージを与える場合のみ演算
        List<Entity> nearbyEntities = new ArrayList<>();

        if (this.getConfig().properties().contains(SpellPropertyInit.DAMAGE)) {
            for (int i = 0; i < hitPos.distanceTo(caster.position()); i++) {
                nearbyEntities.addAll(CombatHelper.getEntitiesInBox(level, caster, caster.position().lerp(hitPos, i / hitPos.distanceTo(caster.position())), new Vec3(3, 3, 3)));
            }
            for (Entity entity : nearbyEntities) {
                hitTarget(level, caster, wand, entity);
            }
            for (Entity entity : CombatHelper.getEntitiesInBox(level, caster, hitPos, new Vec3(4, 4, 4))) {
                nearbyEntities.add(entity);
                hitTarget(level, caster, wand, entity);
                Vec3 hitDirection = entity.position().subtract(hitPos).normalize();
                entity.addDeltaMovement(hitDirection.scale(0.3));
            }
        }
        if (!level.isClientSide) {
            caster.fallDistance = 0;
            TickScheduler.schedule(1, () -> {
                caster.fallDistance = 0;
                caster.teleportTo(hitPos.x, hitPos.y, hitPos.z);
                SoundHelper.broadcastSound(level, caster, hitPos, this.getConfig().endSound());
            }, level.isClientSide);
        } else {
            addBlinkVFX(level, caster, start, hitPos);
        }
        additionalBlinkProcess(level, caster, wand, nearbyEntities, start, hitPos);
    }

    // 追加の処理
    protected void additionalBlinkProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, List<Entity> targets, Vec3 start, Vec3 hitPos) {

    }

    // エフェクト
    protected void addBlinkVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {

    }
}
