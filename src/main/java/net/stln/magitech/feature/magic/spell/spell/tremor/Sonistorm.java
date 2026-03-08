package net.stln.magitech.feature.magic.spell.spell.tremor;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.SpraySpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EntityHelper;
import net.stln.magitech.effect.visual.particle.particle_option.WaveParticleEffect;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class Sonistorm extends SpraySpell {

    public Sonistorm() {
        super(new SpellConfig.Builder(Element.TREMOR, SpellShape.SPRAY, 30, 25)
                .continuous(2.5F)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 5.0F)
                .tickSound(SoundInit.SONISTORM, 5)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3));
            livingTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
        }
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(forward.scale(1));
        Vec3 center = caster.getEyePosition().add(forward);
        Vec3 center2 = center.add(forward.scale(2));
        Set<Entity> attackList = new HashSet<>();
        attackList.addAll(EntityHelper.getEntitiesInBox(level, caster, center, new Vec3(3.0, 3.0, 3.0)));
        attackList.addAll(EntityHelper.getEntitiesInBox(level, caster, center2, new Vec3(4.0, 4.0, 4.0)));
        for (int i = 0; i < 5; i++) {
            level.addParticle(new WaveParticleEffect(new Vector3f(1), new Vector3f(1),
                            5F, 1, 0, level.random.nextInt(5, 10), 0.9F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    forward.x * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2, forward.y * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2, forward.z * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2);
        }
    }
}
