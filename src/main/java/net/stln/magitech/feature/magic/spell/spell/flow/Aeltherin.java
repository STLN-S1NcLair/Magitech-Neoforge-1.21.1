package net.stln.magitech.feature.magic.spell.spell.flow;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.magicentity.aeltherin.AeltherinEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Aeltherin extends ShotSpell {

    public Aeltherin() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.SHOT, 10, 50)
                        .property(SpellPropertyInit.DAMAGE, 7.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 0.2F)
                        .endSound(SoundInit.AELTHERIN)
                        .endAnim("swing_wand"),
                AeltherinEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            Vec3 movement = summoned.getDeltaMovement();
            livingEntity.knockback(2, -movement.x, -movement.z);
        }
    }
}
