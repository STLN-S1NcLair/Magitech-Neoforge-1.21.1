package net.stln.magitech.hud;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OverlayInit {

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mana_gauge"), new ManaGaugeOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "spell_gauge"), new SpellGaugeOverlay());
    }
}
