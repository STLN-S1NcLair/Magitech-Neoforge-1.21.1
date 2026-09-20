package net.stln.magitech.api.machine.inspection.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.machine.inspection.MachineInspectionRenderer;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MachineInspectionShaderRegistry {
    private MachineInspectionShaderRegistry() {
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        MachineInspectionRenderer.registerShaders(event);
    }
}
