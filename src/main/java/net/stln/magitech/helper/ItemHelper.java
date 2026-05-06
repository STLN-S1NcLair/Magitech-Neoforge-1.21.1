package net.stln.magitech.helper;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemHelper {

    public static ItemStack extractStack(IItemHandler handler, int maxAmount, boolean simulate) {
        return extractStack(ItemStack.EMPTY, handler, maxAmount, simulate);
    }

    public static ItemStack extractStack(ItemStack defaultStack, IItemHandler handler, int maxAmount, boolean simulate) {
        int leftCount = maxAmount - defaultStack.getCount();
        ItemStack stack = defaultStack.copy();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack hanslerStack = handler.getStackInSlot(i);
            ItemStack stackInSlot = hanslerStack.copy();
            int grew = 0;
            if (stack.isEmpty()) {
                stack = stackInSlot.split(leftCount);
                grew = stack.getCount();
                leftCount = maxAmount - stack.getCount();
            } else {
                if (ItemStack.isSameItemSameComponents(stack, stackInSlot)) {
                    ItemStack split = stackInSlot.split(leftCount);
                    grew = split.getCount();
                    stack.grow(split.getCount());
                    leftCount = maxAmount - stack.getCount();
                }
            }
            if (!simulate) {
                hanslerStack.shrink(grew);
                if (hanslerStack.isEmpty()) {
                    hanslerStack = ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }
}
