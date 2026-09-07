package net.stln.magitech.feature.magic.spell.spell.mana;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.BeamParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellHelper;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Thaumiris extends BeamSpell {

    public Thaumiris() {
        super(new SpellConfig.Builder(Element.MANA, SpellShape.BEAM, 30, 25)
                .charge(1)
                .property(SpellPropertyInit.DAMAGE, 4.0F)
                .property(SpellPropertyInit.MAX_RANGE, 24F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.25F)
                .endSound(SoundInit.THAUMIRIS)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 3, 0.15F, 0.1F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burstSquare(level, end, element, 10, 0.2F);
        TrailVFX.zapTrail(level, start, end, 0.5F, 0.75F, 0.5F, 25, element);
    }
}
