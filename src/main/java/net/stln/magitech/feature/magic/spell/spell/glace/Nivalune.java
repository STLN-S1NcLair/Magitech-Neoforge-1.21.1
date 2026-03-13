package net.stln.magitech.feature.magic.spell.spell.glace;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
        if (!charging) {
            Element element = this.getConfig().element();
            Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
            Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
            Vec3 offset = bodyPos.add(forward.scale(1));
            PointVFX.spray(level, offset, element,
                    (lvl, pos, elm) -> PresetHelper.bigger(SquareParticles.squareGravityParticle(lvl, pos, elm, 0.1F)),
                    forward, 20, 0.5F, 0.4F);
            PointVFX.spray(level, offset, element,
                    (lvl, pos, elm) -> PresetHelper.bigger(ElementParticles.snowParticle(lvl, pos, elm)),
                    forward, 10, 0.5F, 0.4F);
        }
    }
}
