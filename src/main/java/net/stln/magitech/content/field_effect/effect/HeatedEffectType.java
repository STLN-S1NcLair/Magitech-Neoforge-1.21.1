package net.stln.magitech.content.field_effect.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;

import java.awt.*;
import java.util.List;

public class HeatedEffectType extends RecipeFieldEffectType<SingleRecipeInput, SmeltingRecipe> {

    @Override
    protected RecipeType<SmeltingRecipe> getRecipeType() {
        return RecipeType.SMELTING;
    }

    @Override
    protected SingleRecipeInput createRecipeInput(ItemStack stack) {
        return new SingleRecipeInput(stack);
    }

    @Override
    public FieldInfluenceInstance getCondition() {
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.HEATED.get(), 1));
    }

    @Override
    public List<ItemStack> processBlock(Level level, BlockPos pos) {
        List<ItemStack> results = processItem(level, List.of(level.getBlockState(pos).getBlock().asItem().getDefaultInstance()));
        ItemStack removed = null;
        boolean replaced = false;
        for (ItemStack result : results) {
            if (!replaced && result.getItem() instanceof BlockItem blockItem) {
                replaced = true;
                level.setBlock(pos, blockItem.getBlock().defaultBlockState(), 3);
                result.shrink(1);
                if (result.isEmpty()) {
                    removed = result;
                }
            }
        }
        if (removed != null) {
            results.remove(removed);
        }
        if (!replaced) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
        return results;
    }

    @Override
    public void affectEntity(Entity entity) {
        if (entity instanceof LivingEntity && !entity.fireImmune() && !entity.isOnFire()) {
            entity.setRemainingFireTicks(20);
        }
    }

    @Override
    public Color getPrimary() {
        return new Color(0xFFCD44);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xFF4400);
    }

}
