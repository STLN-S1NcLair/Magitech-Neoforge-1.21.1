package net.stln.magitech.feature.magic.spell.spell.magic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

public class Envistra extends BlinkSpell {

    public Envistra() {
        super(new SpellConfig.Builder(Element.MAGIC, SpellShape.DASH, 40, 50)
                .charge(5)
                .property(SpellPropertyInit.DAMAGE, 6.0F)
                .property(SpellPropertyInit.MAX_RANGE, 10F)
                .endSound(SoundInit.ENVISTRA)
                .castAnim("charge_wand")
                .endAnim("wand_blink")
        );
    }

    @Override
    protected void addBlinkVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = getConfig().element();
        Vec3 bodyAdjustment = new Vec3(0, caster.getBbHeight() * 0.7F, 0);
        Vec3 bodyStart = start.add(bodyAdjustment);
        Vec3 bodyEnd = end.add(bodyAdjustment);
        TrailVFX.directionalTrail(level, bodyStart, bodyEnd, 2.0F, 20, element);
        LineVFX.destinationLinedSquare(level, bodyStart, bodyEnd, element, new Section(0F, 1F), 5, 0.5F, 0.2F);
        LineVFX.destinationLined(level, bodyStart, bodyEnd, element, ElementParticles::runeParticle, new Section(0F, 1F), 5, 0.2F, 0.2F);
        PointVFX.burstSquare(level, bodyEnd, element, 50, 0.3F);
        PointVFX.burst(level, bodyEnd, element, ElementParticles::runeParticle, 50, 0.3F);
    }
}
