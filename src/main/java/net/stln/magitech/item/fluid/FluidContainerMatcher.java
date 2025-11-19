package net.stln.magitech.item.fluid;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.function.Supplier;

/**
 * 液体容器アイテムの情報を保持する
 *
 * @param emptyContainer      取り出したあとの容器
 * @param containerIngredient 液体を詰められる空の容器の指定(Tag可)
 * @param fluid               取り出すときの液体の指定
 * @param fluidIngredient     詰めるときの液体の指定(Tag可)
 * @param filledContainer     詰めたあとのアイテムの指定
 */
public record FluidContainerMatcher(ItemLike emptyContainer, Supplier<Ingredient> containerIngredient,
                                    Supplier<Fluid> fluid, Supplier<FluidIngredient> fluidIngredient,
                                    ItemLike filledContainer) {

    public boolean fillingMatches(ItemStack container, FluidStack fluidStack) {
        return containerIngredient.get().test(container) && fluidIngredient.get().test(fluidStack);
    }

    public boolean emptyingMatches(ItemStack container) {
        return container.is(filledContainer.asItem());
    }
}
