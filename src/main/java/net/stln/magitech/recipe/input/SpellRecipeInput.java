package net.stln.magitech.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.stln.magitech.magic.spell.Spell;
import org.jetbrains.annotations.NotNull;

public record SpellRecipeInput(ItemStack item, Spell spell) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int index) {
        return item;
    }

    @Override
    public int size() {
        return 0;
    }
}
