package net.stln.magitech.feature.magic.spell.spell.glace;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
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
import net.stln.magitech.effect.visual.particle.particle_option.FrostParticleEffect;
import org.joml.Vector3f;

public class Cryoluxa extends BeamSpell {

    public Cryoluxa() {
        super(new SpellConfig.Builder(Element.GLACE, SpellShape.BEAM, 120, 50)
                .charge(4)
                .property(SpellPropertyInit.DAMAGE, 8.0F)
                .property(SpellPropertyInit.MAX_RANGE, 64F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.3F)
                .endSound(SoundInit.FROST_BREAK)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        target.setTicksFrozen(Math.min(target.getTicksFrozen() + 160, 300));
    }

//    @Override
//    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
//        EffectHelper.lineEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(50, 60), 0.99F), start, end, 2, false);
//        level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), end.toVector3f(), 0.7F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
//        for (int i = 0; i < 20; i++) {
//            level.addParticle(new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(50, 60), 0.99F),
//                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
//        }
//    }


    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.linedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.2F, 0.1F);
        LineVFX.lined(level, start, end, element, ElementParticles::snowParticle, new Section(0F, 1F), 5, 0.1F, 0.1F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burst(level, end, element, ((lev, pos, elm) -> SquareParticles.squareGravityParticle(level, pos, elm, 0.2F)), 30, 0.3F);
        PointVFX.burst(level, end, element, ElementParticles::snowParticle, 20, 0.3F);
    }
}
