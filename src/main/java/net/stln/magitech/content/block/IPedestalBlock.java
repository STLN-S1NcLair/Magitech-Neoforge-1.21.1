package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.stln.magitech.content.block.block_entity.IPedestalBlockEntity;

public interface IPedestalBlock {

    default ItemInteractionResult interact(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof IPedestalBlockEntity altar) {
            ItemStack altarStack = altar.getInventory().getStackInSlot(0);
            if (altarStack.isEmpty() && stack.isEmpty()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (altarStack.isEmpty()) { // インベントリからアイテムを置く
                altar.getInventory().insertItem(0, stack.copy(), false);
                stack.setCount(0);
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 2f);

            } else if (stack.isEmpty()) { // インベントリにアイテムを入れる
                player.setItemInHand(InteractionHand.MAIN_HAND, altarStack);
                altar.clearContents();
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);

            } else if (stack.getItem() == altarStack.getItem()) { // 同じアイテムなら取る
                player.addItem(altarStack);
                altar.clearContents();
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);

            } else { // 違うアイテムなら交換
                player.setItemInHand(InteractionHand.MAIN_HAND, altarStack.copy());
                altar.clearContents();
                altar.getInventory().insertItem(0, stack.copy(), false);
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }
}
