package net.stln.magitech.feature.magic.spell.spell.mana;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.*;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.cooldown.CooldownData;
import net.stln.magitech.feature.magic.cooldown.CooldownHelper;
import net.stln.magitech.feature.magic.spell.*;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.VectorHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Quintex extends Spell {

    public Quintex() {
        super(new SpellConfig.Builder(Element.MANA, SpellShape.UTILITY, 2000, 20)
                .property(SpellPropertyInit.DURATION_TIME, 200)
                .charge(30)
                .endSound(SoundInit.QUINTEX)
                .castAnim("wand_chant")
                .endAnim("wand_shoot")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        if (!level.isClientSide) {
            List<ISpell> spells = MagitechRegistries.SPELL.holders().map(holder -> (Holder<ISpell>) holder).map(Holder::value).toList();
            int duration = MagicPerformanceHelper.getEffectiveDurationTime(caster, wand, this);
            for (ISpell spell : spells) {
                CooldownHelper.extendCooldown(caster, spell, duration);
            }
            caster.addEffect(new MobEffectInstance(MobEffectInit.MANA_REGENERATION, duration, 3));
        }
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        Element element = getConfig().element();
        Vec3 pos = caster.position();
        Vec3 up = new Vec3(0, 1, 0);
        EntityVFX.powerupAura(level, element, caster, Section.cover(), 1);
        PointVFX.ringSquare(level, pos, element, up, 1, 0.15F, 0.5F, 0.0F);
    }

    @Override
    protected void endVFX(Level level, LivingEntity caster) {
        Element element = getConfig().element();
        Vec3 pos = caster.position();
        Vec3 up = new Vec3(0, 1, 0);
        PointVFX.ringSquare(level, pos, element, up, 30, 0.2F, 1.2F, 0.05F);
        PointVFX.burst(level, pos.add(0, 0.1F, 0), element, (lvl, p, elm) -> PresetHelper.bigger(PresetHelper.longer(RingParticles.ringReversedParticle(lvl, p, up, elm)), 4.0F), 1, 0.0F);
        EntityVFX.powerupAura(level, element, caster, Section.cover(), 80);
        AreaVFX.areaLight(level, element, pos, 1.0F, 1.0F, 20);
        for (int i = 0; i < 5; i++) {
            Vec3 random = pos.add(VectorHelper.randScaledRandom(level.random).scale(2.0F));
            TrailVFX.directionalZapTrail(level, pos, random, 0.25F, 1.0F, 0.25F, 20, element);

        }
    }
}
