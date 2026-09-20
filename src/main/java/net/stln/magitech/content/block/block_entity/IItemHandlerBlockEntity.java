package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

public interface IItemHandlerBlockEntity {
    IItemHandler getItemHandler();

    int getInputSlot();

    default int getOutputSlot() {
        return -1;
    }

    default ItemInteractionResult handleCustomItemInteraction(
            ItemStack heldStack,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand
    ) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    default IItemHandler getCapabilityItemHandler() {
        return new RestrictedItemHandler(getItemHandler(), getInputSlot(), getOutputSlot());
    }

    final class RestrictedItemHandler implements IItemHandler {
        private final IItemHandler delegate;
        private final int inputSlot;
        private final int outputSlot;

        private RestrictedItemHandler(IItemHandler delegate, int inputSlot, int outputSlot) {
            this.delegate = delegate;
            this.inputSlot = inputSlot;
            this.outputSlot = outputSlot;
        }

        @Override
        public int getSlots() {
            return delegate.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            ItemStack stack = delegate.getStackInSlot(slot);
            return slot == inputSlot ? stack.copy() : stack;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot != inputSlot) {
                return stack;
            }
            return delegate.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != outputSlot) {
                return ItemStack.EMPTY;
            }
            return delegate.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return delegate.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot == inputSlot && delegate.isItemValid(slot, stack);
        }
    }
}
