package net.stln.magitech.feature.magic.spell.spell.flow;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.effect.visual.particle.particle_option.BlowParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class Syllaeze extends Spell {

    public Syllaeze() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.SPRAY, 80, 5)
                .continuous(2)
                .tickSound(SoundInit.SYLLAEZE, 5)
                .castAnim("wand_flight")
        );
    }

    @Override
    public void tickSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, int ticks, boolean charging) {
        caster.fallDistance = 0;
        caster.addDeltaMovement(Vec3.directionFromRotation(caster.getRotationVector()).scale(MagicPerformanceHelper.getEffectiveEffectStrength(caster, wand, this)));
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        Vec3 back = Vec3.directionFromRotation(caster.getRotationVector()).scale(-1);
        Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(back.scale(1));
        for (int i = 0; i < 5; i++) {
            level.addParticle(new BlowParticleEffect(new Vector3f(1), new Vector3f(1),
                            5F, 1, 0.3F, level.random.nextInt(10, 30), 0.87F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    back.x * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.y * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.z * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2);
        }
    }
}
