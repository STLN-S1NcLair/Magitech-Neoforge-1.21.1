package net.stln.magitech.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.network.DoubleJumpPayload;
import net.stln.magitech.network.LongJumpPayload;
import net.stln.magitech.network.OpenThreadBoundPageScreenPayload;
import net.stln.magitech.network.ThreadBoundSelectPayload;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
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
        while (KeyMappingEvent.TRAIT_ACTION.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof PartToolItem || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof PartToolItem) {
                InteractionHand hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof PartToolItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                ((PartToolItem) player.getItemInHand(hand).getItem()).traitAction(player.level(), player, hand);
            }
        }
        while (KeyMappingEvent.DOUBLE_JUMP.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == ItemInit.AETHER_LIFTER.get() && onGroundMarker > 2) {
                PacketDistributor.sendToServer(new DoubleJumpPayload(jumpCount, player.getStringUUID()));
                AetherLifterItem.doubleJump(player, jumpCount, player.getItemBySlot(EquipmentSlot.FEET));
                jumpCount++;
            }
        }
        while (KeyMappingEvent.LONG_JUMP.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == ItemInit.FLAMGLIDE_STRIDER.get() && onGroundMarker < 2 && player.isSprinting()) {
                PacketDistributor.sendToServer(new LongJumpPayload(jumpCount, player.getStringUUID()));
                FlamglideStriderItem.longJump(player, jumpCount, player.getItemBySlot(EquipmentSlot.FEET));
                jumpCount++;
            }
        }
        onGroundMarker++;
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.onGround()) {
            jumpCount = 0;
            onGroundMarker = 0;
        }
        while (KeyMappingEvent.SPELL_SHIFT_RIGHT.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;

            ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
            ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
            if (threadbound.has(ComponentInit.SPELL_COMPONENT)) {
                SpellComponent component = threadbound.get(ComponentInit.SPELL_COMPONENT);
                int selected = component.selected();
                int select = selected < 0 || selected >= component.spells().size() - 1 ? 0 : selected + 1;
                PacketDistributor.sendToServer(new ThreadBoundSelectPayload(select, Minecraft.getInstance().player.getUUID().toString()));
                threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(component.spells(), select));
            }
        }
        while (KeyMappingEvent.SPELL_SHIFT_LEFT.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;

            ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
            ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
            if (threadbound.has(ComponentInit.SPELL_COMPONENT)) {
                SpellComponent component = threadbound.get(ComponentInit.SPELL_COMPONENT);
                int selected = component.selected();
                int select = selected < 1 || selected >= component.spells().size() ? component.spells().size() - 1 : selected - 1;
                PacketDistributor.sendToServer(new ThreadBoundSelectPayload(select, Minecraft.getInstance().player.getUUID().toString()));
                threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(component.spells(), select));
            }
        }
        while (KeyMappingEvent.open_thread_bound_page_screen.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;
            PacketDistributor.sendToServer(new OpenThreadBoundPageScreenPayload(player.getUUID().toString()));
        }
        while (KeyMappingEvent.OPEN_SPELLBOUND_AS_GUIDEBOOK.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;

            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ThreadBoundItem) {
                Book book = BookRegistry.INSTANCE.books.get(player.getItemInHand(InteractionHand.MAIN_HAND).get(PatchouliDataComponents.BOOK));
                if (book != null) {
                    PatchouliAPI.get().openBookGUI(book.id);
                }
            } else {
                ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
                ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
                Book book = BookRegistry.INSTANCE.books.get(threadbound.get(PatchouliDataComponents.BOOK));
                if (book != null) {
                    PatchouliAPI.get().openBookGUI(book.id);
                }
            }
        }
    }
}
