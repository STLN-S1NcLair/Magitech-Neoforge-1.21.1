package net.stln.magitech.content.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.component.SpellComponent;
import net.stln.magitech.content.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.core.api.mana.handler.EntityManaHelper;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.cooldown.CooldownData;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.helper.ClientHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.MathHelper;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class ManaGaugeOverlay implements LayeredDraw.Layer {

    private static final ResourceLocation TEXTURE = Magitech.id("textures/gui/mana_gauge.png");
    public static final ResourceLocation GAUGE = Magitech.id("mana_reversed");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Player player = ClientHelper.getPlayer();
        if (player == null) return;
        double manaRatio = EntityManaHelper.getMagicManaFillRatio(player);
        if (!Minecraft.getInstance().options.hideGui && !player.isSpectator() && !Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen()
                && (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem ||
                player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof SpellCasterItem ||
                manaRatio < 1)) {
            int x = guiGraphics.guiWidth() - 56;
            int y = guiGraphics.guiHeight() / 3;
            if (CuriosApi.getCuriosInventory(player).isPresent()) {
                ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
                if (curiosInventory.getCurios().get("threadbound") != null) {
                    ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
                    int manaGaugeHeight = (int) (manaRatio * 48);

                    int spellX = x + 19;
                    int spellY = y + 5;

                    // 背景
                    guiGraphics.blit(TEXTURE, x, y, 0, 0, 56, 56);

                    // マナゲージ
                    int offset = 0;
                    int remainingHeight = manaGaugeHeight;
                    PoseStack poseStack = guiGraphics.pose();
                    poseStack.pushPose();
                    for (int i = 0; i < manaGaugeHeight / 16 + 1; i++) {
                        int drawHeight = Math.min(16, remainingHeight);
                        guiGraphics.blitSprite(GAUGE, 16, 16, 16 - drawHeight, 4, x + 3 + 48 - offset - drawHeight, y + 45, drawHeight, 8);
                        remainingHeight -= drawHeight;
                        offset += drawHeight;
                    }
                    poseStack.popPose();

                    SpellComponent spellComponent = ComponentHelper.getSpells(threadbound);
                    if (spellComponent.selected() < spellComponent.spells().size() && spellComponent.selected() >= 0) {
                        ISpell spell = spellComponent.getSelectedSpell();

                        // スペルアイコン
                        guiGraphics.blit(spell.getIconId(), spellX, spellY, 0, 0, 32, 32, 32, 32);

                        ItemStack stack = !(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem) && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof SpellCasterItem ? player.getItemInHand(InteractionHand.OFF_HAND) : player.getItemInHand(InteractionHand.MAIN_HAND);
                        double requiredManaRatio = MagicPerformanceHelper.getEffectiveCost(player, stack, spell) / player.getAttributeValue(AttributeInit.MAX_MANA);
                        if (requiredManaRatio <= 1) {
                            int requiredManaGaugeHeight = (int) (requiredManaRatio * 48);
                            // 必要マナゲージ
                            guiGraphics.blit(TEXTURE, x + 3 + 48 - requiredManaGaugeHeight - 3, y + 45, 0, 64, 4, 10);
                        }

                        CooldownData data = player.getData(DataAttachmentInit.SPELL_COOLDOWNS);
                        CooldownData.Cooldown cooldown = data.get(spell);
                        if (cooldown != null) {
                            float delta = deltaTracker.getGameTimeDeltaPartialTick(true);
                            int shadeHeight = (int) (32 * (cooldown.remaining() - delta) / cooldown.length());
                            // クールダウンのシェード
                            guiGraphics.setColor(0.3F, 0.3F, 0.3F, 1F);
                            guiGraphics.blit(spell.getIconId(), spellX, y + 5 + 32 - shadeHeight, 0, 32 - shadeHeight, 32, shadeHeight, 32, 32);

                            // 鎖
                            guiGraphics.setColor(1F, 1F, 1F, 1F);
                            guiGraphics.blit(TEXTURE, spellX, spellY, 0, 80, 32, 32);
                            Element element = spell.getConfig().element();
                            ResourceLocation cdTex = Magitech.id("textures/gui/spell/" + element.get() + "_spell_cooldown.png");
                            guiGraphics.blit(cdTex, spellX + 8, spellY + 8, 0, 0, 16, 16, 16, 16);

                            Font font = Minecraft.getInstance().font;
                            String text = MathHelper.round((double) cooldown.remaining() / 20, 1) + "s";
                            int renderx = x + 35 - font.width(text) / 2;
                            int rendery = y + 29;
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
