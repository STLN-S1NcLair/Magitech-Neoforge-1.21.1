package net.stln.magitech.content.field_effect.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

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
    public @NotNull FieldInfluenceInstance getCondition() {
        return FieldInfluenceInstance.of(new FieldInfluence(FieldInfluenceInit.HEAT, 1));
    }

    @Override
    public ResourceLocation getIconTexture() {
        return Magitech.id("textures/field_effect/heated.png");
    }

    @Override
    public void affectEntity(@NotNull Entity entity) {
        if (entity instanceof LivingEntity && !entity.fireImmune() && !entity.isOnFire()) {
            entity.setRemainingFireTicks(20);
        }
    }

    @Override
    public @NotNull Color getPrimary() {
        return new Color(0xFFCD44);
    }

    @Override
    public @NotNull Color getSecondary() {
        return new Color(0xFF4400);
    }

}
