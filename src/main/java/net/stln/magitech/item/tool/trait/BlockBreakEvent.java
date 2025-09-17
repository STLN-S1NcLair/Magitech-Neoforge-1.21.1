package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.network.BreakBlockPayload;
import net.stln.magitech.util.ComponentHelper;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class BlockBreakEvent {
    private static final Set<BlockPos> BROKEN_BLOCKS = new HashSet<>();

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        if (BROKEN_BLOCKS.contains(pos)) {
            BROKEN_BLOCKS.remove(pos);
            return;
        }
        ItemStack tool = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND).copy();
        Player player = event.getPlayer();
        if (tool.getItem() instanceof PartToolItem partToolItem && player instanceof ServerPlayer serverPlayer && !ComponentHelper.isBroken(tool)) {

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
            blockList.forEach(pos1 -> traitMap.forEach((trait, value) -> blockList2.addAll(trait.addAdditionalBlockBreakFirst(player, event.getPlayer().level(), tool, value, partToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getLevel().getBlockState(pos1), pos1, 1, breakDirection))));
            blockList2.forEach(pos1 -> traitMap.forEach((trait, value) -> {
                if (!event.getLevel().getBlockState(pos1).getBlock().equals(Blocks.AIR)) {
                    finalBlockList.addAll(trait.addAdditionalBlockBreakSecond(player, event.getPlayer().level(), tool, value, partToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getLevel().getBlockState(pos1), pos1, 1, breakDirection));
                }
            }));
            BROKEN_BLOCKS.addAll(finalBlockList);
            if (finalBlockList.size() < 2) {
                BROKEN_BLOCKS.removeAll(finalBlockList);
            }
            finalBlockList.forEach(pos1 -> {
                final boolean[] flag = {true};
                traitMap.forEach((trait, value) -> {
                    if (pos1 != pos) {
                        BreakBlockPayload payload = new BreakBlockPayload(pos1, pos, player.getUUID(), trait.emitEffect(player, event.getPlayer().level(), tool, value, partToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getState(), pos1, 1, false), flag[0]);
                        PacketDistributor.sendToAllPlayers(payload);
                        if (flag[0]) {
                            trait.onBreakBlock(player, event.getPlayer().level(), tool, value, partToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getLevel().getBlockState(pos1), pos1, 1, false);

                            serverPlayer.gameMode.destroyBlock(pos1);
                            flag[0] = false;
                        }
                    } else {
                        BreakBlockPayload payload = new BreakBlockPayload(pos1, pos, player.getUUID(), trait.emitEffect(player, event.getPlayer().level(), tool, value, partToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getState(), pos1, 1, true), flag[0]);
                        PacketDistributor.sendToAllPlayers(payload);
                        if (flag[0]) {
                            trait.onBreakBlock(player, event.getPlayer().level(), tool, value, partToolItem.getSumStats(player, event.getPlayer().level(), tool), event.getState(), pos1, 1, true);
                            flag[0] = false;
                        }
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
                    if (((PartToolItem) stack.getItem()).isCorrectTool(stack, player.level().getBlockState(pos.offset(i, j, k)), (PartToolItem) stack.getItem(), ((PartToolItem) stack.getItem()).getSumStats(player, player.level(), stack))) {
                        blockPosList.add(pos.offset(i, j, k));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void blockLooting(BlockDropsEvent event) {
        ItemStack tool = event.getTool().copy();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        List<ItemEntity> drops = event.getDrops();
        Entity entity = event.getBreaker();
        Map<Trait, Integer> traitMap = PartToolItem.getTraitLevel(PartToolItem.getTraits(tool));
        if (entity instanceof Player player && tool.getItem() instanceof PartToolItem partToolItem && !ComponentHelper.isBroken(tool)) {
            AtomicReference<List<ItemStack>> lootStack = new AtomicReference<>(new ArrayList<>());
            AtomicReference<List<ItemStack>> setLootStack = new AtomicReference<>(new ArrayList<>());
            lootStack.set(drops.stream().map(ItemEntity::getItem).collect(Collectors.toCollection(ArrayList::new)));
            traitMap.forEach((trait, value) -> trait.modifyEnchantmentOnBlockLooting(player, player.level(), tool, value, partToolItem.getSumStats(player, player.level(), tool), state, pos, lootStack.get()));
            final double[] expMul = {1.0};
            traitMap.forEach((trait, value) -> expMul[0] *= trait.modifyExpOnBlockLooting(player, player.level(), tool, value, partToolItem.getSumStats(player, player.level(), tool), state, pos, lootStack.get(), event.getDroppedExperience()));
            event.setDroppedExperience((int) (event.getDroppedExperience() * expMul[0]));

            LootParams.Builder builder = new LootParams.Builder((ServerLevel) entity.level())
                    .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                    .withParameter(LootContextParams.TOOL, tool)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, event.getBlockEntity())
                    .withParameter(LootContextParams.THIS_ENTITY, player);

            lootStack.set(state.getDrops(builder));

            traitMap.forEach((trait, value) -> lootStack.get().addAll(trait.addItemOnBlockLooting(player, player.level(), tool, value, partToolItem.getSumStats(player, player.level(), tool), state, pos, lootStack.get())));
            traitMap.forEach((trait, value) -> setLootStack.set(trait.setItemOnBlockLooting(player, player.level(), tool, value, partToolItem.getSumStats(player, player.level(), tool), state, pos, lootStack.get())));
            Vec3 center = pos.getCenter();
            if (setLootStack.get() != null && !setLootStack.get().isEmpty()) {
                event.getDrops().clear();
                for (ItemStack itemStack : setLootStack.get()) {
                    if (!itemStack.isEmpty()) {
                        ItemEntity itemEntity = new ItemEntity(event.getLevel(), center.x, center.y, center.z, itemStack);
                        itemEntity.setPickUpDelay(10);
                        event.getDrops().add(itemEntity);
                    }
                }
            } else {
                event.getDrops().clear();
                for (ItemStack itemStack : lootStack.get()) {
                    if (!itemStack.isEmpty()) {
                        ItemEntity itemEntity = new ItemEntity(event.getLevel(), center.x, center.y, center.z, itemStack);
                        itemEntity.setPickUpDelay(10);
                        event.getDrops().add(itemEntity);
                    }
                }
            }
        }
    }


}
