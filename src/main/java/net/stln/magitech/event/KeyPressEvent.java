package net.stln.magitech.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ThreadboundItem;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.network.OpenSpellboundPageScreenPayload;
import net.stln.magitech.network.ThreadBoundSelectPayload;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.PatchouliDataComponents;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public class KeyPressEvent {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (KeyMappingEvent.TRAIT_ACTION.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof PartToolItem || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof PartToolItem) {
                InteractionHand hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof PartToolItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                ((PartToolItem) player.getItemInHand(hand).getItem()).traitAction(player.level(), player, hand);
            }
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
        while (KeyMappingEvent.OPEN_SPELLBOUND_PAGE_SCREEN.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;
            PacketDistributor.sendToServer(new OpenSpellboundPageScreenPayload(player.getUUID().toString()));
        }
        while (KeyMappingEvent.OPEN_SPELLBOUND_AS_GUIDEBOOK.get().consumeClick()) {
            Player player = Minecraft.getInstance().player;

            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ThreadboundItem) {
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
