package net.stln.magitech.network;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.armor.AetherLifterItem;
import net.stln.magitech.item.armor.FlamglideStriderItem;

import java.util.UUID;

public class LongJumpPayLoadHandler {

    public static void handleDataOnMainS2C(final LongJumpPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.getItem() == ItemInit.FLAMGLIDE_STRIDER.get()) {
            FlamglideStriderItem.longJump(player, payload.jumpCount(), boots);
        }
    }

    public static void handleDataOnMainC2S(final LongJumpPayload payload, final IPayloadContext context) {
        Player player = context.player();
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.getItem() == ItemInit.FLAMGLIDE_STRIDER.get()) {
            FlamglideStriderItem.longJump(player, payload.jumpCount(), boots);
        }
    }
}
