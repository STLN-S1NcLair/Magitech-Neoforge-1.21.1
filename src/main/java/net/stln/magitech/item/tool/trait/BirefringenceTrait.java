package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.BlockUtil;
import org.joml.Vector3f;

import java.util.Set;

public class BirefringenceTrait extends Trait {

    @Override
    public Set<BlockPos> addAdditionalBlockBreakSecond(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
            return BlockUtil.getConnectedBlocks(level, pos, blockState.getBlock(), traitLevel);
    }

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, stats, target);
        if (target instanceof LivingEntity livingEntity && livingEntity.getHealth() < livingEntity.getMaxHealth() / 2) {
            livingEntity.invulnerableTime = 0;
            DamageSource source = player.damageSources().playerAttack(player);
            livingEntity.hurt(source, traitLevel);
        }
    }

    @Override
    public boolean emitEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        return !isInitial;
    }

    @Override
    public void addEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.addEffect(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new PowerupParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(0.9F, 1.0F, 1.0F), 1F, 1, 0),
                    pos.getX() + player.getRandom().nextFloat(), pos.getY() + player.getRandom().nextFloat(), pos.getZ() + player.getRandom().nextFloat(), 0, 0, 0);
        }
    }

    @Override
    public int getColor() {
        return 0xF0FFFF;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.birefringence");
    }

}
