package net.stln.magitech.feature.magic.spell.spell.phantom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.MembraneParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class Fadancea extends BlinkSpell {

    public Fadancea() {
        super(new SpellConfig.Builder(Element.PHANTOM, SpellShape.DASH, 50, 35)
                .charge(5)
                .property(SpellPropertyInit.DAMAGE, 4.0F)
                .property(SpellPropertyInit.MAX_RANGE, 15F)
                .endSound(SoundInit.FADANCEA)
                .castAnim("charge_wand")
                .endAnim("wand_blink")
        );
    }

    @Override
    protected void additionalBlinkProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, List<Entity> targets, Vec3 start, Vec3 hitPos) {
        if (!level.isClientSide) {
            Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
            caster.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 40, 0, false, false, true));
            caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 1, false, false, true));
            caster.addDeltaMovement(forward.normalize().scale(1).add(0, 0.6, 0));
            caster.hurtMarked = true;
        }
    }

    @Override
    protected void addBlinkVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = getConfig().element();
        Vec3 bodyAdjustment = new Vec3(0, caster.getBbHeight() * 0.7F, 0);
        Vec3 bodyStart = start.add(bodyAdjustment);
        Vec3 bodyEnd = end.add(bodyAdjustment);
        TrailVFX.directionalTrail(level, bodyStart, bodyEnd, 2.0F, 20, element);
        LineVFX.destinationLinedSquare(level, bodyStart, bodyEnd, element, new Section(0F, 1F), 5, 0.7F, 0.2F);
        LineVFX.destinationLined(level, bodyStart, bodyEnd, element, ElementParticles::glintParticle, new Section(0F, 1F), 5, 0.3F, 0.3F);
        PointVFX.burstSquare(level, bodyEnd, element, 50, 0.3F);
        PointVFX.burst(level, bodyEnd, element, ElementParticles::glintParticle, 50, 0.3F);
    }
}
