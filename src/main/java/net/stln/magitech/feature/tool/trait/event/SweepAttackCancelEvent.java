package net.stln.magitech.feature.tool.trait.event;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.helper.ComponentHelper;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class SweepAttackCancelEvent {

    @SubscribeEvent
    public static void cancelSweep(SweepAttackEvent event) {

        Player player = event.getEntity();
        ItemStack tool = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (tool.getItem() instanceof SynthesisedToolItem && !ComponentHelper.isBroken(tool)) {
            event.setSweeping(false);
            event.setCanceled(true);
        }
    }
}
