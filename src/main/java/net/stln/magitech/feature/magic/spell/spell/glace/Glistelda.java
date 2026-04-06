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
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.DamageSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
        } else {
            addDashVFX(level, caster, duration);
        }
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < duration; i++) {
            TickScheduler.schedule(i, () -> {
                List<Entity> nearbyEntities = CombatHelper.getEntitiesInBox(level, caster, caster.position(), new Vec3(3, 3, 3));
                entities.addAll(nearbyEntities);
                if (level.isClientSide) {
                    for (Entity entity : entities) {
                        addTargetMarkVFX(level, caster, entity);
                    }
                }
            }, level.isClientSide);
        }
        TickScheduler.schedule(duration, () -> {
            for (Entity entity : entities) {
                hitTarget(level, caster, wand, entity);
                if (!level.isClientSide) {
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
        Element element = getConfig().element();
        Vec3 pos = EffectHelper.getRandomBody(target);
        PointVFX.burst(level, pos, element, SquareParticles::squareParticle, 5, 0.1F);
        PointVFX.burst(level, pos, element, ElementParticles::snowParticle, 5, 0.1F);
    }

    public void addTargetAttackVFX(Level level, LivingEntity caster, Entity target) {
        Element element = getConfig().element();
        Vec3 pos = CombatHelper.getBodyPos(target);
        PointVFX.burst(level, pos, element, ((lev, position, elm) -> SquareParticles.squareGravityParticle(level, pos, elm, 0.2F)), 20, 0.3F);
        PointVFX.burst(level, pos, element, ElementParticles::snowParticle, 20, 0.3F);
    }

    public void addDashVFX(Level level, LivingEntity caster, int duration) {
        Element element = getConfig().element();
        TrailPointBuilder trail = TrailPointBuilder.create(5);
        TrailPointBuilder longTrail = TrailPointBuilder.create(20);
        Vec3 position = CombatHelper.getBodyPos(caster);
        trail.addTrailPoint(position);
        longTrail.addTrailPoint(position);
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailData trailData = new TrailData(level, builderFunc, trail, element.getPrimary(), element.getSecondary(), 2.0F, 0.9F);
        TrailData longTrailData = new TrailData(level, builderFunc, longTrail, element.getSecondary(), element.getDark(), 1.0F, 0.5F);
        TrailRenderer.add(trailData);
        TrailRenderer.add(longTrailData);

        for (int i = 0; i < duration; i++) {
            TickScheduler.schedule(i, () -> {
                Vec3 pos = caster.position();
                Vec3 old = CombatHelper.getOldBodyPos(caster);
                trail.addTrailPoint(old);
                longTrail.addTrailPoint(old);
                Vec3 up = new Vec3(0, 1, 0);
                PointVFX.spraySquare(level, pos, element, up, 10, 0.05F, 0.05F);
                PointVFX.spray(level, pos, element, ElementParticles::snowParticle, up, 10, 0.05F, 0.05F);
            }, level.isClientSide);
        }
    }
}
