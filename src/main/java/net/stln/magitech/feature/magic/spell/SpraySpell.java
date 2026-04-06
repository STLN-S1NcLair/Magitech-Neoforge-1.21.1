package net.stln.magitech.feature.magic.spell;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class SpraySpell extends DamageSpell {

    public SpraySpell(SpellConfig.Builder builder) {
        super(builder);
    }

    @Override
    public void tickSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, int ticks, boolean charging) {
        if (!charging) {
            Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
            Vec3 bodyPos = CombatHelper.getBodyPos(caster);
            Vec3 offset = bodyPos.add(forward.scale(1));
            Set<Entity> attackList = SpellHelper.getTargets(level, caster);
            for (Entity target : attackList) {
                Vec3 targetBodyPos = CombatHelper.getBodyPos(target);
                if (!SpellHelper.canSee(level, caster, targetBodyPos, offset)) {
                    continue;
                }
                hitTarget(level, caster, wand, SpellPropertyInit.CONTINUOUS_DAMAGE, target);
            }
            additionalSprayProcess(level, caster, wand, attackList);
        }
    }

    // 追加の処理
    protected void additionalSprayProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, Set<Entity> targets) {

    }
}
