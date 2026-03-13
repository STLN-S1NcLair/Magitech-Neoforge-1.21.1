package net.stln.magitech.feature.magic.spell.spell.ember;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.*;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.effect.visual.particle.particle_option.FlameParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class Blazewend extends DamageSpell {

    public Blazewend() {
        super(new SpellConfig.Builder(Element.EMBER, SpellShape.DASH, 100, 70)
                .property(SpellPropertyInit.DAMAGE, 8.0F)
                .property(SpellPropertyInit.EFFECT_STRENGTH, 1.0F)
                .endSound(SoundInit.BLAZEWEND)
                .endAnim("wand_blink")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        float strength = MagicPerformanceHelper.getEffectiveEffectStrength(caster, wand, this);
        final int DURATION = 10;
        Vec3 front = Vec3.directionFromRotation(caster.getRotationVector());
        if (!level.isClientSide) {
            caster.addDeltaMovement(front.scale(strength * 2));
            caster.hurtMarked = true;
        }
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
                } else {
                    addJetVFX(level, caster);
                }
            }, level.isClientSide);
        }
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        target.setRemainingFireTicks(Math.min(200, target.getRemainingFireTicks() + 100));
    }

    protected void addJetVFX(Level level, LivingEntity caster) {
        Vec3 back = Vec3.directionFromRotation(caster.getRotationVector()).scale(-1);
        Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(back.scale(1));
        for (int j = 0; j < 10; j++) {
            level.addParticle(new FlameParticleEffect(new Vector3f(1), new Vector3f(1),
                            5F, 1, 0.3F, level.random.nextInt(5, 8), 0.9F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    back.x * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.y * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.z * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2);
        }
    }
}
