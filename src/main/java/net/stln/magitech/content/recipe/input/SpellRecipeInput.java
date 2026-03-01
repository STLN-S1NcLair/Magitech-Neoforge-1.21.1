package net.stln.magitech.content.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.Spell;
import org.jetbrains.annotations.NotNull;

public record SpellRecipeInput(ItemStack item, ISpell spell) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int index) {
        return item;
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty();
    }

    @Override
    public int size() {
        return 0;
    }
}
