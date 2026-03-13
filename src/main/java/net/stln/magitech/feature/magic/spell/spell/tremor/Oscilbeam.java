package net.stln.magitech.feature.magic.spell.spell.tremor;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.BeamParticles;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

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
        Element element = this.getConfig().element();
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.15F, 0.1F);
        LineVFX.destinationLined(level, start, end, element, (lvl, pos, elm) -> RingParticles.ringParticle(lvl, pos, end.subtract(start).normalize(), elm), new Section(0F, 1F), 0.5F, 0.0F, 0.0F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burst(level, end, element, SquareParticles::squareGravityParticle, 50, 0.5F);
    }
}
