package net.stln.magitech.api.machine.inspection.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public final class MachineInspectionOverlay implements LayeredDraw.Layer {
    public static final MachineInspectionOverlay INSTANCE = new MachineInspectionOverlay();

    private MachineInspectionOverlay() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        MachineInspectionRequestController.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        MachineInspectionHudRenderer.render(guiGraphics, partialTick);
    }
}
