package net.stln.magitech.feature.magic.spell.spell.flow;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.particle.particle_option.BlowParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareFieldParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class Nymphora extends Spell {

    public Nymphora() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.RESILIENCE, 300, 60)
                .charge(20)
                .property(SpellPropertyInit.HEALING_AMOUNT, 4.0F)
                .endSound(SoundInit.NYMPHORA)
                .castAnim("wand_chant")
                .endAnim("wand_shoot")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        if (!level.isClientSide) {
            caster.heal(MagicPerformanceHelper.getEffectiveHealAmount(caster, wand, this));
        }
    }

    @Override
    protected void endVFX(Level level, LivingEntity caster) {
        Vec3 casterPos = caster.position();
        level.addParticle(new SquareFieldParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 1.0F, 1, 0, 15, 1.0F), caster.getX(), caster.getY() + 0.1, caster.getZ(), 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(10, 30), 0.87F),
                    casterPos.x, casterPos.y, casterPos.z, (caster.getRandom().nextFloat() - 0.5) / 6, (caster.getRandom().nextFloat() - 0.5) / 6 + 0.1, (caster.getRandom().nextFloat() - 0.5) / 6);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 1.0F, caster.getRandom().nextInt(5, 7), (float) ((caster.getRandom().nextFloat() - 0.5) / 10), 15, 1.0F),
                    casterPos.x, casterPos.y, casterPos.z, (caster.getRandom().nextFloat() - 0.5) / 6, (caster.getRandom().nextFloat() - 0.5) / 6 + 0.1, (caster.getRandom().nextFloat() - 0.5) / 6);
        }
        EffectHelper.entityEffect(level, () -> new SquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 0.75F, caster.getRandom().nextInt(5, 7), (float) ((caster.getRandom().nextFloat() - 0.5) / 10), 15, 1.0F),
                () -> new Vec3(0, caster.getRandom().nextFloat() / 8, 0), caster, 30);
        EffectHelper.entityEffect(level, () -> new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 0, 0, level.random.nextInt(10, 30), 0.87F),
                () -> new Vec3(0, caster.getRandom().nextFloat() / 8, 0), caster, 30);
    }
}
