package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.sound.SoundInit;
import org.joml.Vector3f;

import java.util.function.Predicate;

public class GeoMendingTrait extends Trait {

    @Override
    public Boolean isCorrectTool(ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState) {
        if (blockState.getTags().anyMatch(Predicate.isEqual(BlockTags.BASE_STONE_OVERWORLD))) {
            return true;
        }
        return super.isCorrectTool(stack, traitLevel, stats, blockState);
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        if (blockState.getTags().anyMatch(Predicate.isEqual(BlockTags.BASE_STONE_OVERWORLD)) && damageAmount > 0 && stack.getDamageValue() < stack.getMaxDamage()) {
            if (player.getRandom().nextFloat() < traitLevel * 0.22F) {
                stack.setDamageValue(stack.getDamageValue() - 1);
                level.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundInit.GEOMENDING_BREAK.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
            }
        }
    }

    @Override
    public boolean emitEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        return blockState.getTags().anyMatch(Predicate.isEqual(BlockTags.BASE_STONE_OVERWORLD)) && damageAmount > 0 && stack.getDamageValue() < stack.getMaxDamage();
    }

    @Override
    public void addEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.addEffect(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new PowerupParticleEffect(new Vector3f(0.5F, 0.5F, 0.5F), new Vector3f(0.5F, 0.5F, 0.5F), 1F, 1, 0),
                    pos.getX() + player.getRandom().nextFloat(), pos.getY() + player.getRandom().nextFloat(), pos.getZ() + player.getRandom().nextFloat(), 0, 0, 0);
        }
        level.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundInit.GEOMENDING_BREAK.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
    }

    @Override
    public int getColor() {
        return 0x808080;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.geomending");
    }
}
