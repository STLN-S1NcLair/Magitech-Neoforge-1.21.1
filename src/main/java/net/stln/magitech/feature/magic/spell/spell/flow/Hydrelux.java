package net.stln.magitech.feature.magic.spell.spell.flow;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.magicentity.BombSpellProjectileEntity;
import net.stln.magitech.content.entity.magicentity.hydrelux.HydreluxEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Hydrelux extends ShotSpell {

    public Hydrelux() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.BOMB, 200, 65)
                        .charge(15)
                        .property(SpellPropertyInit.DAMAGE, 9.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 2.0F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 3.5F)
                        .endSound(SoundInit.HYDRELUX_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                HydreluxEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity && summoned instanceof BombSpellProjectileEntity bomb) {
            Vec3 toEntity = livingEntity.position().subtract(bomb.position()).normalize();
            double distance = bomb.position().distanceTo(target.position());
            float explosionRadius = bomb.getExplosionRadius();
            double pushStrength = Mth.clamp((explosionRadius - distance) / explosionRadius, 0, 1) * 2;
            livingEntity.addDeltaMovement(toEntity.scale(pushStrength).add(0, 0.3, 0));
        }
    }
}
