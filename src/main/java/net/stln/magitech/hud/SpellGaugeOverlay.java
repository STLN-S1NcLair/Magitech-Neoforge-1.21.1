package net.stln.magitech.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.magic.charge.Charge;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class SpellGaugeOverlay implements LayeredDraw.Layer {

    private static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/spell_gauge.png");
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
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
        }
    }
}
