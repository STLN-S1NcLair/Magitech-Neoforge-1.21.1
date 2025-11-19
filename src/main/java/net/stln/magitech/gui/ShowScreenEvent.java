package net.stln.magitech.gui;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.event.KeyMappingEvent;
import net.stln.magitech.gui.overlay.RadialSpellMenuOverlay;
import net.stln.magitech.gui.overlay.ToolBeltOverlay;
import net.stln.magitech.util.ClientHelper;
import net.stln.magitech.util.CuriosHelper;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public class ShowScreenEvent {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        CuriosHelper.getThreadBoundStack(ClientHelper.getPlayer()).ifPresent(stack -> {
            while (KeyMappingEvent.RADIAL_SPELL_MENU.get().consumeClick() && Minecraft.getInstance().screen == null && !stack.isEmpty()) {
                Minecraft.getInstance().setScreen(new RadialSpellMenuOverlay());
            }
        });

        CuriosHelper.getToolBeltStack(ClientHelper.getPlayer()).ifPresent(stack -> {
            while (KeyMappingEvent.TOOL_BELT.get().consumeClick() && Minecraft.getInstance().screen == null) {
                Minecraft.getInstance().setScreen(new ToolBeltOverlay());
            }
        });
    }
}