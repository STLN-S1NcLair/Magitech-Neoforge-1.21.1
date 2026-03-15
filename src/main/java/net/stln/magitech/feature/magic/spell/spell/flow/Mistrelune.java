package net.stln.magitech.feature.magic.spell.spell.flow;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.preset.BehaviorPreset;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.SpraySpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.util.function.Consumer;

public class Mistrelune extends SpraySpell {

    public Mistrelune() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.SPRAY, 80, 20)
                .continuous(2)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 4.0F)
                .tickSound(SoundInit.BLOW, 5)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(forward.scale(2));
        target.addDeltaMovement(offset.subtract(target.position()).add(0, 1, 0).scale(0.020));
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

            Consumer<LodestoneWorldParticle> behavior = BehaviorPreset.toDestination(offset.add(forward), 1.0F, 0.5F, 0.7F);
            PointVFX.spray(level, offset, element,
                    (lvl, pos, elm) -> PresetHelper.modify(ElementParticles.leafParticle(lvl, pos, elm),
                            (builder -> builder.modifyScaleData(data -> data.multiplyValue(2.0F)).setFriction(0.95F).addTickActor(behavior))),
                    forward, 10, 5.0F, 5.0F);
        }
    }
}
