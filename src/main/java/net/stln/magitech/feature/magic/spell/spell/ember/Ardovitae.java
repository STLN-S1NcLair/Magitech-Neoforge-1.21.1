package net.stln.magitech.feature.magic.spell.spell.ember;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.particle.particle_option.FlameParticleEffect;
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

public class Ardovitae extends Spell {

    public Ardovitae() {
        super(new SpellConfig.Builder(Element.EMBER, SpellShape.RESILIENCE, 250, 60)
                .charge(15)
                .property(SpellPropertyInit.DURATION_TIME, 600)
                .endSound(SoundInit.ARDOVITAE)
                .castAnim("wand_chant")
                .endAnim("wand_shoot")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        if (!level.isClientSide) {
            int durationTime = MagicPerformanceHelper.getEffectiveDurationTime(caster, wand, this);
            caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, durationTime / 3, 0, false, true, true));
            caster.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, durationTime, 2, false, true, true));
        }
    }

    @Override
    protected void endVFX(Level level, LivingEntity caster) {
        Vector3f fromCol = new Vector3f(1.0F, 0.7F, 0.3F);
        Vector3f toCol = new Vector3f(1.0F, 0.2F, 0.0F);
        Vec3 casterPos = caster.position();
        level.addParticle(new SquareFieldParticleEffect(fromCol, toCol, 1.0F, 1, 0, 15, 1.0F), caster.getX(), caster.getY() + 0.1, caster.getZ(), 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new FlameParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2.0F, 1, 0, level.random.nextInt(5, 8), 0.9F),
                    casterPos.x, casterPos.y, casterPos.z, (caster.getRandom().nextFloat() - 0.5) / 20, (caster.getRandom().nextFloat() - 0.5) / 6 + 0.1, (caster.getRandom().nextFloat() - 0.5) / 20);
            level.addParticle(new SquareParticleEffect(fromCol, toCol, 1.0F, caster.getRandom().nextInt(5, 7), (float) ((caster.getRandom().nextFloat() - 0.5) / 10), 20, 0.9F),
                    casterPos.x, casterPos.y, casterPos.z, (caster.getRandom().nextFloat() - 0.5) / 20, (caster.getRandom().nextFloat() - 0.5) / 6 + 0.2, (caster.getRandom().nextFloat() - 0.5) / 20);
        }
        EffectHelper.entityEffect(level, () -> new SquareParticleEffect(fromCol, toCol, 1.0F, caster.getRandom().nextInt(5, 7), (float) ((caster.getRandom().nextFloat() - 0.5) / 10), 15, 0.9F),
                () -> new Vec3(0, caster.getRandom().nextFloat() / 8, 0), caster, 30);
        EffectHelper.entityEffect(level, () -> new FlameParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.5F, 0, 0, level.random.nextInt(5, 8), 0.9F),
                () -> new Vec3(0, caster.getRandom().nextFloat() / 8, 0), caster, 30);
    }
}
