package net.stln.magitech.item.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.TooltipTextItem;
import net.stln.magitech.util.ComponentHelper;

public class AlchemicalFlaskItem extends TooltipTextItem {

    public AlchemicalFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (ComponentHelper.getFluidContent(itemstack).isEmpty()) {
            BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY); // 液体検知
            if (blockhitresult.getType() == HitResult.Type.MISS) {
                return InteractionResultHolder.pass(itemstack);
            } else {
                if (blockhitresult.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockpos = blockhitresult.getBlockPos();
                    if (!level.mayInteract(player, blockpos)) {
                        return InteractionResultHolder.pass(itemstack);
                    }

                    FluidState fluidState = level.getFluidState(blockpos);
                    if (fluidState.is(FluidTags.WATER)) { // 水なら
                        level.playSound(
                                player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F
                        );
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                        return InteractionResultHolder.sidedSuccess(
                                this.turnBottleIntoItem(itemstack, player, ItemInit.WATER_FLASK.toStack()), level.isClientSide()
                        );
                    } else if (!fluidState.isEmpty()) { // 水以外は液体ブロックから汲むことができない
                        player.displayClientMessage(Component.translatable("item.magitech.hint.cant_pickup_fluid_from_fluid_block").withColor(0xFF8080), true);
                    }
                }

            }
        }
        return InteractionResultHolder.pass(itemstack);
    }

    protected ItemStack turnBottleIntoItem(ItemStack bottleStack, Player player, ItemStack filledBottleStack) {
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(bottleStack, player, filledBottleStack);
    }

}
