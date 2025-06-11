package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
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

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModifyBreakSpeedEvent {

    @SubscribeEvent
    public static void modifyBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        Level level = player.level();
        float defaultSpeed = event.getOriginalSpeed();
        Optional<BlockPos> blockPosOptional = event.getPosition();
        BlockState state = event.getState();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof PartToolItem partToolItem) {

            ToolStats stats = ((PartToolItem) stack.getItem()).getSumStats(player, level, stack);
            if (partToolItem.isCorrectTool(stack, state, partToolItem, stats) && blockPosOptional.isPresent()) {
                BlockPos pos = blockPosOptional.get();
                final float[] speed = {stats.getStats().get(ToolStats.MIN_STAT)};
                PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                    speed[0] += trait.modifyMiningSpeed(player, level, stack, integer, stats, state, pos);
                });
                if (partToolItem.getToolType() == ToolType.HAMMER) {
                    speed[0] *= 0.25F;
                }
                event.setNewSpeed(speed[0] * defaultSpeed);
            }
        }
    }
}
