package net.stln.magitech.feature.magic.spell.spell.ember;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.RenderTypeTokenInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.particle.particle_option.FlameParticleEffect;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
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
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class Blazewend extends DamageSpell {

    public Blazewend() {
        super(new SpellConfig.Builder(Element.EMBER, SpellShape.DASH, 100, 70)
                .property(SpellPropertyInit.DAMAGE, 8.0F)
                .property(SpellPropertyInit.EFFECT_STRENGTH, 1.0F)
                .endSound(SoundInit.BLAZEWEND)
                .endAnim("wand_blink")
        );
    }

    final static int DURATION = 10;

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        float strength = MagicPerformanceHelper.getEffectiveEffectStrength(caster, wand, this);
        Vec3 front = Vec3.directionFromRotation(caster.getRotationVector());
        if (!level.isClientSide) {
            caster.addDeltaMovement(front.scale(strength * 2));
            caster.hurtMarked = true;
        }
        if (!level.isClientSide) {
            for (int i = 0; i < DURATION; i++) {
                int finalI = i;
                TickScheduler.schedule(i, () -> {
                    if (!level.isClientSide) {
                        List<Entity> nearbyEntities = CombatHelper.getEntitiesInBox(level, caster, caster.position(), new Vec3(3, 3, 3));
                        Vec3 newFront = Vec3.directionFromRotation(caster.getRotationVector());
                        caster.addDeltaMovement(newFront.scale(strength / 3));
                        SoundHelper.broadcastSound(level, caster, this.getConfig().endSound());
                        for (Entity entity : nearbyEntities) {
                            hitTarget(level, caster, wand, entity);
                        }
                        if (finalI + 1 == DURATION) {
                            caster.setDeltaMovement(caster.getDeltaMovement().scale(0.3));
                        }
                        caster.fallDistance = 0;
                        caster.hurtMarked = true;
                    }
                }, level.isClientSide);
            }
        } else {
            addJetVFX(level, caster);
        }
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        target.setRemainingFireTicks(Math.min(200, target.getRemainingFireTicks() + 100));
    }

    protected void addJetVFX(Level level, LivingEntity caster) {
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

        for (int i = 0; i < DURATION + 5; i++) {
            TickScheduler.schedule(i, () -> {
                Vec3 old = CombatHelper.getOldBodyPos(caster);
                Vec3 current = CombatHelper.getBodyPos(caster);
                trail.addTrailPoint(old);
                longTrail.addTrailPoint(old);
                LineVFX.spreadLined(level, current, old, element, SquareParticles::squareBlastParticle, new Section(0F, 1F), 5F, 0.5F, 0.2F);
                LineVFX.spreadLined(level, current, old, element, (lvl, p, elm) -> PresetHelper.bigger(ElementParticles.smokeParticle(lvl, p, elm)), new Section(0F, 1F), 5F, 0.5F, 0.2F);
            }, level.isClientSide);
        }
    }
}
