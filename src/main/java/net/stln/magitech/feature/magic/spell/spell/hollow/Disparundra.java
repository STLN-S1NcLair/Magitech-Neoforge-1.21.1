package net.stln.magitech.feature.magic.spell.spell.hollow;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.VoidGlowParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

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
        EffectHelper.lineEffect(level, new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(1, 21), 1.0F), start, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.3F, 0.0F, 1.0F), new Vector3f(0.5F, 0.0F, 1.0F), end.toVector3f(), 0.7F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(1, 21), 1.0F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
    }
}
