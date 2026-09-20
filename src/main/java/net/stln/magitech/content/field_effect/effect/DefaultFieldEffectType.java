package net.stln.magitech.content.field_effect.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.content.recipe.FieldEffectRecipe;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.effect.visual.preset.BlockVFX;

import java.util.List;

public abstract class DefaultFieldEffectType extends FieldEffectType {

    @Override
    public boolean canProcess(Level level, List<ItemStack> inputs) {
        return inputs.stream().anyMatch(input -> FieldEffectRecipe.findItemRecipe(level, this, input).isPresent());
    }

    @Override
    public boolean canProcess(Level level, BlockPos pos) {
        return FieldEffectRecipe.findBlockRecipe(level, pos, this).isPresent();
    }

    @Override
    public List<ItemStack> processItem(Level level, List<ItemStack> inputs) {
        return FieldEffectRecipe.processItem(level, this, inputs);
    }

    @Override
    public List<ItemStack> processBlock(Level level, BlockPos pos) {
        return FieldEffectRecipe.findBlockRecipe(level, pos, this)
                .map(recipe -> FieldEffectRecipe.processBlock(level, pos, recipe))
                .orElseGet(List::of);
    }

    @Override
    public FieldInfluenceInstance getCondition() {
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.COOLING.get(), 1));
    }

    @Override
    public void renderVFX(Level level, BlockPos pos) {
        BlockVFX.fieldEffect(level, getPrimary(), getSecondary(), pos, 0.02F);
    }
}
