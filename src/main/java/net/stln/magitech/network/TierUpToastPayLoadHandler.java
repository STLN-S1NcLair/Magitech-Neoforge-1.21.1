package net.stln.magitech.network;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.gui.toast.TierUpToast;

public class TierUpToastPayLoadHandler {

    public static void handleDataOnMainS2C(final TierUpToastPayload payload, final IPayloadContext context) {
        addToast(payload);
    }

    @OnlyIn(Dist.CLIENT)
    private static void addToast(TierUpToastPayload payload) {
        Minecraft.getInstance().getToasts().addToast(new TierUpToast(payload.newTier(), Minecraft.getInstance().player.getInventory().getItem(payload.slot()).copy()));
    }
}
