package net.stln.magitech.feature.magic.spell.spell.hollow;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Disparundra extends BlinkSpell {

    public Disparundra() {
        super(new SpellConfig.Builder(Element.HOLLOW, SpellShape.DASH, 40, 20)
                .charge(2)
                .property(SpellPropertyInit.MAX_RANGE, 30F)
                .endSound(SoundInit.DISPARUNDRA)
                .castAnim("charge_wand")
                .endAnim("wand_blink")
        );
    }

    @Override
    protected void additionalBlinkProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, List<Entity> targets, Vec3 start, Vec3 end) {
        if (!level.isClientSide) {
            TickScheduler.schedule(1, () -> {
                caster.addEffect(new MobEffectInstance(MobEffectInit.PHASELOCK, 5));
            }, level.isClientSide);
        }
    }

    @Override
    protected void addBlinkVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = getConfig().element();
        Vec3 bodyAdjustment = new Vec3(0, caster.getBbHeight() * 0.7F, 0);
        Vec3 bodyStart = start.add(bodyAdjustment);
        Vec3 bodyEnd = end.add(bodyAdjustment);
        TrailVFX.directionalTrail(level, bodyStart, bodyEnd, 2.0F, 20, element);
        LineVFX.destinationLinedSquare(level, bodyStart, bodyEnd, element, new Section(0F, 1F), 5, 0.5F, 0.2F);
        LineVFX.destinationLined(level, bodyStart, bodyEnd, element, ElementParticles::riftParticle, new Section(0F, 1F), 5, 0.2F, 0.2F);
    }
}
