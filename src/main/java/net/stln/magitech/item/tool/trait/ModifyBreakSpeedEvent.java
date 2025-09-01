package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.Optional;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class ModifyBreakSpeedEvent {

    @SubscribeEvent
    public static void modifyBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        Level level = player.level();
        float defaultSpeed = event.getOriginalSpeed();
        Optional<BlockPos> blockPosOptional = event.getPosition();
        BlockState state = event.getState();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        Direction direction = PartToolItem.getBreakDirection(player.blockInteractionRange(), blockPosOptional.orElse(BlockPos.ZERO), player);
        if (stack.getItem() instanceof PartToolItem partToolItem) {

            ToolStats stats = ((PartToolItem) stack.getItem()).getSumStats(player, level, stack);
            if (partToolItem.isCorrectTool(stack, state, partToolItem, stats) && blockPosOptional.isPresent()) {
                BlockPos pos = blockPosOptional.get();
                final float[] speed = {stats.getStats().get(ToolStats.MIN_STAT)};
                PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                    speed[0] += trait.modifyMiningSpeed(player, level, stack, integer, stats, state, pos);
                });
                if (partToolItem.getToolType() == ToolType.HAMMER) {
                    speed[0] = getHammerMineSpeed(player, stack, pos, direction, speed[0]);
                }
                event.setNewSpeed(speed[0] * defaultSpeed);
            }
        }
    }

    private static float getHammerMineSpeed(Player player, ItemStack stack, BlockPos pos, Direction direction, float defaultSpeed) {
        int x = 0;
        int y = 0;
        int z = 0;
        float hard = 0;
        int count = 0;
        switch (direction) {
            case UP, DOWN -> {
                x = 1;
                z = 1;
            }
            case NORTH, SOUTH -> {
                x = 1;
                y = 1;
            }
            case EAST, WEST -> {
                y = 1;
                z = 1;
            }
        }
        for (int i = -x; i <= x; i++) {
            for (int j = -y; j <= y; j++) {
                for (int k = -z; k <= z; k++) {
                    if (((PartToolItem) stack.getItem()).isCorrectTool(stack, player.level().getBlockState(pos.offset(i, j, k)), (PartToolItem) stack.getItem(), ((PartToolItem) stack.getItem()).getSumStats(player, player.level(), stack))) {
                        hard += player.level().getBlockState(pos.offset(i, j, k)).getDestroySpeed(player.level(), pos.offset(i, j, k));
                        count++;
                    }
                }
            }
        }
        float centerHard = player.level().getBlockState(pos).getDestroySpeed(player.level(), pos);
        float avgHard = hard / count;
        float speed = defaultSpeed * (centerHard / avgHard) / count;
        return speed;
    }
}
