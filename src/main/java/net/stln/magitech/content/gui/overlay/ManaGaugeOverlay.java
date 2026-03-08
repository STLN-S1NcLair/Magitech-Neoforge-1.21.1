package net.stln.magitech.content.gui.overlay;

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
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.component.SpellComponent;
import net.stln.magitech.content.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.handler.EntityManaHandler;
import net.stln.magitech.core.api.mana.handler.EntityManaHelper;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.cooldown.CooldownData;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.helper.ClientHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.MathHelper;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class ManaGaugeOverlay implements LayeredDraw.Layer {

    private static final ResourceLocation TEXTURE = Magitech.id("textures/gui/mana_gauge.png");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Player player = ClientHelper.getPlayer();
        if (player == null) return;
        double manaRatio = EntityManaHelper.getMagicManaFillRatio(player);
        if (!Minecraft.getInstance().options.hideGui && !player.isSpectator() && !Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen()
                && (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem ||
                player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof SpellCasterItem ||
                manaRatio < 1)) {
            int x = guiGraphics.guiWidth() - 64;
            int y = guiGraphics.guiHeight() / 3;
            if (CuriosApi.getCuriosInventory(player).isPresent()) {
                ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
                if (curiosInventory.getCurios().get("threadbound") != null) {
                    ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
                    int manaGaugeHeight = (int) (manaRatio * 48);

                    guiGraphics.blit(TEXTURE, x + 5, y + 64 - manaGaugeHeight, 8, 96, 8, manaGaugeHeight);
                    guiGraphics.blit(TEXTURE, x, y, 0, 0, 64, 91);
                    guiGraphics.blit(TEXTURE, x + 2, y + 64 - manaGaugeHeight, 0, 144 - manaGaugeHeight, 2, manaGaugeHeight);

//        if (manaRatio >= 1) {
//            guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 58, guiGraphics.guiHeight() - 54, 8, 88, 12, 11);
//            int frame = Minecraft.getInstance().player != null ? (Minecraft.getInstance().player.tickCount / 5) % 7 : 0;
//            guiGraphics.blit(TEXTURE, guiGraphics.guiWidth() - 56, guiGraphics.guiHeight() - 62, 0, 128 + 8 * frame, 8, 8);
//        }

                    SpellComponent spellComponent = ComponentHelper.getSpells(threadbound);
                    if (spellComponent.selected() < spellComponent.spells().size() && spellComponent.selected() >= 0) {
                        ISpell spell = spellComponent.getSelectedSpell();
                        guiGraphics.blit(spell.getIconId(), x + 25, y + 52, 0, 0, 32, 32, 32, 32);

                        ItemStack stack = !(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem) && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof SpellCasterItem ? player.getItemInHand(InteractionHand.OFF_HAND) : player.getItemInHand(InteractionHand.MAIN_HAND);
                        double requiredManaRatio = MagicPerformanceHelper.getEffectiveCost(player, stack, spell) / player.getAttributeValue(AttributeInit.MAX_MANA);
                        if (requiredManaRatio <= 1) {
                            int requiredManaGaugeHeight = (int) (requiredManaRatio * 48);
                            guiGraphics.blit(TEXTURE, x, y + 60 - requiredManaGaugeHeight, 0, 160, 16, 8);
                        }

                        CooldownData data = player.getData(DataAttachmentInit.SPELL_COOLDOWNS);
                        CooldownData.Cooldown cooldown = data.get(spell);
                        if (cooldown != null) {
                            float delta = deltaTracker.getGameTimeDeltaPartialTick(true);
                            int shadeHeight = (int) (32 * (cooldown.remaining() - delta) / cooldown.length());
                            guiGraphics.setColor(0.3F, 0.3F, 0.3F, 1F);
                            guiGraphics.blit(spell.getIconId(), x + 25, y + 84 - shadeHeight, 0, 32 - shadeHeight, 32, shadeHeight, 32, 32);

                            guiGraphics.setColor(1F, 1F, 1F, 1F);
                            guiGraphics.blit(TEXTURE, x + 25, y + 52, 16, 96, 32, 32);
                            Element element = spell.getConfig().element();
                            int offset = switch (element) {
                                case NONE -> 0;
                                case MANA -> 0;
                                case EMBER -> 1;
                                case GLACE -> 2;
                                case SURGE -> 3;
                                case PHANTOM -> 4;
                                case TREMOR -> 5;
                                case MAGIC -> 6;
                                case FLOW -> 7;
                                case HOLLOW -> 8;
                                case LOGOS -> 9;
                            } * 16;
                            guiGraphics.blit(TEXTURE, x + 33, y + 60, 72 + offset, 24, 16, 16);

                            Font font = Minecraft.getInstance().font;
                            String text = MathHelper.round((double) cooldown.remaining() / 20, 1) + "s";
                            int renderx = x + 41 - font.width(text) / 2;
                            int rendery = y + 76;
                            RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, element);
                        }
                    } else {
                        threadbound.set(ComponentInit.SPELL_COMPONENT, spellComponent.setSelected(0));
                    }

                    guiGraphics.blit(TEXTURE, x - 1, y + 59 - manaGaugeHeight, 0, 144, 6, 10);

                }
            }
        }
    }
}
