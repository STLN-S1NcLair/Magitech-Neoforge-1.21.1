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


    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.2F, 0.1F);
        LineVFX.destinationLined(level, start, end, element, ElementParticles::snowParticle, new Section(0F, 1F), 5, 0.1F, 0.1F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burst(level, end, element, ((lev, pos, elm) -> SquareParticles.squareGravityParticle(level, pos, elm, 0.2F)), 30, 0.3F);
        PointVFX.burst(level, end, element, ElementParticles::snowParticle, 20, 0.3F);
    }
}
