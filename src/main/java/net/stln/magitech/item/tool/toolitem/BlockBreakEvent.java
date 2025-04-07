package net.stln.magitech.item.tool.toolitem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.trait.Trait;
import net.stln.magitech.network.BreakBlockS2CPayload;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class BlockBreakEvent {
    private static final Set<BlockPos> BROKEN_BLOCKS = new HashSet<>();

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        if (BROKEN_BLOCKS.contains(pos)) {
            BROKEN_BLOCKS.remove(pos);
            return;
        }
        ItemStack tool = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        Player player = event.getPlayer();
        if (tool.getItem() instanceof PartToolItem partToolItem && player instanceof ServerPlayer serverPlayer) {

            Map<Trait, Integer> traitMap = PartToolItem.getTraitLevel(PartToolItem.getTraits(tool));
            Set<BlockPos> blockList = new HashSet<>();
            Set<BlockPos> blockList2 = new HashSet<>();
            Set<BlockPos> finalBlockList = new HashSet<>();
            Direction breakDirection = PartToolItem.getBreakDirection(player.blockInteractionRange(), pos, player);
            if (((PartToolItem) tool.getItem()).getToolType().equals(ToolType.HAMMER)) {
                addHammerMine(player, tool, pos, blockList, breakDirection);
            } else {
                blockList.add(pos);
            }
            blockList.forEach(pos1 -> {
                traitMap.forEach((trait, value) -> {
                    blockList2.addAll(trait.addAdditionalBlockBreakFirst(player, event.getPlayer().level(), tool, value, PartToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getLevel().getBlockState(pos1), pos1, 1, breakDirection));
                });
            });
            blockList2.forEach(pos1 -> {
                traitMap.forEach((trait, value) -> {
                    finalBlockList.addAll(trait.addAdditionalBlockBreakSecond(player, event.getPlayer().level(), tool, value, PartToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getLevel().getBlockState(pos1), pos1, 1, breakDirection));
                    BROKEN_BLOCKS.addAll(finalBlockList);
                });
            });
            finalBlockList.forEach(pos1 -> {
                traitMap.forEach((trait, value) -> {
                    if (pos1 != pos) {
                        BreakBlockS2CPayload payload = new BreakBlockS2CPayload(pos1, pos, player.getUUID().toString(), trait.emitEffect(player, event.getPlayer().level(), tool, value, PartToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getState(), pos1, 1, false));
                        PacketDistributor.sendToAllPlayers(payload);
                        trait.onBreakBlock(player, event.getPlayer().level(), tool, value, PartToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getLevel().getBlockState(pos1), pos1, 1, false);

                        serverPlayer.gameMode.destroyBlock(pos1);
                    } else {
                        BreakBlockS2CPayload payload = new BreakBlockS2CPayload(pos1, pos, player.getUUID().toString(), trait.emitEffect(player, event.getPlayer().level(), tool, value, PartToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getState(), pos1, 1, true));
                        PacketDistributor.sendToAllPlayers(payload);
                        trait.onBreakBlock(player, event.getPlayer().level(), tool, value, PartToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getState(), pos1, 1, true);
                    }
                });
            });
        }
    }

    private static void addHammerMine(Player player, ItemStack stack, BlockPos pos, Set<BlockPos> blockPosList, Direction direction) {
        int x = 0;
        int y = 0;
        int z = 0;
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
                    if (PartToolItem.isCorrectTool(stack, player.level().getBlockState(pos.offset(i, j, k)), (PartToolItem) stack.getItem(), PartToolItem.getSumStats(player, player.level(), stack))) {
                        blockPosList.add(pos.offset(i, j, k));
                    }
                }
            }
        }
    }
}
