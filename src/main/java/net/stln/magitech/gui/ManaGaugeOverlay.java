package net.stln.magitech.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.magic.cooldown.Cooldown;
import net.stln.magitech.magic.cooldown.CooldownData;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;
import net.stln.magitech.util.MathUtil;
import net.stln.magitech.util.RenderHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class ManaGaugeOverlay implements LayeredDraw.Layer {

    private static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/mana_gauge.png");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!Minecraft.getInstance().options.hideGui && !Minecraft.getInstance().player.isSpectator()) {
            int x = guiGraphics.guiWidth() - 64;
            int y = guiGraphics.guiHeight() / 3;
            Player player = Minecraft.getInstance().player;
            if (CuriosApi.getCuriosInventory(player).isPresent()) {
                ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
                if (curiosInventory.getCurios().get("threadbound") != null) {
                    ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
                    double manaRatio = ManaData.getCurrentMana(player, ManaUtil.ManaType.MANA) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);
                    double noctisRatio = ManaData.getCurrentMana(player, ManaUtil.ManaType.NOCTIS) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.NOCTIS);
                    double luminisRatio = ManaData.getCurrentMana(player, ManaUtil.ManaType.LUMINIS) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.LUMINIS);
                    double fluxiaRatio = ManaData.getCurrentMana(player, ManaUtil.ManaType.FLUXIA) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.FLUXIA);
                    int manaGaugeHeight = (int) (manaRatio * 48);
                    int noctisGaugeWidth = (int) (noctisRatio * 32);
                    int luminisGaugeWidth = (int) (luminisRatio * 32);
                    int fluxiaGaugeWidth = (int) (fluxiaRatio * 32);


                    guiGraphics.blit(TEXTURE, x + 56 - noctisGaugeWidth, y + 6, 72, 0, noctisGaugeWidth, 7);
                    guiGraphics.blit(TEXTURE, x + 56 - luminisGaugeWidth, y + 20, 72, 8, luminisGaugeWidth, 7);
                    guiGraphics.blit(TEXTURE, x + 56 - fluxiaGaugeWidth, y + 34, 72, 16, fluxiaGaugeWidth, 7);
                    guiGraphics.blit(TEXTURE, x + 5, y + 64 - manaGaugeHeight, 8, 96, 8, manaGaugeHeight);

                    guiGraphics.blit(TEXTURE, x, y, 0, 0, 64, 91);

                    guiGraphics.blit(TEXTURE, x + 55 - noctisGaugeWidth + 1, y + 3, 145 - noctisGaugeWidth, 0, noctisGaugeWidth + 1, 2);
                    guiGraphics.blit(TEXTURE, x + 55 - luminisGaugeWidth + 1, y + 17, 145 - luminisGaugeWidth, 8, luminisGaugeWidth + 1, 2);
                    guiGraphics.blit(TEXTURE, x + 55 - fluxiaGaugeWidth + 1, y + 31, 145 - fluxiaGaugeWidth, 16, fluxiaGaugeWidth + 1, 2);
                    guiGraphics.blit(TEXTURE, x + 2, y + 64 - manaGaugeHeight, 0, 144 - manaGaugeHeight, 2, manaGaugeHeight);

                    guiGraphics.blit(TEXTURE, x + 53 - noctisGaugeWidth, y + 1, 104, 0, 8, 5);
                    guiGraphics.blit(TEXTURE, x + 53 - luminisGaugeWidth, y + 15, 104, 8, 8, 5);
                    guiGraphics.blit(TEXTURE, x + 53 - fluxiaGaugeWidth, y + 29, 104, 16, 8, 5);

//        if (manaRatio >= 1) {
//            guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 58, guiGraphics.guiHeight() - 54, 8, 88, 12, 11);
//            int frame = Minecraft.getInstance().player != null ? (Minecraft.getInstance().player.tickCount / 5) % 7 : 0;
//            guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 56, guiGraphics.guiHeight() - 62, 0, 128 + 8 * frame, 8, 8);
//        }

                    if (threadbound.has(ComponentInit.SPELL_COMPONENT)) {
                        SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
                        if (spellComponent.selected() < spellComponent.spells().size() && spellComponent.selected() >= 0) {
                            Spell spell = spellComponent.spells().get(spellComponent.selected());
                            ResourceLocation icon = SpellRegister.getId(spell);
                            if (icon != null) {
                                String namespace = icon.getNamespace();
                                String path = icon.getPath();
                                icon = ResourceLocation.fromNamespaceAndPath(namespace, "textures/spell/" + path + ".png");
                                guiGraphics.blit(icon, x + 25, y + 52, 0, 0, 32, 32, 32, 32);

                                ItemStack stack = !(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem) && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof SpellCasterItem ? player.getItemInHand(InteractionHand.OFF_HAND) : player.getItemInHand(InteractionHand.MAIN_HAND);
                                double requiredManaRatio = spell.getRequiredMana(player.level(), player, stack).get(ManaUtil.ManaType.MANA) / ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);
                                if (requiredManaRatio <= 1) {
                                    int requiredManaGaugeHeight = (int) (requiredManaRatio * 48);
                                    guiGraphics.blit(TEXTURE, x, y + 60 - requiredManaGaugeHeight, 0, 160, 16, 8);
                                }

                                Cooldown cooldown = CooldownData.getCurrentCooldown(player, spell);
                                if (cooldown != null) {
                                    int shadeHeight = (int) (32 * (1 - cooldown.getProgress() / cooldown.getCooltime()));
                                    guiGraphics.setColor(0.3F, 0.3F, 0.3F, 1F);
                                    guiGraphics.blit(icon, x + 25, y + 84 - shadeHeight, 0, 32 - shadeHeight, 32, shadeHeight, 32, 32);

                                    guiGraphics.setColor(1F, 1F, 1F, 1F);
                                    guiGraphics.blit(TEXTURE, x + 25, y + 52, 16, 96, 32, 32);
                                    Element element = spell.getElement();
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
                                    } * 16;
                                    guiGraphics.blit(TEXTURE, x + 33, y + 60, 72 + offset, 24, 16, 16);

                                    Font font = Minecraft.getInstance().font;
                                    String text = MathUtil.round((cooldown.getCooltime() - cooldown.getProgress()) / 20, 1) + "s";
                                    int renderx = x + 41 - font.width(text) / 2;
                                    int rendery = y + 76;
                                    RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, element);
                                }
                            }
                        } else {
                            threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spellComponent.spells(), 0));
                        }
                    }

                    guiGraphics.blit(TEXTURE, x - 1, y + 59 - manaGaugeHeight, 0, 144, 6, 10);

                }
            }
        }
    }
}
