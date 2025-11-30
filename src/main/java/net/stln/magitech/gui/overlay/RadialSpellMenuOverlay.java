package net.stln.magitech.gui.overlay;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.event.KeyMappingEvent;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.magic.cooldown.Cooldown;
import net.stln.magitech.magic.cooldown.CooldownData;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.network.ThreadboundSelectPayload;
import net.stln.magitech.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RadialSpellMenuOverlay extends Screen {

    private static final ResourceLocation TEXTURE = Magitech.id("textures/gui/mana_gauge.png");
    float selectAnimTick = 0;
    float selectTick = 0.0F;
    private int select = -1;
    private int ticks = 0;

    public RadialSpellMenuOverlay() {
        super(Component.empty());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!Minecraft.getInstance().options.hideGui) {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight() / 2;

            double dx = mouseX - x;
            double dy = mouseY - y;
            double naturalMouseAngle = MathUtil.getGeneralAngle(Math.atan2(dy, dx) + Math.PI / 2);

            Player player = ClientHelper.getPlayer();
            if (player == null) return;
            SpellComponent spellComponent = CuriosHelper.getThreadBoundStack(player).map(ComponentHelper::getSpells).orElse(SpellComponent.EMPTY);

            int index = 0;
            Spell selectSpell = null;
            for (Spell spell : spellComponent.spells()) {
                int animLength = 3;
                float animTick = Math.min(ticks + partialTicks, animLength);
                double scaledAnimTick = (double) animTick / animLength;
                float radius = (int) (96 - Math.pow(1 - scaledAnimTick, 3) * 48);
                double mouseAngle = naturalMouseAngle;
                double angle = Math.PI * 2 * index / spellComponent.spells().size();
                float sin = (float) (radius * Math.sin(angle));
                float cos = (float) -(radius * Math.cos(angle));
                float size = (float) Math.pow(scaledAnimTick, 2) / 2 + 0.5F;
                double min = MathUtil.getGeneralAngle(Math.PI * 2 * (index - 0.5) / spellComponent.spells().size());
                double max = MathUtil.getGeneralAngle(Math.PI * 2 * (index + 0.5) / spellComponent.spells().size());
                if (index == 0) {
                    min -= Math.PI * 2;
                    if (mouseAngle > max) {
                        mouseAngle -= Math.PI * 2;
                    }
                }
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (mouseAngle >= min && mouseAngle <= max && distance > 10) {
                    if (select != index) {
                        select = index;
                        selectAnimTick = -partialTicks - ticks;
                    }
                    selectTick = selectAnimTick + ticks + partialTicks + 0.01F;
                    size *= (float) (Math.clamp(selectTick / 5, 0.0, 0.5) + 1.0);

                    if (distance > 20) {
                        selectSpell = spell;
                    }
                } else if (distance <= 10) {
                    select = -1;
                }
                guiGraphics.blit(spell.getIconId(), (int) (x + sin - size * 16), (int) (y + cos - size * 16), (int) (size * 32), (int) (size * 32), 0, 0, 32, 32, 32, 32);

                Cooldown cooldown = CooldownData.getCurrentCooldown(player, spell);
                if (cooldown != null) {
                    int shadeHeight = (int) (32 * (1 - cooldown.getProgress() / cooldown.getCooltime()));
                    guiGraphics.setColor(0.3F, 0.3F, 0.3F, 1F);
                    int mulHeight = (int) (size * shadeHeight);
                    guiGraphics.blit(spell.getIconId(), (int) (x + sin - size * 16), (int) (y + cos + (size * 16) - mulHeight), (int) (size * 32), mulHeight, 0, 32 - shadeHeight, 32, shadeHeight, 32, 32);

                    guiGraphics.setColor(1F, 1F, 1F, 1F);
                    guiGraphics.blit(TEXTURE, (int) (x + sin - size * 16), (int) (y + cos - size * 16), (int) (size * 32), (int) (size * 32), 48, 96, 32, 32, 256, 256);
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
                    guiGraphics.blit(TEXTURE, (int) (x + sin - size * 8), (int) (y + cos - size * 8), (int) (size * 16), (int) (size * 16), 72 + offset, 24, 16, 16, 256, 256);

                    Font font = Minecraft.getInstance().font;
                    String text = MathUtil.round((cooldown.getCooltime() - cooldown.getProgress()) / 20, 1) + "s";
                    int renderx = (int) (x + sin - (float) font.width(text) / 2);
                    int rendery = (int) (y + cos + 8 * size);
                    if (animTick == animLength) {
                        RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, element);
                    }
                }
                index++;
            }
            if (selectSpell != null) {
                float squareEase = Math.min(selectTick * selectTick / 4, 4) * 2;
                String text = selectSpell.getDescription().getString();
                List<Component> componentList = selectSpell.getTooltip(player.level(), player, player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem ? player.getItemInHand(InteractionHand.MAIN_HAND) : player.getItemInHand(InteractionHand.OFF_HAND));
                int renderx = (x - font.width(text) / 2);
                int rendery = (int) (y - 4 + 8 - squareEase - componentList.size() * 5);
                RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, selectSpell.getElement());
                int i = 1;
                for (Component component : componentList) {
                    i++;
                    int tooltipx = (x - font.width(component.getString()) / 2);
                    int color = component.getStyle().getColor() != null ? component.getStyle().getColor().getValue() : 0xFFFFFF;
                    RenderHelper.renderFramedText(guiGraphics, font, component.getString(), tooltipx, rendery + i * 10, color, color == selectSpell.getElement().getSpellColor() ? selectSpell.getElement().getSpellDark() : ColorHelper.Argb.mul(color, 0x404060));
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        ticks++;
    }

    @Override
    public void onClose() {
        super.onClose();
        Player player = ClientHelper.getPlayer();
        if (player == null) return;
        CuriosHelper.getThreadBoundStack(player).ifPresent(stack -> {
            if (stack.has(ComponentInit.SPELL_COMPONENT) && select >= 0) {
                PacketDistributor.sendToServer(new ThreadboundSelectPayload(select, player.getUUID()));
                player.releaseUsingItem();
                ComponentHelper.updateSpells(stack, spellComponent -> spellComponent.setSelected(select));
                var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(Magitech.id("animation"));
                if (playerAnimationData != null && playerAnimationData.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimationPlayer) {
                    keyframeAnimationPlayer.stop();
                }
            }
        });
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == KeyMappingEvent.RADIAL_SPELL_MENU.get().getKey().getValue()) {
            this.onClose();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
