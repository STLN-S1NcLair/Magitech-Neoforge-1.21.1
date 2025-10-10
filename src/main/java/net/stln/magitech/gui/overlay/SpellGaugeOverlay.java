package net.stln.magitech.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.magic.charge.Charge;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.util.ClientHelper;
import net.stln.magitech.util.MathUtil;
import net.stln.magitech.util.RenderHelper;
import org.jetbrains.annotations.NotNull;

public class SpellGaugeOverlay implements LayeredDraw.Layer {

    private static final ResourceLocation TEXTURE = Magitech.id("textures/gui/spell_gauge.png");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Player player = ClientHelper.getPlayer();
        if (player == null) return;
        if (!Minecraft.getInstance().options.hideGui) {
            Charge charge = ChargeData.getCurrentCharge(player);
            if (charge != null) {
                double chargeProgress = charge.getCharge() / charge.getMaxCharge();
                int chargeGaugeWidth = (int) (chargeProgress * 40);
                Element element = charge.getElement();
                int offset = switch (element) {
                    case NONE -> 0;
                    case EMBER -> 1;
                    case GLACE -> 2;
                    case SURGE -> 3;
                    case PHANTOM -> 4;
                    case TREMOR -> 5;
                    case MAGIC -> 6;
                    case FLOW -> 7;
                    case HOLLOW -> 8;
                } * 24;

                guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() / 2 - 24, guiGraphics.guiHeight() * 2 / 3 - 12, 0, offset, 48, 23);

                guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() / 2 - 20, guiGraphics.guiHeight() * 2 / 3 - 6, 48, offset, chargeGaugeWidth, 11);

                Font font = Minecraft.getInstance().font;
                String text = MathUtil.round((charge.getMaxCharge() - charge.getCharge()) / 20, 1) + "s";
                int renderx = guiGraphics.guiWidth() / 2 - font.width(text) / 2;
                int rendery = guiGraphics.guiHeight() * 2 / 3 - 4;
                RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, element);
            }
        }
    }
}
