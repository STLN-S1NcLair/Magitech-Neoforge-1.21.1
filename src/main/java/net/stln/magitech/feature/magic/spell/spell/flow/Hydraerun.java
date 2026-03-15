package net.stln.magitech.feature.magic.spell.spell.flow;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class Hydraerun extends Spell {

    public Hydraerun() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.UTILITY, 450, 60)
                .charge(10)
                .property(SpellPropertyInit.DURATION_TIME, 2400)
                .endSound(SoundInit.HYDRAERUN)
                .castAnim("wand_chant")
                .endAnim("wand_shoot")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        if (!level.isClientSide) {
            caster.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, MagicPerformanceHelper.getEffectiveDurationTime(caster, wand, this), 0));
        }
    }

    @Override
    protected void endVFX(Level level, LivingEntity caster) {
        Vec3 casterPos = caster.position();
        level.addParticle(new SquareFieldParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 1.0F, 1, 0, 15, 1.0F), caster.getX(), caster.getY() + 0.1, caster.getZ(), 0, 0, 0);
        for (int i = 0; i < 50; i++) {
            level.addParticle(new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(10, 30), 0.87F),
                    casterPos.x + Mth.nextDouble(caster.getRandom(), -1, 1), casterPos.y + caster.getBbHeight() * 0.5 + Mth.nextDouble(caster.getRandom(), -1, 1), casterPos.z + Mth.nextDouble(caster.getRandom(), -1, 1),
                    (caster.getRandom().nextFloat() - 0.5) / 2, (caster.getRandom().nextFloat() - 0.5) / 2 + 0.1, (caster.getRandom().nextFloat() - 0.5) / 2);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 1.0F, caster.getRandom().nextInt(5, 7), (float) ((caster.getRandom().nextFloat() - 0.5) / 10), 15, 0.8F),
                    casterPos.x + Mth.nextDouble(caster.getRandom(), -1, 1), casterPos.y + caster.getBbHeight() * 0.5 + Mth.nextDouble(caster.getRandom(), -1, 1), casterPos.z + Mth.nextDouble(caster.getRandom(), -1, 1),
                    (caster.getRandom().nextFloat() - 0.5) / 2, (caster.getRandom().nextFloat() - 0.5) / 2 + 0.1, (caster.getRandom().nextFloat() - 0.5) / 2);
        }
        EffectHelper.entityEffect(level, () -> new SquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.0F), new Vector3f(0.9F, 1.0F, 0.0F), 0.75F, caster.getRandom().nextInt(5, 7), (float) ((caster.getRandom().nextFloat() - 0.5) / 10), 15, 1.0F),
                () -> new Vec3(caster.getRandom().nextFloat() / 8, caster.getRandom().nextFloat() / 8, caster.getRandom().nextFloat() / 8), caster, 30);
        EffectHelper.entityEffect(level, () -> new BlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 0, 0, level.random.nextInt(10, 30), 0.87F),
                () -> new Vec3(caster.getRandom().nextFloat() / 8, caster.getRandom().nextFloat() / 8, caster.getRandom().nextFloat() / 8), caster, 30);
    }
}
