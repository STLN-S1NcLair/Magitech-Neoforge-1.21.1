package net.stln.magitech.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.event.KeyMappingEvent;
import net.stln.magitech.gui.overlay.RadialSpellMenuOverlay;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public class ShowScreenEvent {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Optional<ICuriosItemHandler> inventory = CuriosApi.getCuriosInventory(Minecraft.getInstance().player);
        if (inventory.isPresent()) {
            ICuriosItemHandler curiosInventory = inventory.get();
            ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
            while (KeyMappingEvent.RADIAL_SPELL_MENU.get().consumeClick() && Minecraft.getInstance().screen == null && !threadbound.isEmpty()) {
                Minecraft.getInstance().setScreen(new RadialSpellMenuOverlay(Component.empty()));
            }
        }
    }
}
