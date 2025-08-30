package net.stln.magitech.gui.toast;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;

@OnlyIn(Dist.CLIENT)
public class TierUpToast implements Toast {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/tier_up_toast.png");
    private final int newTier;
    private final ItemStack stack;
    private long lastChanged;
    private boolean changed;

    public TierUpToast(int newTier, ItemStack stack) {
        this.newTier = newTier;
        this.stack = stack;
    }

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible) {
        if (this.changed) {
            this.lastChanged = timeSinceLastVisible;
            this.changed = false;
        }
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        guiGraphics.drawString(toastComponent.getMinecraft().font, Component.translatable("toast.magitech.tier_up.title"), 30, 7, 0xebf7f8, false);
        guiGraphics.drawString(toastComponent.getMinecraft().font, getDescription(newTier), 30, 18, 0xBAEE57, false);
        guiGraphics.pose().pushPose();
        guiGraphics.renderFakeItem(stack, 8, 8);
        guiGraphics.pose().popPose();
        return (double)(timeSinceLastVisible - this.lastChanged) >= 5000.0 * toastComponent.getNotificationDisplayTimeMultiplier()
                ? Toast.Visibility.HIDE
                : Toast.Visibility.SHOW;
    }

    private Component getDescription(int newTier) {
        return Component.translatable("toast.magitech.tier_up.description", newTier - 1, newTier);
    }
}
