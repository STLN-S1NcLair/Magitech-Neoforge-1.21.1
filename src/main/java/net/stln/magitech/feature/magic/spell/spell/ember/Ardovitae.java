package net.stln.magitech.feature.magic.spell.spell.ember;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Ardovitae extends Spell {

    public Ardovitae() {
        super(new SpellConfig.Builder(Element.EMBER, SpellShape.RESILIENCE, 250, 60)
                .charge(15)
                .property(SpellPropertyInit.DURATION_TIME, 600)
                .endSound(SoundInit.ARDOVITAE)
                .castAnim("wand_chant")
                .endAnim("wand_shoot")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        if (!level.isClientSide) {
            int durationTime = MagicPerformanceHelper.getEffectiveDurationTime(caster, wand, this);
            caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, durationTime / 3, 0, false, true, true));
            caster.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, durationTime, 2, false, true, true));
        }
    }

    @Override
    protected void endVFX(Level level, LivingEntity caster) {
        Element element = getConfig().element();
        Vec3 pos = caster.position();
        Vec3 up = new Vec3(0, 1, 0);
        PointVFX.ringSquare(level, pos, element, up, 30, 0.15F, 0.5F, 0.1F);
        PointVFX.ring(level, pos, element, ElementParticles::smokeParticle, up, 30, 0.15F, 0.5F, 0.1F);
        PointVFX.burst(level, pos.add(0, 0.1F, 0), element, (lvl, p, elm) -> PresetHelper.bigger(PresetHelper.longer(RingParticles.ringReversedParticle(lvl, p, up, elm))), 1, 0.0F);
        EntityVFX.powerupAura(level, element, caster, Section.cover(), 40);
    }
}
