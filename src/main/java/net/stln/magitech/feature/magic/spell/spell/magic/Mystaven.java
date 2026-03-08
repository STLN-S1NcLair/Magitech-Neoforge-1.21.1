package net.stln.magitech.feature.magic.spell.spell.magic;

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
import net.stln.magitech.effect.visual.particle.particle_option.RuneParticleEffect;
import org.joml.Vector3f;

public class Mystaven extends BeamSpell {

    public Mystaven() {
        super(new SpellConfig.Builder(Element.MAGIC, SpellShape.BEAM, 60, 55)
                .charge(2)
                .property(SpellPropertyInit.DAMAGE, 7.0F)
                .property(SpellPropertyInit.MAX_RANGE, 64F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.2F)
                .endSound(SoundInit.MYSTAVEN)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        EffectHelper.lineEffect(level, new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 20), 0.9F), start, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 0.0F, 0.7F), new Vector3f(1.0F, 0.0F, 0.3F), end.toVector3f(), 0.5F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 20), 0.9F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
    }
}
