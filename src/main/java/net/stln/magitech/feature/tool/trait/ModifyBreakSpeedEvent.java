package net.stln.magitech.feature.tool.trait;

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
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.tool_type.ToolTypeInit;

import java.util.List;
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
        Direction direction = SynthesisedToolItem.getBreakDirection(player.blockInteractionRange(), blockPosOptional.orElse(BlockPos.ZERO), player);
        if (stack.getItem() instanceof SynthesisedToolItem partToolItem) {

            ToolProperties properties = partToolItem.getAppliedProperties(player, level, stack);
            if (partToolItem.isCorrectTool(stack, state, partToolItem, properties) && blockPosOptional.isPresent()) {
                BlockPos pos = blockPosOptional.get();
                final float[] speed = {(float) player.getAttribute(AttributeInit.MINING_SPEED).getValue()};
                TraitHelper.getTrait(stack).forEach((instance) -> {
                    speed[0] *= instance.trait().modifyMiningSpeed(player, level, stack, instance.level(), properties, state, pos);
                });
                if (partToolItem.getToolType() == ToolTypeInit.HAMMER.asToolType()) {
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
        SynthesisedToolItem item = (SynthesisedToolItem) stack.getItem();
        for (int i = -x; i <= x; i++) {
            for (int j = -y; j <= y; j++) {
                for (int k = -z; k <= z; k++) {
                    if ((item).isCorrectTool(stack, player.level().getBlockState(pos.offset(i, j, k)), item, (item).getAppliedProperties(player, player.level(), stack))) {
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
