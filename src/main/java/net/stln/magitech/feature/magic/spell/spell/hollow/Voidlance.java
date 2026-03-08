package net.stln.magitech.feature.magic.spell.spell.hollow;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.EntityHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.VoidGlowParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Voidlance extends BeamSpell {

    public Voidlance() {
        super(new SpellConfig.Builder(Element.HOLLOW, SpellShape.BEAM, 120, 40)
                .charge(4)
                .property(SpellPropertyInit.DAMAGE, 7.0F)
                .property(SpellPropertyInit.MAX_RANGE, 64F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.1F)
                .endSound(SoundInit.VOIDLANCE)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void additionalBeamProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable Entity target, Vec3 end) {
        final List<Vec3>[] vec3s = new List[]{new ArrayList<>()};
        TickScheduler.schedule(2, () -> vec3s[0] = fractalBeam(level, caster, wand, end, 1), level.isClientSide);
        TickScheduler.schedule(4, () -> {
            for (Vec3 hit : vec3s[0]) {
                fractalBeam(level, caster, wand, hit, 2);
            }
        }, level.isClientSide);
    }

    public List<Vec3> fractalBeam(Level level, LivingEntity caster, ItemStack wand, Vec3 end, int order) {
        List<Vec3> vec3s = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) != 1) {
                        continue;
                    }
                    Vec3 dir = new Vec3(i, j, k).normalize();
                    float beamradius = MagicPerformanceHelper.getEffectiveBeamRadius(caster, wand, this);
                    Vec3 hit = EntityHelper.raycastBeam(caster, (double) 24 / Math.pow(order, 2), end.add(dir.scale(0.5)), dir, beamradius);
                    Entity target = EntityHelper.raycastBeamEntity(caster, (double) 24 / Math.pow(order, 2), end.add(dir.scale(0.5)), dir, beamradius);

                    if (!level.isClientSide) {
                        hitTarget(level, caster, wand, target);
                        SoundHelper.broadcastSound(level, caster, hit, getConfig().endSound());
                    } else {
                        Vec3 start = end.add(dir.scale(0.5));
                        addBeamVFX(level, caster, end, hit);
                    }
                    vec3s.add(hit);
                }
            }
        }
        return vec3s;
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(new MobEffectInstance(MobEffectInit.VOIDROT, 80, 0), livingTarget);
        }
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        EffectHelper.lineEffect(level, new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(1, 21), 1.0F), start, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.3F, 0.0F, 1.0F), new Vector3f(0.5F, 0.0F, 1.0F), end.toVector3f(), 0.7F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(1, 21), 1.0F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
    }
}
