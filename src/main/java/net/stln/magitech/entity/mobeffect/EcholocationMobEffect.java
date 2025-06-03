package net.stln.magitech.entity.mobeffect;


import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.stln.magitech.particle.particle_option.PowerupNoCullParticleEffect;
import net.stln.magitech.particle.particle_option.WaveNoCullParticleEffect;
import net.stln.magitech.util.TickScheduler;
import org.joml.Vector3f;

import java.util.function.Predicate;

class EcholocationMobEffect extends CustomMobEffect {
    protected EcholocationMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().isClientSide) {
                for (int i = -5; i < 6; i++) {
                    for (int j = -5; j < 6; j++) {
                        for (int k = -5; k < 6; k++) {
                            BlockPos blockPos = livingEntity.blockPosition().above(i).east(j).south(k);
                            BlockState blockState = livingEntity.level().getBlockState(blockPos);
                            if (blockState.getTags().anyMatch(Predicate.isEqual(Tags.Blocks.ORES))) {
                                Vec3 center = blockPos.getCenter();
                                Vec3 corner = Vec3.atLowerCornerOf(blockPos);
                                livingEntity.level().addParticle(new WaveNoCullParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 4F, 1, 0), center.x, center.y, center.z, 0, 0, 0);
                                for (int l = 0; l < 3; l++) {
                                    livingEntity.level().addParticle(new PowerupNoCullParticleEffect(new Vector3f(0.0F, 0.5F, 0.5F), new Vector3f(0.0F, 1.0F, 1.0F), 1F, 1, 0),
                                            corner.x + livingEntity.getRandom().nextFloat(), corner.y + livingEntity.getRandom().nextFloat(), corner.z + livingEntity.getRandom().nextFloat(), 0, 0, 0);
                                }
                            }
                        }
                    }
                }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
