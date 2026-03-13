package net.stln.magitech.feature.magic.spell.spell.flow;

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

public class Fluvinae extends BeamSpell {

    public Fluvinae() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.BEAM, 80, 40)
                .charge(8)
                .property(SpellPropertyInit.DAMAGE, 7.0F)
                .property(SpellPropertyInit.MAX_RANGE, 256F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.1F)
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
        Element element = this.getConfig().element();
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.4F, 0.2F);
        LineVFX.destinationLined(level, start, end, element, ElementParticles::leafParticle, new Section(0F, 1F), 5, 0.5F, 0.1F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burst(level, end, element, ((lev, pos, elm) -> SquareParticles.squareGravityParticle(level, pos, elm, 0.1F)), 20, 0.3F);
        PointVFX.burst(level, end, element, ElementParticles::leafParticle, 20, 0.2F);
    }
}
