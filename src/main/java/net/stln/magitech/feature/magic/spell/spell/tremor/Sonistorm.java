package net.stln.magitech.feature.magic.spell.spell.tremor;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.SpraySpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.effect.visual.particle.particle_option.WaveParticleEffect;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;

import java.util.HashSet;
import java.util.Set;

public class Sonistorm extends SpraySpell {

    public Sonistorm() {
        super(new SpellConfig.Builder(Element.TREMOR, SpellShape.SPRAY, 30, 25)
                .continuous(2.5F)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 5.0F)
                .tickSound(SoundInit.SONISTORM, 5)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3));
            livingTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
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
            if (level.getGameTime() % 5 == 0) {
                RandomSource random = level.getRandom();
                PointVFX.spray(level, offset, element,
                        (lvl, pos, elm) -> PresetHelper.modify(RingParticles.ringReversedParticle(lvl, pos, forward, elm),
                                (builder -> builder.modifyScaleData(data -> data.multiplyValue(3.0F)))),
                        forward, 1, 0.5F, 0.0F);
            }
        }
    }
}
