package net.stln.magitech.feature.magic.spell.spell.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.core.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.core.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.BeamParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Enercrux extends BeamSpell {

    public Enercrux() {
        super(new SpellConfig.Builder(Element.MANA, SpellShape.INFUSE, 30, 30)
                .charge(5)
                .property(SpellPropertyInit.DAMAGE, 5.0F)
                .property(SpellPropertyInit.MAX_RANGE, 8F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.1F)
                .endSound(SoundInit.ENERCRUX)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void additionalBeamProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable Entity target, Vec3 hitPos) {
        if (ManaTransferHelper.getManaContainer(level, BlockPos.containing(hitPos), null) instanceof IBasicManaHandler handler) {
            handler.addMana(10000L);
        }
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.15F, 0.1F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burstSquare(level, end, element, 10, 0.1F);
        TrailVFX.zapTrail(level, start, end, 0.5F, 0.5F, 0.5F, 15, element);
    }
}
