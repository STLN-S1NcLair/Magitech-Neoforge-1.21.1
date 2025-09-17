package net.stln.magitech.item.tool.toolitem;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;

public class ShovelItem extends PartToolItem {

    public ShovelItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.SHOVEL;
    }

    public float getMultiplier(ToolPart part) {
        return switch (part) {
            case TOOL_BINDING -> 0.5F;
            case PLATE -> 1.2F;
            case LIGHT_BLADE -> 1.7F;
            case HEAVY_HANDLE -> 0.6F;
            default -> 1F;
        } / getToolType().getSize();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = context.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.SHOVEL_FLATTEN, false);
            BlockState blockstate2 = null;
            if (blockstate1 != null && level.getBlockState(blockpos.above()).isAir()) {
                level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            } else if ((blockstate2 = blockstate.getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.SHOVEL_DOUSE, false)) != null) {
                if (!level.isClientSide()) {
                    level.levelEvent(null, 1009, blockpos, 0);
                }

            }

            if (blockstate2 != null) {
                if (!level.isClientSide) {
                    level.setBlock(blockpos, blockstate2, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, blockstate2));
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                        progress(player.getItemBySlot(LivingEntity.getSlotForHand(context.getHand())), level, player);
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }
}
