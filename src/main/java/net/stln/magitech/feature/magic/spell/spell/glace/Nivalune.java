package net.stln.magitech.feature.magic.spell.spell.glace;

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
import net.stln.magitech.effect.visual.particle.particle_option.FrostParticleEffect;
import org.joml.Vector3f;

public class Nivalune extends SpraySpell {

    public Nivalune() {
        super(new SpellConfig.Builder(Element.GLACE, SpellShape.SPRAY, 40, 15)
                .continuous(1.5F)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 4.0F)
                .tickSound(SoundInit.NIVALUNE, 5)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        target.setTicksFrozen(Math.min(target.getTicksFrozen() + 100, 300));
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(forward.scale(1));
        for (int i = 0; i < 5; i++) {
            level.addParticle(new FrostParticleEffect(new Vector3f(1), new Vector3f(1),
                            3F, 1, (float) (caster.getRandom().nextFloat() - 0.5), level.random.nextInt(20, 30), 0.9F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    forward.x * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2, forward.y * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2, forward.z * 0.5 + (caster.getRandom().nextFloat() - 0.5) / 2);
        }
    }
}
