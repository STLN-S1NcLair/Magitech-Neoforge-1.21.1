package net.stln.magitech.feature.magic.spell.spell.tremor;

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
import net.stln.magitech.effect.visual.particle.particle_option.WaveParticleEffect;
import org.joml.Vector3f;

public class Oscilbeam extends BeamSpell {

    public Oscilbeam() {
        super(new SpellConfig.Builder(Element.TREMOR, SpellShape.BEAM, 100, 55)
                .charge(15)
                .property(SpellPropertyInit.DAMAGE, 10.0F)
                .property(SpellPropertyInit.MAX_RANGE, 32F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.7F)
                .endSound(SoundInit.SONICBOOM)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        EffectHelper.lineEffect(level, new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 10), 0.9F), start, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.0F, 0.7F, 0.7F), new Vector3f(0.0F, 1.0F, 1.0F), end.toVector3f(), 1.8F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 10), 0.9F),
                    end.x + (caster.getRandom().nextFloat() - 0.5) / 3, end.y + (caster.getRandom().nextFloat() - 0.5) / 3, end.z + (caster.getRandom().nextFloat() - 0.5) / 3, 0, 0, 0);
        }
    }
}
