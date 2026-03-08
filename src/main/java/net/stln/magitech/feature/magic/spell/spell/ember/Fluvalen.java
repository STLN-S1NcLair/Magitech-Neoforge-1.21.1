package net.stln.magitech.feature.magic.spell.spell.ember;

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
import net.stln.magitech.effect.visual.particle.particle_option.FlameParticleEffect;
import org.joml.Vector3f;

public class Fluvalen extends SpraySpell {

    public Fluvalen() {
        super(new SpellConfig.Builder(Element.EMBER, SpellShape.SPRAY, 60, 20)
                .continuous(2)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 3.0F)
                .tickSound(SoundInit.FLAME, 5)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        target.setRemainingFireTicks(Math.min(200, target.getRemainingFireTicks() + 60));
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        if (!charging) {
            Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
            Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
            Vec3 offset = bodyPos.add(forward.scale(1));
            for (int i = 0; i < 5; i++) {
                level.addParticle(new FlameParticleEffect(new Vector3f(1), new Vector3f(1),
                                5F, 1, 0.3F, level.random.nextInt(5, 8), 0.9F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                        forward.x * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 4, forward.y * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 4, forward.z * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 4);
            }
        }
    }
}
