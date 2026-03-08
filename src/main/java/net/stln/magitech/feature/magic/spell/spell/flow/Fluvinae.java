package net.stln.magitech.feature.magic.spell.spell.flow;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.BlowParticleEffect;
import org.joml.Vector3f;

public class Fluvinae extends BeamSpell {

    public Fluvinae() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.BEAM, 80, 40)
                .charge(8)
                .property(SpellPropertyInit.DAMAGE, 7.0F)
                .property(SpellPropertyInit.MAX_RANGE, 256F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.3F)
                .endSound(SoundInit.FLUVINAE)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            Vec3 distance = caster.position().subtract(target.position()).normalize();
            livingEntity.knockback(1.5, distance.x, distance.z);
        }
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        EffectHelper.lineEffect(level, new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(10, 30), 0.87F), start, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), end.toVector3f(), 0.3F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(10, 30), 0.87F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
    }
}
