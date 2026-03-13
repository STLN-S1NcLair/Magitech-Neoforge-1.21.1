package net.stln.magitech.feature.magic.spell.spell.phantom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.SpraySpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
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
        if (!charging) {
            Element element = this.getConfig().element();
            Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
            Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
            Vec3 offset = bodyPos.add(forward.scale(1));
            PointVFX.spray(level, offset, element,
                    (lvl, pos, elm) -> PresetHelper.bigger(SquareParticles.squareParticle(lvl, pos, elm)),
                    forward, 20, 0.5F, 0.4F);
            PointVFX.spray(level, offset, element,
                    (lvl, pos, elm) -> PresetHelper.bigger(ElementParticles.glintParticle(lvl, pos, elm)),
                    forward, 10, 0.5F, 0.4F);
        }
    }
}
