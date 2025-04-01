package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.damage.DamageTypeInit;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.FrostParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShatterpiercerTrait extends Trait {

    @Override
    public Set<BlockPos> addAdditionalBlockBreakFirst(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        Set<BlockPos> posSet = new HashSet<>();
        posSet.add(pos);
        for (int i = 0; i < traitLevel / 2; i++) {
            if (level.getBlockState(pos.relative(direction, -i - 1)).getBlock() == level.getBlockState(pos).getBlock()) {
                posSet.add(pos.relative(direction, -i - 1));
            }
        }
        return posSet;
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        if (!isInitial) {
            for (int i = 0; i < 20; i++) {
                Vec3 offset = new Vec3(player.getRandom().nextFloat(), player.getRandom().nextFloat(), player.getRandom().nextFloat()).add(Vec3.atLowerCornerOf(pos));

                level.addParticle(new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1F, 1),
                        offset.x, offset.y, offset.z, 0, 0, 0);
                level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.1F, 0.7F);
            }
        }
    }

    @Override
    public void onAttackEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onAttackEntity(player, level, stack, traitLevel, stats, target);
        if (target instanceof LivingEntity livingEntity) {
            if (livingEntity.getPercentFrozen() > 0.8F) {
                Vec3 range = new Vec3(3, 3, 3);
                ResourceKey<DamageType> damageType = DamageTypeInit.GLACE_DAMAGE;
                DamageSource damageSource = stack.has(DataComponents.CUSTOM_NAME) ? player.damageSources().source(damageType, player) : player.damageSources().source(damageType);
                List<Entity> list = level.getEntities(player, new AABB(target.position().subtract(range), target.position().add(range)), entity -> entity instanceof LivingEntity);
                        for (Entity entity : list) {
                            entity.hurt(damageSource, stats.getStats().get(ToolStats.ELM_ATK_STAT) * (float)Math.sqrt(traitLevel - 0.5));
                            EffectUtil.entityEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1F, 1), entity, 60);
                        }
                for (int i = 0; i < 60; i++) {
                    Vec3 offset = new Vec3(3 * (player.getRandom().nextFloat() - 0.5), 3 * (player.getRandom().nextFloat() - 0.5), 3 * (player.getRandom().nextFloat() - 0.5));
                    Vec3 randomBody = livingEntity.position().add(0, livingEntity.getBbHeight() / 2, 0).add(offset);

                    level.addParticle(new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1F, 1),
                            randomBody.x, randomBody.y, randomBody.z, offset.x / 10, offset.y / 10, offset.z / 10);
                }
                        livingEntity.setTicksFrozen(0);
                        Vec3 pos = target.position();
                        level.playSound(player, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 0.7F);
            } else {
                target.setTicksFrozen(200 * traitLevel / (traitLevel + 1) + target.getTicksFrozen());
                EffectUtil.entityEffect(level, new FrostParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.6F, 1.0F, 1.0F), 1F, 1), livingEntity, 60);
            }
        }
    }

    @Override
    public int getColor() {
        return 0x80FFE0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.shatterpiercer");
    }

}
