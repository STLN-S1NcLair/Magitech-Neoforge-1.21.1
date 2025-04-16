package net.stln.magitech.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL12;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class ManaGaugeOverlay implements LayeredDraw.Layer {

    private static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/mana_gauge.png");
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
        ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
        double manaRatio = ManaData.getCurrentMana(player, ManaUtil.ManaType.MANA) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);
        double noctisRatio = ManaData.getCurrentMana(player, ManaUtil.ManaType.NOCTIS) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.NOCTIS);
        double luminisRatio = ManaData.getCurrentMana(player, ManaUtil.ManaType.LUMINIS) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.LUMINIS);
        double fluxiaRatio = ManaData.getCurrentMana(player, ManaUtil.ManaType.FLUXIA) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.FLUXIA);
        int manaGaugeHeight = (int) (manaRatio * 40);
        int noctisGaugeWidth = (int) (noctisRatio * 23);
        int luminisGaugeWidth = (int) (luminisRatio * 23);
        int fluxiaGaugeWidth = (int) (fluxiaRatio * 23);


        guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 9 - noctisGaugeWidth, guiGraphics.guiHeight() - 74, 64, 0, noctisGaugeWidth, 5);
        guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 9 - luminisGaugeWidth, guiGraphics.guiHeight() - 66, 64, 8, luminisGaugeWidth, 5);
        guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 9 - fluxiaGaugeWidth, guiGraphics.guiHeight() - 58, 64, 16, fluxiaGaugeWidth, 5);

        guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 60, guiGraphics.guiHeight() - 84, 0, 0, 60, 84);

        if (manaRatio >= 1) {
            guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 58, guiGraphics.guiHeight() - 54, 8, 88, 12, 11);
            int frame = Minecraft.getInstance().player != null ? (Minecraft.getInstance().player.tickCount / 5) % 7 : 0;
            guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 56, guiGraphics.guiHeight() - 62, 0, 128 + 8 * frame, 8, 8);
        }
        guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 56, guiGraphics.guiHeight() - 3 - manaGaugeHeight, 0, 88, 8, manaGaugeHeight);
        guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 58, guiGraphics.guiHeight() - 43, 8, 104, 12, 41);

        if (threadbound.has(ComponentInit.SPELL_COMPONENT)) {
            SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
            if (spellComponent.selected() < spellComponent.spells().size()) {
                Spell spell = spellComponent.spells().get(spellComponent.selected());
                ResourceLocation icon = SpellRegister.getId(spell);
                if (icon != null) {
                    String namespace = icon.getNamespace();
                    String path = icon.getPath();
                    icon = ResourceLocation.fromNamespaceAndPath(namespace, "textures/spell/" + path + ".png");
                    guiGraphics.blit(icon, guiGraphics.guiWidth() - 40, guiGraphics.guiHeight() - 40, 0, 0, 32, 32, 32, 32);
                }
            } else {
                threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spellComponent.spells(), 0));
            }
        }
    }
}
