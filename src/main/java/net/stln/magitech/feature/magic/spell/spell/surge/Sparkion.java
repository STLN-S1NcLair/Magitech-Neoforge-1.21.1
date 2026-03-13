package net.stln.magitech.feature.magic.spell.spell.surge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.*;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.effect.visual.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.helper.VectorHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public class Sparkion extends SpraySpell {

    public Sparkion() {
        super(new SpellConfig.Builder(Element.SURGE, SpellShape.SPRAY, 30, 25)
                .continuous(1.5F)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 3.0F)
                .tickSound(SoundInit.ZAP, 5)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void additionalSprayProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, Set<Entity> targets) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        Vec3 bodyPos = CombatHelper.getBodyPos(caster);
        Vec3 offset = bodyPos.add(forward.scale(1));
        if (targets.isEmpty()) {
            if (level.isClientSide) {
                addNoTargetVFX(level, caster);
            }
        } else {
            // 電撃効果
            for (Entity target : new HashSet<>(targets)) {
                if (level.isClientSide) {
                    addChainVFX(level, caster, offset, target);
                }
            }
            // 連鎖
            Set<Entity> visited = new HashSet<>(targets);
            visited.add(caster);
            for (int i = 0; i < 2; i++) {
                for (Entity entity : new HashSet<>(targets)) {

                    Vec3 entityBody = CombatHelper.getBodyPos(entity);
                    List<Entity> chainList = CombatHelper.getEntitiesInBox(level, entity, entityBody, new Vec3(3.0, 3.0, 3.0));

                    for (Entity chain : new HashSet<>(chainList)) {
                        if (visited.contains(chain)) continue;

                        Vec3 chainBody = CombatHelper.getBodyPos(chain);
                        visited.add(chain);

                        if (!SpellHelper.canSee(level, caster, entityBody, chainBody)) {
                            continue;
                        }
                        targets.add(chain);
                        if (!level.isClientSide) {
                            hitTarget(level, caster, wand, SpellPropertyInit.CONTINUOUS_DAMAGE, chain);
                        } else {
                            addChainVFX(level, caster, entityBody, chain);
                        }
                    }
                    targets.remove(entity);
                }
            }
        }
    }

    public void addNoTargetVFX(Level level, LivingEntity caster) {
        Element element = this.getConfig().element();
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        Vec3 bodyPos = CombatHelper.getBodyPos(caster);
        Vec3 offset = bodyPos.add(forward.scale(0.5));
        Vec3 center = caster.getEyePosition().add(forward);
        Vec3 center2 = center.add(forward.scale(2));
        Vec3 random = center2.add(VectorHelper.randScaledRandom(level.random).scale(2.0F));
        TrailVFX.directionalZapTrail(level, offset, random, 0.25F, 2.0F, 0.25F, 10, element);
    }

    public void addChainVFX(Level level, LivingEntity caster, Vec3 from, Entity target) {
        Element element = this.getConfig().element();
        Vec3 targetBodyPos = CombatHelper.getBodyPos(target);
        TrailVFX.zapTrail(level, from, targetBodyPos, 0.25F, 2.0F, 0.25F, 10, element);
    }

    public void addChainVFX(Level level, LivingEntity caster, Entity from, Entity target) {
        addChainVFX(level, caster, CombatHelper.getBodyPos(from), target);
    }
}
