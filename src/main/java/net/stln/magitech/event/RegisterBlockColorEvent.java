package net.stln.magitech.event;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.stln.magitech.block.BlockInit;

public class RegisterBlockColorEvent {

//    @SubscribeEvent
//    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
//        event.register((state, world, pos, tintIndex) -> {
//            return world != null && pos != null
//                    ? BiomeColors.getAverageFoliageColor(world, pos)
//                    : FoliageColor.getDefaultColor();
//        }, BlockInit.CELIFERN_LEAVES.get());
//    }
}
