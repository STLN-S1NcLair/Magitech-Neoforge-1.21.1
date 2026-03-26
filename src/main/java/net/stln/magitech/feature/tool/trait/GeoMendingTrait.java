package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.SingleToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import org.checkerframework.checker.units.qual.C;
import org.joml.Vector3f;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GeoMendingTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        ToolPropertyModifier mod = new RationalToolPropertyModifier(ToolPropertyCategory.DEFENCE, 0.2F * traitLevel);
        return List.of(mod);
    }

    @Override
    public Boolean modifyCorrectTool(ItemStack stack, int traitLevel, BlockState blockState) {
        if (blockState.getTags().anyMatch(Predicate.isEqual(BlockTags.BASE_STONE_OVERWORLD))) {
            return true;
        }
        return super.modifyCorrectTool(stack, traitLevel, blockState);
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, properties, blockState, pos, damageAmount, isInitial);
        if (blockState.getTags().anyMatch(Predicate.isEqual(BlockTags.BASE_STONE_OVERWORLD)) && damageAmount > 0 && stack.getDamageValue() < stack.getMaxDamage()) {
            if (player.getRandom().nextFloat() < traitLevel * 0.2F) {
                stack.setDamageValue(stack.getDamageValue() - 1);
                level.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundInit.GEOMENDING_BREAK.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
            }
        }
    }

    @Override
    public Color getColor() {
        return new Color(0x808080);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.geomending");
    }
}
