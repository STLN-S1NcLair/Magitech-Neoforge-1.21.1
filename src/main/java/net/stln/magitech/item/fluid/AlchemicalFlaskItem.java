package net.stln.magitech.item.fluid;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.TooltipTextItem;
import net.stln.magitech.util.ComponentHelper;

import java.util.List;

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

                    if (level.getFluidState(blockpos).is(FluidTags.WATER)) { // 水なら
                        level.playSound(
                                player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F
                        );
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                        return InteractionResultHolder.sidedSuccess(
                                this.turnBottleIntoItem(itemstack, player, ItemInit.WATER_FLASK.toStack()), level.isClientSide()
                        );
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
