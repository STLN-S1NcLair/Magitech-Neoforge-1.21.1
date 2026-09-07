package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.CrossRefNegativeRationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.CrossRefRationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;

import java.awt.*;
import java.util.List;

public class SeveringTrait extends Trait {

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos) {
        return 1.0F + Math.max(0, 2 - blockState.getDestroySpeed(level, pos)) / 5 * traitLevel;
    }

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        float value = 0.15F * traitLevel;
        ToolPropertyModifier mod = new CrossRefNegativeRationalToolPropertyModifier(ToolPropertyCategory.ATTACK, ToolPropertyCategory.HANDLING, 2.0F, value);
        return List.of(mod);
    }

    @Override
    public Color getColor() {
        return new Color(0x765CAF);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("severing");
    }
}
