package net.stln.magitech.feature.magic.spell.spell.glace;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.DamageSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.EntityHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.effect.visual.particle.particle_option.FrostParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareFieldParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public class Glistelda extends DamageSpell {

    public Glistelda() {
        super(new SpellConfig.Builder(Element.GLACE, SpellShape.DASH, 80, 60)
                .property(SpellPropertyInit.DAMAGE, 8.0F)
                .property(SpellPropertyInit.DURATION_TIME, 15)
                .endSound(SoundInit.GLISTELDA)
                .endAnim("wand_blink")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        int duration = MagicPerformanceHelper.getEffectiveDurationTime(caster, wand, this);
        if (!level.isClientSide) {
            caster.addEffect(new MobEffectInstance(MobEffectInit.LEAP_STEP, duration, 6, false, false, true));
        }
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < duration; i++) {
            TickScheduler.schedule(i, () -> {
                List<Entity> nearbyEntities = EntityHelper.getEntitiesInBox(level, caster, caster.position(), new Vec3(3, 3, 3));
                entities.addAll(nearbyEntities);
                EffectHelper.entityEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(50, 60), 0.99F), caster, 4);
                EffectHelper.entityEffect(level, new SquareParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1.0F, 1, 0, 15, 1.0F), caster, 10);
                for (Entity entity : nearbyEntities) {
                    addTargetMarkVFX(level, caster, entity);
                }
                addDashVFX(level, caster);
            }, level.isClientSide);
        }
        TickScheduler.schedule(duration, () -> {
            for (Entity entity : entities) {
                if (!level.isClientSide) {
                    hitTarget(level, caster, wand, entity);
                    SoundHelper.broadcastSound(level, entity, Optional.of(SoundInit.GLISTELDA_BREAK));
                } else {
                    addTargetAttackVFX(level, caster, entity);
                }
            }
        }, level.isClientSide);
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        target.setTicksFrozen(Math.min(target.getTicksFrozen() + 170, 300));
    }

    public void addTargetMarkVFX(Level level, LivingEntity caster, Entity target) {
        EffectHelper.entityEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(50, 60), 0.99F), target, 1);
        level.addParticle(new SquareFieldParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1.0F, caster.getRandom().nextInt(3, 6), 0, 15, 1.0F), target.getX(), target.getY() + 0.1, target.getZ(), 0, 0, 0);
    }

    public void addTargetAttackVFX(Level level, LivingEntity caster, Entity target) {
        for (int i = 0; i < 60; i++) {
            float rotSpeed = caster.getRandom().nextFloat() / 5 - 0.1F;

            Vec3 offset = new Vec3(3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5));
            Vec3 randomBody = target.position().add(0, target.getBbHeight() / 2, 0).add(offset);

            level.addParticle(new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, rotSpeed, level.random.nextInt(50, 60), 0.99F),
                    randomBody.x, randomBody.y, randomBody.z, offset.x / 10, offset.y / 10, offset.z / 10);
        }

    }

    public void addDashVFX(Level level, LivingEntity caster) {
        level.addParticle(new SquareFieldParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1.0F, caster.getRandom().nextInt(3, 6), 0, 15, 1.0F), caster.getX(), caster.getY() + 0.1, caster.getZ(), 0, 0, 0);
    }
}
