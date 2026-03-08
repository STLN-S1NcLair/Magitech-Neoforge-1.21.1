package net.stln.magitech.feature.magic.spell.spell.surge;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.EntityHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.effect.visual.particle.particle_option.SparkParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.ZapParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class Arclume extends BlinkSpell {

    public Arclume() {
        super(new SpellConfig.Builder(Element.SURGE, SpellShape.DASH, 60, 55)
                .property(SpellPropertyInit.DAMAGE, 7.0F)
                .property(SpellPropertyInit.MAX_RANGE, 10F)
                .endSound(SoundInit.ARCLUME)
                .endAnim("wand_blink")
        );
    }

    @Override
    protected void additionalBlinkProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, List<Entity> targets, Vec3 start, Vec3 hitPos) {
        for (int i = 0; i < hitPos.subtract(start).length() / 2; i++) {
            int finalI = i;
            TickScheduler.schedule(i * 2, () -> {
                addLightning(level, caster, wand, start.lerp(hitPos, finalI / hitPos.subtract(start).length() * 2));
            }, level.isClientSide);
        }
    }

    private void addLightning(Level level, LivingEntity caster, ItemStack wand, Vec3 pos) {
        Vec3 surface = EntityHelper.findSurface(level, pos);
        Vec3 lightningTop = surface.add(0, Mth.randomBetween(caster.getRandom(), 5, 20), 0);
        List<Entity> entities = EntityHelper.getEntitiesInBox(level, caster, surface, new Vec3(2, 2, 2));

        SoundHelper.broadcastSound(level, caster, surface, this.getConfig().endSound());

        if (!level.isClientSide) {
            for (Entity target : entities) {
                hitTarget(level, caster, wand, target);
            }
        } else {
            level.addParticle(new ZapParticleEffect(new Vector3f(1), new Vector3f(1), lightningTop.toVector3f(), 2F, 3, 0, level.random.nextInt(2, 5), 1.0F), surface.x, surface.y, surface.z,
                    0, 0, 0);
            Vector3f fromColor = new Vector3f(1.0F, 1.0F, 1.0F);
            Vector3f toColor = new Vector3f(0.5F, 0.5F, 1.0F);
            float scale = 1.0F;
            float rotSpeed = 0.0F;
            int particleAmount = 20;

            for (int i = 0; i < particleAmount; i++) {
                int twinkle = caster.getRandom().nextInt(2, 4);

                double x = lightningTop.x;
                double y = lightningTop.y;
                double z = lightningTop.z;
                double vx = (caster.getRandom().nextFloat() - 0.5) / 10;
                double vy = (caster.getRandom().nextFloat() - 0.5) / 10;
                double vz = (caster.getRandom().nextFloat() - 0.5) / 10;
                level.addParticle(new SquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed, 15, 0.8F), x, y, z, vx, vy, vz);
            }

            for (int i = 0; i < particleAmount; i++) {
                int twinkle = caster.getRandom().nextInt(2, 4);

                double x = surface.x + Mth.randomBetween(caster.getRandom(), -0.2F, 0.2F);
                double y = surface.y + Mth.randomBetween(caster.getRandom(), -0.2F, 0.2F);
                double z = surface.z + Mth.randomBetween(caster.getRandom(), -0.2F, 0.2F);
                double vx = (caster.getRandom().nextFloat() - 0.5) / 2;
                double vy = (caster.getRandom().nextFloat() - 0.5);
                double vz = (caster.getRandom().nextFloat() - 0.5) / 2;
                level.addParticle(new UnstableSquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed, 15, 0.8F), x, y, z, vx, vy, vz);
            }
        }
    }

    @Override
    protected void addBlinkVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {

        EffectHelper.lineEffect(level, new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 3, 0, level.random.nextInt(5, 15), 0.99F), start, end, 2, false);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 3, 0, level.random.nextInt(5, 15), 0.99F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
    }
}
