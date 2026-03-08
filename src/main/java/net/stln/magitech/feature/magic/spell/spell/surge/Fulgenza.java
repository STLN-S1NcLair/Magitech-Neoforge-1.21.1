package net.stln.magitech.feature.magic.spell.spell.surge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.BeamParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellHelper;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.EntityHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SparkParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.ZapParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class Fulgenza extends BeamSpell {

    public Fulgenza() {
        super(new SpellConfig.Builder(Element.SURGE, SpellShape.BEAM, 200, 85)
                .charge(80)
                .property(SpellPropertyInit.DAMAGE, 18.0F)
                .property(SpellPropertyInit.MAX_RANGE, 64F)
                .property(SpellPropertyInit.BEAM_RADIUS, 1.0F)
                .chargeSound(SoundInit.FULGENZA_CHARGE)
                .endSound(SoundInit.FULGENZA)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void additionalBeamProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable Entity target, Vec3 hitPos) {
        if (target != null) {
            Set<Entity> attackList = new HashSet<>();
            Vec3 targetBody = EntityHelper.getBodyPos(target);
            attackList.addAll(EntityHelper.getEntitiesInBox(level, caster, hitPos, new Vec3(12.0, 12.0, 12.0)));
            attackList.remove(target);
            for (Entity chainTarget : attackList) {
                Vec3 chainBody = EntityHelper.getBodyPos(chainTarget);
                if (!SpellHelper.canSee(level, caster, targetBody, chainBody)) {
                    continue;
                }

                if (!level.isClientSide) {
                    hitTarget(level, caster, wand, chainTarget, this.getConfig().properties().get(SpellPropertyInit.DAMAGE), 0.5F);
                } else {
                    addChainVFX(level, caster, hitPos, chainBody);
                }
            }
        }
    }

    protected void addChainVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        level.addParticle(new ZapParticleEffect(new Vector3f(1), new Vector3f(1), end.toVector3f(), 2F, 3, 0, level.random.nextInt(2, 5), 1.0F), start.x, start.y, start.z,
                0, 0, 0);
        TickScheduler.schedule(5, () -> {
            EffectHelper.lineEffect(level, new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 3, 0, level.random.nextInt(5, 15), 0.99F), end, start, 3, false);
        }, level.isClientSide);
    }

//    @Override
//    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
//        EffectHelper.lineEffect(level, new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 3, 0, level.random.nextInt(5, 15), 0.99F), start, end, 2, false);
//        for (int i = 0; i < 3; i++) {
//            level.addParticle(new ZapParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F),
//                            new Vector3f((float) (end.x), (float) (end.y), (float) (end.z)), 1.0F, 1, 0, level.random.nextInt(2, 5), 1.0F),
//                    start.x, start.y, start.z, 0, 0, 0);
//        }
//        level.addParticle(new BeamParticleEffect(new Vector3f(0.5F, 0.5F, 0.7F), new Vector3f(0.3F, 0.3F, 0.5F), end.toVector3f(), 1.0F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
//        level.addParticle(new BeamParticleEffect(new Vector3f(0.3F, 0.3F, 0.4F), new Vector3f(0.2F, 0.2F, 0.4F), end.toVector3f(), 3.0F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
//        for (int i = 0; i < 5; i++) {
//            level.addParticle(new ZapParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F),
//                            new Vector3f((float) (end.x + (caster.getRandom().nextFloat() - 0.5)), (float) (end.y + (caster.getRandom().nextFloat() - 0.5)), (float) (end.z + (caster.getRandom().nextFloat() - 0.5))), 1.0F, 3, 0, level.random.nextInt(2, 5), 1.0F),
//                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) * 2, (caster.getRandom().nextFloat() - 0.5) * 2, (caster.getRandom().nextFloat() - 0.5) * 2);
//        }
//    }


    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.linedSquare(level, start, end, element, new Section(0F, 1F), 10, 0.2F, 0.3F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burstSquare(level, end, element, 50, 0.3F);
        PointVFX.zap(level, end, element, 5, 0.5F, 4.0F, 1.0F, 1.0F, 15);

        TrailVFX.zapTrail(level, start, end, 1.0F, 0.5F, 1.0F, 5, element);
        TrailVFX.zapTrail(level, end, start, 0.5F, 0.5F, 1.0F, 5, element);

        TrailVFX.zapTrail(level, start, end, 1.0F, 0.5F, 1.5F, 25, element);
        TrailVFX.zapTrail(level, end, start, 0.5F, 0.5F, 1.5F, 25, element);
    }
}
