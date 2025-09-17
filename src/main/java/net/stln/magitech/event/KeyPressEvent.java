package net.stln.magitech.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.ThreadBoundItem;
import net.stln.magitech.item.armor.AetherLifterItem;
import net.stln.magitech.item.armor.FlamglideStriderItem;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.network.DoubleJumpPayload;
import net.stln.magitech.network.LongJumpPayload;
import net.stln.magitech.network.OpenThreadBoundPageScreenPayload;
import net.stln.magitech.network.ThreadBoundSelectPayload;
import net.stln.magitech.util.ClientHelper;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.CuriosHelper;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.PatchouliDataComponents;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public class KeyPressEvent {
    public static int jumpCount = 0;
    public static int onGroundMarker = 0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Player player = ClientHelper.getPlayer();
        if (player == null) return;
        
        while (KeyMappingEvent.TRAIT_ACTION.get().consumeClick()) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof PartToolItem || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof PartToolItem) {
                InteractionHand hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof PartToolItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                ((PartToolItem) player.getItemInHand(hand).getItem()).traitAction(player.level(), player, hand);
            }
        }
        while (KeyMappingEvent.DOUBLE_JUMP.get().consumeClick()) {
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == ItemInit.AETHER_LIFTER.get() && onGroundMarker > 2) {
                PacketDistributor.sendToServer(new DoubleJumpPayload(jumpCount, player.getStringUUID()));
                AetherLifterItem.doubleJump(player, jumpCount, player.getItemBySlot(EquipmentSlot.FEET));
                jumpCount++;
            }
        }
        while (KeyMappingEvent.LONG_JUMP.get().consumeClick()) {
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == ItemInit.FLAMGLIDE_STRIDER.get() && onGroundMarker < 2 && player.isSprinting()) {
                PacketDistributor.sendToServer(new LongJumpPayload(jumpCount, player.getStringUUID()));
                FlamglideStriderItem.longJump(player, jumpCount, player.getItemBySlot(EquipmentSlot.FEET));
                jumpCount++;
            }
        }
        onGroundMarker++;
        if (player.onGround()) {
            jumpCount = 0;
            onGroundMarker = 0;
        }
        while (KeyMappingEvent.SPELL_SHIFT_RIGHT.get().consumeClick()) {
            CuriosHelper.getThreadBoundStack(player).ifPresent(stack -> {
                ComponentHelper.updateSpells(stack, component -> {
                    int selected = component.selected();
                    int select = selected < 0 || selected >= component.spells().size() - 1 ? 0 : selected + 1;

                    PacketDistributor.sendToServer(new ThreadBoundSelectPayload(select, player.getUUID().toString()));
                    return new SpellComponent(component.spells(), select);
                });
            });
        }
        while (KeyMappingEvent.SPELL_SHIFT_LEFT.get().consumeClick()) {
            CuriosHelper.getThreadBoundStack(player).ifPresent(stack -> {
                ComponentHelper.updateSpells(stack, component -> {
                    int selected = component.selected();
                    int select = selected < 1 || selected >= component.spells().size() ? component.spells().size() - 1 : selected - 1;
                    PacketDistributor.sendToServer(new ThreadBoundSelectPayload(select, player.getUUID().toString()));
                    return new SpellComponent(component.spells(), select);
                });
            });
        }
        while (KeyMappingEvent.open_thread_bound_page_screen.get().consumeClick()) {
            PacketDistributor.sendToServer(new OpenThreadBoundPageScreenPayload(player.getUUID()));
        }
        while (KeyMappingEvent.OPEN_SPELLBOUND_AS_GUIDEBOOK.get().consumeClick()) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ThreadBoundItem) {
                Book book = BookRegistry.INSTANCE.books.get(player.getItemInHand(InteractionHand.MAIN_HAND).get(PatchouliDataComponents.BOOK));
                if (book != null) {
                    PatchouliAPI.get().openBookGUI(book.id);
                }
            } else {
                CuriosHelper.getThreadBoundStack(player).ifPresent(stack -> {
                    Book book = BookRegistry.INSTANCE.books.get(stack.get(PatchouliDataComponents.BOOK));
                    if (book != null) {
                        PatchouliAPI.get().openBookGUI(book.id);
                    }
                });
            }
        }
    }
}
