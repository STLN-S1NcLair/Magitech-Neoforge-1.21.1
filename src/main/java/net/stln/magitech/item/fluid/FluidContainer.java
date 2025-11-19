package net.stln.magitech.item.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

/**
 * 液体容器アイテムの情報を保持する
 *
 * @param emptyContainer              取り出したあとの容器
 * @param fillableContainerIngredient 液体を詰められる空の容器の指定(Tag可)
 * @param drainFluid                  取り出すときの液体の指定
 * @param fillableFluidIngredient     詰めるときの液体の指定(Tag可)
 * @param filledContainer             詰めたあとのアイテムの指定
 */
public record FluidContainer(Item emptyContainer, Ingredient fillableContainerIngredient, FluidStack drainFluid,
                             SizedFluidIngredient fillableFluidIngredient, Item filledContainer) {
    public boolean fillingMatches(ItemStack container, FluidStack fluidStack) {
        return fillableContainerIngredient.test(container) && fillableFluidIngredient.test(fluidStack);
    }

    public boolean emptyingMatches(ItemStack container) {
        return container.is(filledContainer);
    }
}
