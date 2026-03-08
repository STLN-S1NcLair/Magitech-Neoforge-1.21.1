package net.stln.magitech.feature.magic.spell.spell.ember;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.particle.particle_option.ManaZapParticleEffect;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.BeamParticles;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.FlameParticleEffect;
import org.joml.Vector3f;

public class Pyrolux extends BeamSpell {

    public Pyrolux() {
        super(new SpellConfig.Builder(Element.EMBER, SpellShape.BEAM, 100, 45)
                .charge(4)
                .property(SpellPropertyInit.DAMAGE, 7.0F)
                .property(SpellPropertyInit.MAX_RANGE, 64F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.5F)
                .endSound(SoundInit.PYROLUX)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        target.setRemainingFireTicks(Math.min(200, target.getRemainingFireTicks() + 100));
    }

//    @Override
//    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
//        EffectHelper.lineEffect(level, new FlameParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 8), 0.9F), start, end, 2, false);
//        level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 0.5F, 0.3F), new Vector3f(0.7F, 0.2F, 0.0F), end.toVector3f(), 0.8F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
//        for (int i = 0; i < 20; i++) {
//            level.addParticle(new FlameParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 8), 0.9F),
//                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
//        }
//    }


    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.linedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.3F, 0.2F);
        LineVFX.lined(level, start, end, element, ElementParticles::smokeParticle, new Section(0F, 1F), 3, 0.3F, 0.1F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burst(level, end, element, SquareParticles::squareGravityParticle, 30, 0.3F);
        PointVFX.burst(level, end, element, ElementParticles::smokeParticle, 10, 0.1F);
    }
}
