package net.stln.magitech.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.stln.magitech.content.block.block_entity.IItemHandlerBlockEntity;

public final class MachineInteractionHelper {
    private MachineInteractionHelper() {
    }

    public static ItemInteractionResult interact(
            ItemStack heldStack,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            IItemHandlerBlockEntity blockEntity
    ) {
        ItemInteractionResult customResult = blockEntity.handleCustomItemInteraction(heldStack, level, pos, player, hand);
        if (customResult != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) {
            return customResult;
        }
        return interact(
                heldStack,
                level,
                pos,
                player,
                hand,
                blockEntity.getItemHandler(),
                blockEntity.getInputSlot(),
                blockEntity.getOutputSlot()
        );
    }

    private static ItemInteractionResult interact(
            ItemStack heldStack,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            IItemHandler inventory,
            int inputSlot,
            int outputSlot
    ) {
        ItemStack input = inventory.getStackInSlot(inputSlot);
        ItemStack output = outputSlot >= 0 ? inventory.getStackInSlot(outputSlot) : ItemStack.EMPTY;

        if (heldStack.isEmpty() && input.isEmpty() && output.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (heldStack.isEmpty()) {
            if (player.isCrouching()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            collectContents(inventory, inputSlot, outputSlot, player, hand);
            playPickupSound(level, player, pos, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (input.isEmpty()) {
            ItemStack remainder = inventory.insertItem(inputSlot, heldStack.copy(), false);
            if (remainder.getCount() == heldStack.getCount()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            heldStack.setCount(remainder.getCount());
            playPickupSound(level, player, pos, 2.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (ItemStack.isSameItemSameComponents(heldStack, input)) {
            giveOrDrop(player, inventory.extractItem(inputSlot, input.getCount(), false));
            collectOutput(inventory, outputSlot, player);
            playPickupSound(level, player, pos, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (!inventory.isItemValid(inputSlot, heldStack)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        ItemStack removedInput = inventory.extractItem(inputSlot, input.getCount(), false);
        ItemStack remainder = inventory.insertItem(inputSlot, heldStack.copy(), false);
        if (!remainder.isEmpty()) {
            inventory.insertItem(inputSlot, removedInput, false);
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        player.setItemInHand(hand, removedInput);
        collectOutput(inventory, outputSlot, player);
        playPickupSound(level, player, pos, 1.0F);
        return ItemInteractionResult.SUCCESS;
    }

    private static void collectContents(IItemHandler inventory, int inputSlot, int outputSlot, Player player, InteractionHand hand) {
        ItemStack input = inventory.extractItem(inputSlot, inventory.getStackInSlot(inputSlot).getCount(), false);
        ItemStack output = extractOutput(inventory, outputSlot);
        if (!input.isEmpty()) {
            player.setItemInHand(hand, input);
            giveOrDrop(player, output);
        } else {
            player.setItemInHand(hand, output);
        }
    }

    private static void collectOutput(IItemHandler inventory, int outputSlot, Player player) {
        giveOrDrop(player, extractOutput(inventory, outputSlot));
    }

    private static ItemStack extractOutput(IItemHandler inventory, int outputSlot) {
        if (outputSlot < 0) {
            return ItemStack.EMPTY;
        }
        return inventory.extractItem(outputSlot, inventory.getStackInSlot(outputSlot).getCount(), false);
    }

    private static void giveOrDrop(Player player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        if (!player.addItem(stack) && !player.level().isClientSide) {
            player.drop(stack, false);
        }
    }

    public static void playPickupSound(Level level, Player player, BlockPos pos, float pitch) {
        // 音がいまいちなので暫定無音
//        level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, pitch);
    }
}
