package net.stln.magitech.network;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.gui.toast.TierUpToast;
import net.stln.magitech.util.ClientHelper;

public class TierUpToastPayLoadHandler {

    public static void handleDataOnMainS2C(final TierUpToastPayload payload, final IPayloadContext context) {
        addToast(payload);
    }

    @OnlyIn(Dist.CLIENT)
    private static void addToast(TierUpToastPayload payload) {
        var player = ClientHelper.getPlayer();
        if (player == null) return;
        Minecraft.getInstance().getToasts().addToast(new TierUpToast(payload.newTier(), player.getInventory().getItem(payload.slot()).copy()));
    }
}
