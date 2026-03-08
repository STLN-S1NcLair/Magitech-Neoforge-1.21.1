package net.stln.magitech.feature.magic.spell.spell.phantom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.*;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.MembraneParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
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
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        Vec3 startPos = start.add(0, caster.getBbHeight() * 0.7, 0).add(forward.scale(0.5));
        Vec3 back = Vec3.directionFromRotation(caster.getRotationVector()).scale(-1);
        Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(back.scale(1));
        EffectHelper.lineEffect(level, new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(10, 40), 0.85F), startPos, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 1.0F, 0.9F), new Vector3f(0.6F, 1.0F, 0.3F), end.toVector3f(), 0.7F, 1, 1, 5, 1), startPos.x, startPos.y, startPos.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(10, 40), 0.85F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
        for (int j = 0; j < 10; j++) {
            level.addParticle(new MembraneParticleEffect(new Vector3f(1), new Vector3f(1),
                            5F, 1, 0.3F, level.random.nextInt(10, 40), 0.85F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    back.x * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.y * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.z * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2);
        }

        for (int i = 0; i < 15; i++) {
            Vec3 off = new Vec3(3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5));
            Vec3 randomBody = caster.position().add(0, caster.getBbHeight() / 2, 0).add(off);
            level.addParticle(new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, level.random.nextInt(10, 40), 0.85F),
                    randomBody.x, randomBody.y, randomBody.z, off.x / 40, off.y / 40, off.z / 40);
        }
        for (int i = 0; i < 15; i++) {
            Vec3 off = new Vec3(3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5));
            Vec3 randomBody = caster.position().add(0, caster.getBbHeight() / 2, 0).add(off);
            level.addParticle(new SquareParticleEffect(new Vector3f(1.0F, 1.0F, 0.9F), new Vector3f(0.6F, 1.0F, 0.3F), 1F, 1, 0, 15, 1.0F),
                    randomBody.x, randomBody.y, randomBody.z, off.x / 40, off.y / 40, off.z / 40);
        }
    }
}
