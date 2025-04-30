package net.stln.magitech.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.event.KeyMappingEvent;
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
import net.stln.magitech.network.ThreadBoundSelectPayload;
import net.stln.magitech.util.MathUtil;
import net.stln.magitech.util.RenderHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class RadialSpellMenuOverlay extends Screen {

    private static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/mana_gauge.png");

    private int select = -1;
    private int ticks = 0;
    float selectAnimTick = 0;
    float selectTick = 0.0F;

    public RadialSpellMenuOverlay(Component title) {
        super(title);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!Minecraft.getInstance().options.hideGui) {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight() / 2;

            double dx = mouseX - x;
            double dy = mouseY - y;
            double naturalMouseAngle = MathUtil.getGeneralAngle(Math.atan2(dy, dx) + Math.PI / 2);

            Player player = Minecraft.getInstance().player;
            ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
            ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

            if (threadbound.has(ComponentInit.SPELL_COMPONENT)) {
                SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
                int index = 0;
                for (Spell spell : spellComponent.spells()) {
                    ResourceLocation icon = SpellRegister.getId(spell);
                    if (icon != null) {
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
                        float squareEase = Math.min(selectTick * selectTick / 4, 4) * 2;
                        if (mouseAngle >= min && mouseAngle <= max && distance > 10) {
                            if (select != index) {
                                select = index;
                                selectAnimTick = -partialTicks - ticks;
                            }
                            selectTick = selectAnimTick + ticks + partialTicks + 0.01F;
                            size *= (float) (Math.clamp(selectTick / 5, 0.0, 0.5) + 1.0);

                            if (distance > 20) {
                                ResourceLocation location = SpellRegister.getId(spell);
                                String text = Component.translatable("spell." + location.getNamespace() + "." + location.getPath()).getString();
                                int renderx = (x - font.width(text) / 2);
                                int rendery = (int) (y - 4 + 8 - squareEase);
                                RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, spell.getElement());
                                int i = 1;
                                for (Component component : spell.getTooltip(player.level(), player, player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellCasterItem ? player.getItemInHand(InteractionHand.MAIN_HAND) : player.getItemInHand(InteractionHand.OFF_HAND))) {
                                    i++;
                                    int tooltipx = (x - font.width(component.getString()) / 2);
                                    RenderHelper.renderFramedText(guiGraphics, font, component.getString(), tooltipx, rendery + i * 10, 0xFFFFFF, 0x404040);
                                }
                            }
                        } else if (distance <= 10) {
                            select = -1;
                        }
                        String namespace = icon.getNamespace();
                        String path = icon.getPath();
                        icon = ResourceLocation.fromNamespaceAndPath(namespace, "textures/spell/" + path + ".png");
                        guiGraphics.blit(icon, (int) (x + sin - size * 16), (int) (y + cos - size * 16), (int) (size * 32), (int) (size * 32), 0, 0, 32, 32, 32, 32);

                        Cooldown cooldown = CooldownData.getCurrentCooldown(player, spell);
                        if (cooldown != null) {
                            int shadeHeight = (int) (32 * (1 - cooldown.getProgress() / cooldown.getCooltime()));
                            guiGraphics.setColor(0.3F, 0.3F, 0.3F, 1F);
                            int mulHeight = (int) (size * shadeHeight);
                            guiGraphics.blit(icon, (int) (x + sin - size * 16), (int) (y + cos + (size * 16) - mulHeight), (int) (size * 32), mulHeight, 0, 32 - shadeHeight, 32, shadeHeight, 32, 32);

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
                            int renderx = (int) (x + sin - font.width(text) / 2);
                            int rendery = (int) (y + cos + 8 * size);
                            if (animTick == animLength) {
                            RenderHelper.renderFramedText(guiGraphics, font, text, renderx, rendery, element);
                            }
                        }
                    }
                    index++;
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

        Player player = Minecraft.getInstance().player;
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
        ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

        if (threadbound.has(ComponentInit.SPELL_COMPONENT) && select >= 0) {
            PacketDistributor.sendToServer(new ThreadBoundSelectPayload(select, Minecraft.getInstance().player.getUUID().toString()));
            SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
            threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spellComponent.spells(), select));
        }
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
