package net.stln.magitech.feature.magic.spell.spell.phantom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.SpraySpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EntityHelper;
import net.stln.magitech.effect.visual.particle.particle_option.MembraneParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.UnstableSquareParticleEffect;
import org.joml.Vector3f;

public class Veilmist extends SpraySpell {

    public Veilmist() {
        super(new SpellConfig.Builder(Element.PHANTOM, SpellShape.SPRAY, 60, 25)
                .continuous(2.5F)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 5.0F)
                .tickSound(SoundInit.VEILMIST, 5)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
            livingTarget.addEffect(new MobEffectInstance(MobEffectInit.SEIZE, 1, 0));
        }
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        Vec3 bodyPos = EntityHelper.getBodyPos(caster);
        Vec3 offset = bodyPos.add(forward.scale(1));
        for (int i = 0; i < 2; i++) {
            level.addParticle(new MembraneParticleEffect(new Vector3f(1), new Vector3f(1),
                            5F, 1, (float) ((caster.getRandom().nextFloat() - 0.5) / 8), level.random.nextInt(10, 40), 0.85F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    forward.x * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2, forward.y * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2, forward.z * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2);
            level.addParticle(new UnstableSquareParticleEffect(new Vector3f(1, 1, 0.7F), new Vector3f(1, 1, 0.5F),
                            2F, 1, (float) ((caster.getRandom().nextFloat() - 0.5) / 8), 15, 0.0F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    forward.x * 0.25 + (caster.getRandom().nextFloat() - 0.5) / 6, forward.y * 0.25 + (caster.getRandom().nextFloat() - 0.5) / 6, forward.z * 0.25 + (caster.getRandom().nextFloat() - 0.5) / 6);
        }
    }
}
