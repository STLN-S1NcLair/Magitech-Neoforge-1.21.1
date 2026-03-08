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
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.MembraneParticleEffect;
import org.joml.Vector3f;

public class Phantastra extends BeamSpell {

    public Phantastra() {
        super(new SpellConfig.Builder(Element.PHANTOM, SpellShape.BEAM, 70, 40)
                .charge(6)
                .property(SpellPropertyInit.DAMAGE, 5.0F)
                .property(SpellPropertyInit.MAX_RANGE, 128F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.7F)
                .endSound(SoundInit.PHANTASTRA)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffectInit.SEIZE, 50, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
        }
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        EffectHelper.lineEffect(level, new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2.0F, 1, 0, level.random.nextInt(10, 40), 0.85F), start, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 1.0F, 0.9F), new Vector3f(0.6F, 1.0F, 0.3F), end.toVector3f(), 0.4F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(10, 40), 0.85F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
    }
}
