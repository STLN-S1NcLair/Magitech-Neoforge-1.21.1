package net.stln.magitech.feature.magic.spell;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.helper.CombatHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BeamSpell extends DamageSpell {

    public BeamSpell(SpellConfig.Builder builder) {
        super(builder);
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        float radius = MagicPerformanceHelper.getEffectiveBeamRadius(caster, wand, this);
        float maxRange = MagicPerformanceHelper.getEffectiveMaxRange(caster, wand, this);
        Vec3 hitPos = CombatHelper.raycastBeam(caster, maxRange, radius);
        Entity target = CombatHelper.raycastBeamEntity(caster, maxRange, radius);
        Vec3 start = CombatHelper.getBodyPos(caster).add(forward.scale(0.5));

        if (target != null) {
            hitTarget(level, caster, wand, target);
        }
        if (!level.isClientSide) {
            List<Entity> entities = CombatHelper.getEntitiesInBox(level, caster, hitPos, new Vec3(1, 1, 1));
            for (Entity entity : entities) {
                if (entity instanceof ItemEntity item) {
                    SpellHelper.applyEffectToItem(level, this, item);
                }
            }
            SoundHelper.broadcastSound(level, caster, hitPos, this.getConfig().endSound());
        } else {
            addBeamVFX(level, caster, start, hitPos);
        }
        additionalBeamProcess(level, caster, wand, target, hitPos);
    }

    // 追加の処理
    protected void additionalBeamProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable Entity target, Vec3 hitPos) {

    }

    // ビームのエフェクト
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {

    }
}
