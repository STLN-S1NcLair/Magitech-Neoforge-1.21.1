package net.stln.magitech.feature.tool.trait;

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
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.BlockHelper;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Set;

public class BirefringenceTrait extends Trait {

    @Override
    public Set<BlockPos> additionalBlockBreak(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        return BlockHelper.getConnectedBlocks(level, pos, blockState.getBlock(), traitLevel);;
    }

    @Override
    public Color getColor() {
        return new Color(0xF0FFFF);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.birefringence");
    }

}
