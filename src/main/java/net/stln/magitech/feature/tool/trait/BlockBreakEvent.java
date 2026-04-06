package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
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
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.content.network.BreakBlockPayload;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.tool_type.ToolTypeInit;
import net.stln.magitech.helper.BlockHelper;
import net.stln.magitech.helper.ComponentHelper;

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
        BlockState state = event.getState();
        ItemStack tool = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        Player player = event.getPlayer();
        if (tool.getItem() instanceof SynthesisedToolItem partToolItem && !ComponentHelper.isBroken(tool)) {

            List<TraitInstance> instances = TraitHelper.getTrait(tool);
            Set<BlockPos> finalBlockList = new HashSet<>();
            Direction breakDirection = SynthesisedToolItem.getBreakDirection(player.blockInteractionRange(), pos, player);
            ToolType toolType = partToolItem.getToolType();
            Set<BlockPos> blockList = new HashSet<>(toolType.additionalMine().apply(player, tool, pos, breakDirection));
            blockList.add(pos);
            finalBlockList.addAll(blockList);

            blockList.forEach(pos1 -> instances.forEach((instance) ->
                    finalBlockList.addAll(instance.trait().additionalBlockBreak(player, event.getPlayer().level(), tool, instance.level(),
                            partToolItem.getAppliedProperties(player, event.getPlayer().level(), tool), event.getLevel().getBlockState(pos1), pos1, 1, breakDirection))));
            BROKEN_BLOCKS.addAll(finalBlockList);
            if (finalBlockList.size() < 2) {
                BROKEN_BLOCKS.removeAll(finalBlockList);
            }
            finalBlockList.forEach(pos1 -> {
                final boolean[] flag = {true};
                instances.forEach((instance) -> {
                    if (pos1 != pos) {
                        BreakBlockPayload payload = new BreakBlockPayload(pos1, pos, player.getUUID(), flag[0]);
                        PacketDistributor.sendToAllPlayers(payload);
                        if (flag[0]) {
                            instance.trait().onBreakBlock(player, event.getPlayer().level(), tool, instance.level(), partToolItem.getAppliedProperties(player, event.getPlayer().level(), tool), event.getLevel().getBlockState(pos1), pos1, 1, false);

                            if (player instanceof ServerPlayer serverPlayer) {
                                serverPlayer.gameMode.destroyBlock(pos1);
                            }
                            flag[0] = false;
                        }
                    } else {
                        BreakBlockPayload payload = new BreakBlockPayload(pos1, pos, player.getUUID(), flag[0]);
                        PacketDistributor.sendToAllPlayers(payload);
                        if (flag[0]) {
                            instance.trait().onBreakBlock(player, event.getPlayer().level(), tool, instance.level(), partToolItem.getAppliedProperties(player, event.getPlayer().level(), tool), state, pos1, 1, true);
                            flag[0] = false;
                        }
                    }
                });
            });
        }
    }

    @SubscribeEvent
    public static void blockLooting(BlockDropsEvent event) {
        ItemStack tool = event.getTool().copy();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        List<ItemEntity> drops = event.getDrops();
        Entity entity = event.getBreaker();
        List<TraitInstance> instances = TraitHelper.getTrait(tool);
        if (entity instanceof Player player && tool.getItem() instanceof SynthesisedToolItem partToolItem && !ComponentHelper.isBroken(tool)) {
            AtomicReference<ArrayList<ItemStack>> lootStack = new AtomicReference<>(new ArrayList<>());
            AtomicReference<ArrayList<ItemStack>> setLootStack = new AtomicReference<>(new ArrayList<>());
            lootStack.set(drops.stream().map(ItemEntity::getItem).collect(Collectors.toCollection(ArrayList::new)));
            instances.forEach((instance) -> instance.trait().modifyEnchantmentOnBlockLooting(player, player.level(), tool, instance.level(), partToolItem.getAppliedProperties(player, player.level(), tool), state, pos, lootStack.get()));
            final double[] expMul = {1.0};
            instances.forEach((instance) -> expMul[0] *= instance.trait().modifyExpOnBlockLooting(player, player.level(), tool, instance.level(), partToolItem.getAppliedProperties(player, player.level(), tool), state, pos, lootStack.get(), event.getDroppedExperience()));
            event.setDroppedExperience((int) (event.getDroppedExperience() * expMul[0]));

            LootParams.Builder builder = new LootParams.Builder((ServerLevel) entity.level())
                    .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                    .withParameter(LootContextParams.TOOL, tool)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, event.getBlockEntity())
                    .withParameter(LootContextParams.THIS_ENTITY, player);

            lootStack.set(new ArrayList<>(state.getDrops(builder)));

            instances.forEach((instance) -> lootStack.get().addAll(instance.trait().addItemOnBlockLooting(player, player.level(), tool, instance.level(), partToolItem.getAppliedProperties(player, player.level(), tool), state, pos, lootStack.get())));
            instances.forEach((instance) -> setLootStack.set(new ArrayList<>(instance.trait().setItemOnBlockLooting(player, player.level(), tool, instance.level(), partToolItem.getAppliedProperties(player, player.level(), tool), state, pos, lootStack.get()))));
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
