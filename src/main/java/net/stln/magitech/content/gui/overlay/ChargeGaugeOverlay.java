package net.stln.magitech.content.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.stln.magitech.Magitech;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.charge.ChargeData;
import net.stln.magitech.helper.ClientHelper;
import net.stln.magitech.helper.MathHelper;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.NotNull;

public class ChargeGaugeOverlay implements LayeredDraw.Layer {

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Player player = ClientHelper.getPlayer();
        if (player == null) return;
        if (!Minecraft.getInstance().options.hideGui) {
            ChargeData data = player.getData(DataAttachmentInit.SPELL_CHARGE);
            ChargeData.Charge charge = data.charge();
            if (data.spell().isPresent() && charge != null) {
                float delta = deltaTracker.getGameTimeDeltaPartialTick(true);
                double chargeProgress = (double) (charge.progress() + delta) / charge.length();
                int chargeGaugeWidth = (int) (chargeProgress * 40);
                Element element = data.spell().get().getConfig().element();
                ResourceLocation texture = Magitech.id("textures/gui/spell/" + element.get() + "_charge_gauge.png");

                guiGraphics.blit(texture, guiGraphics.guiWidth() / 2 - 24, guiGraphics.guiHeight() * 2 / 3 - 12, 0, 0, 48, 23, 88, 24);

                guiGraphics.blit(texture, guiGraphics.guiWidth() / 2 - 20, guiGraphics.guiHeight() * 2 / 3 - 6, 48, 0, chargeGaugeWidth, 11, 88, 24);

                Font font = Minecraft.getInstance().font;
                String text = MathHelper.round((double) (charge.remaining()) / 20, 1) + "s";
                int renderx = guiGraphics.guiWidth() / 2 - font.width(text) / 2;
                int rendery = guiGraphics.guiHeight() * 2 / 3 - 4;
                RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, element);
            }
        }
    }
}
