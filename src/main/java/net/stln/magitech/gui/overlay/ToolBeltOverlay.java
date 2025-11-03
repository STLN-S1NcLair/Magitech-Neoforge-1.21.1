package net.stln.magitech.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.event.KeyMappingEvent;
import net.stln.magitech.item.ItemTagKeys;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.ToolBeltComponent;
import net.stln.magitech.network.SwapToolFromBeltPayLoadHandler;
import net.stln.magitech.network.SwapToolFromBeltPayload;
import net.stln.magitech.network.ThreadboundSelectPayload;
import net.stln.magitech.util.*;
import org.jetbrains.annotations.NotNull;

public class ToolBeltOverlay extends Screen {
    private static final ResourceLocation TEXTURE = Magitech.id("textures/gui/tool_belt.png");
    private int ticks = 0;
    private int select = -1;
    static int ANIM_LENGTH = 10;

    public ToolBeltOverlay() {
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
            ToolBeltComponent toolBeltComponent = CuriosHelper.getToolBeltStack(player).map(ComponentHelper::getToolsInBelt).orElse(ToolBeltComponent.EMPTY);

            // ベルト部表示
            guiGraphics.blit(TEXTURE, x - 51, y - 51, 0, 0, 102, 102);

            // ツール部表示
            for (int i = 0; i < 8; i++) {
                float animTick = Math.min(ticks + partialTicks, ANIM_LENGTH);
                double scaledAnimTick = (double) animTick / ANIM_LENGTH;
                float radius = (int) (51 - Math.pow(1 - scaledAnimTick, 3) * 25);
                double mouseAngle = naturalMouseAngle;
                double angle = Math.PI * 2 * i / 8;
                float sin = (float) (radius * Math.sin(angle));
                float cos = (float) -(radius * Math.cos(angle));
                double min = MathUtil.getGeneralAngle(Math.PI * 2 * (i - 0.5) / 8);
                double max = MathUtil.getGeneralAngle(Math.PI * 2 * (i + 0.5) / 8);
                if (i == 0) {
                    min -= Math.PI * 2;
                    if (mouseAngle > max) {
                        mouseAngle -= Math.PI * 2;
                    }
                }
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (mouseAngle >= min && mouseAngle <= max && distance > 10) {
                    if (select != i) {
                        select = i;
                    }

                    // 表示部分
//                    String text = spell.getDescription().getString();
//                    List<Component> componentList = spell.getTooltip(player.level(), player, player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem ? player.getItemInHand(InteractionHand.MAIN_HAND) : player.getItemInHand(InteractionHand.OFF_HAND));
//                    int renderx = (x - font.width(text) / 2);
//                    int rendery = (int) (y - 4 + 8 - componentList.size() * 4);
//                    RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, spell.getElement());
//
//                    int tooltipx = (x - font.width(component.getString()) / 2);
//                    int color = component.getStyle().getColor() != null ? component.getStyle().getColor().getValue() : 0xFFFFFF;
//                    RenderHelper.renderFramedText(guiGraphics, font, component.getString(), tooltipx, rendery + j * 10, color, color == spell.getElement().getSpellColor() ? spell.getElement().getSpellDark() : ColorHelper.Argb.mul(color, 0x404040));

                } else if (distance <= 10) {
                    select = -1;
                }
                guiGraphics.blit(TEXTURE, (int) (x + sin - 11), (int) (y + cos - 11), 102, 0, 22, 22);
                if (select == i) {
                    guiGraphics.blit(TEXTURE, (int) (x + sin - 12), (int) (y + cos - 12), 124, 0, 24, 24);
                }

                // アイテム表示
                ItemStack tool = toolBeltComponent.stacks().get(i);
                if (!tool.isEmpty()) {
                    guiGraphics.renderItem(tool, (int) (x + sin - 8), (int) (y + cos - 8));
                    if (select == i) {
                        Component toolName = Component.empty().append(tool.getHoverName()).withStyle(tool.getRarity().getStyleModifier())
                                .withStyle(p_220170_ -> p_220170_.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(tool))));
                        int color = toolName.getStyle().getColor().getValue();
                        RenderHelper.renderFramedText(guiGraphics, Minecraft.getInstance().font, toolName.getString(), x - Minecraft.getInstance().font.width(toolName) / 2, y + 70, color, ColorHelper.Argb.mul(color, 0x404060));
                    }
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
        if (player == null || !canChangeTool(player)) return;
        CuriosHelper.getToolBeltStack(player).ifPresent(stack -> {
            if (stack.has(ComponentInit.TOOLBELT_COMPONENT) && select >= 0) {
                PacketDistributor.sendToServer(new SwapToolFromBeltPayload(select, player.getUUID()));
                ToolBeltHelper.swapTool(player, stack, select);
            }
        });
    }

    private boolean canChangeTool(Player player) {
        if (select < 0) {
            return false;
        }
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (stack.isEmpty() || stack.getTags().anyMatch(tag -> tag.equals(ItemTagKeys.TOOLS))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == KeyMappingEvent.TOOL_BELT.get().getKey().getValue()) {
            this.onClose();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
