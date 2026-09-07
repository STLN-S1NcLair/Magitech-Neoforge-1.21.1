package net.stln.magitech.feature.tool.trait.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.feature.tool.trait.TraitHelper;
import net.stln.magitech.feature.tool.trait.TraitInstance;
import net.stln.magitech.helper.ComponentHelper;

import java.util.List;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class DropLootEvent {

    /**
     * ブロックを採掘したとき
     */
    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        Entity breaker = event.getBreaker();

        if (!(breaker instanceof ServerPlayer player)) {
            return;
        }

        ItemStack tool = player.getItemInHand(InteractionHand.MAIN_HAND);
        List<ItemEntity> drops = event.getDrops();
        if (tool.getItem() instanceof SynthesisedToolItem partToolItem && !ComponentHelper.isBroken(tool)) {

            List<TraitInstance> instances = TraitHelper.getTrait(tool);

            instances.forEach(instance -> {
                instance.trait().onBlockLooting(player, player.level(), tool, instance.level(), partToolItem.getAppliedProperties(player, player.level(), tool), event.getState(), event.getPos(), drops);
            });
        }
    }

    /**
     * エンティティを倒したとき
     */
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        Entity killer = event.getSource().getEntity();

        if (!(killer instanceof ServerPlayer player)) {
            return;
        }

        ItemStack tool = player.getItemInHand(InteractionHand.MAIN_HAND);
        List<ItemEntity> drops = event.getDrops().stream().toList();
        if (tool.getItem() instanceof SynthesisedToolItem partToolItem && !ComponentHelper.isBroken(tool)) {

            List<TraitInstance> instances = TraitHelper.getTrait(tool);

            instances.forEach(instance -> {
                instance.trait().onEntityLooting(player, player.level(), tool, instance.level(), partToolItem.getAppliedProperties(player, player.level(), tool), drops);
            });
        }
    }
}
