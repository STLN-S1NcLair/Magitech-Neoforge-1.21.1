package net.stln.magitech.gui;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.item.tool.upgrade.Upgrade;
import net.stln.magitech.item.tool.upgrade.UpgradeInstance;
import net.stln.magitech.item.tool.upgrade.UpgradeRegister;
import net.stln.magitech.util.ClientHelper;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.RenderHelper;
import net.stln.magitech.util.ToolMaterialUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ToolUpgradeScreen extends AbstractContainerScreen<ToolUpgradeMenu> {
    private static final ResourceLocation BG_LOCATION = Magitech.id("textures/gui/tool_upgrade.png");
    private static final int SWITCH_INTERVAL = 20; // 20tick = 1秒
    private int tickCounter = 0;
    private int currentIndex = 0;
    List<Item> tagItems = new ArrayList<>();
    ItemStack stack = null;
    private OwoUIAdapter<FlowLayout> uiAdapter;

    private int bgWidth = 176;
    private int panelWidth = 160;

    public ToolUpgradeScreen(ToolUpgradeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = bgWidth + panelWidth;
        this.imageHeight = 199;
        this.titleLabelY = 4;
        this.inventoryLabelY = 106;
    }

    @Override
    protected void init() {
        super.init();
        this.uiAdapter = OwoUIAdapter.create(this, Containers::verticalFlow);
        reloadUI();
    }

    /**
     * Renders the graphical user interface (GUI) element.
     *
     * @param guiGraphics the GuiGraphics object used for rendering.
     * @param mouseX      the x-coordinate of the mouse cursor.
     * @param mouseY      the y-coordinate of the mouse cursor.
     * @param partialTick the partial tick time.
     */
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(BG_LOCATION, i, j, 0, 0, bgWidth, this.imageHeight);
        int l = this.leftPos + 44;
        int i1 = this.topPos + 31;
        int j1 = 3;
        if (menu.canUpgrade()) {
            this.renderButtons(guiGraphics, mouseX, mouseY, l, i1, j1);
        } else {
            ItemStack itemStack = menu.container.getItem(0);
            if (itemStack.getItem() instanceof PartToolItem) {
                if (menu.hasUpgradePoint(itemStack) && !ToolMaterialUtil.isCorrectMaterialForUpgrade(ComponentHelper.getTier(itemStack), ComponentHelper.getUpgradePoint(itemStack), menu.container.getItem(1).getItem())) {
                    MutableComponent text = Component.translatable("recipe.magitech.tool_upgrade.incorrect_material");
                    guiGraphics.drawString(this.font, text.withColor(0xF0D080), l - font.width(text) / 2 + 58, i1 + 15, 0xFFFFFF, false);

                    tagItems = BuiltInRegistries.ITEM.getTag(ToolMaterialUtil.getUpgradeMaterialTag(ComponentHelper.getTier(itemStack), ComponentHelper.getUpgradePoint(itemStack))).stream().flatMap(HolderSet.ListBacked::stream).map(Holder::value).toList();
                    currentIndex = Math.min(currentIndex, tagItems.size() - 1);
                    guiGraphics.renderItem(new ItemStack(tagItems.get(currentIndex)), l - 8 + 58, i1 + 31);

                } else {
                    MutableComponent text = Component.translatable("recipe.magitech.tool_upgrade.no_upgrade_point");
                    guiGraphics.drawString(this.font, text.withColor(0xFF8080), l - font.width(text) / 2 + 58, i1 + 23, 0xFFFFFF, false);
                }
            } else {
                MutableComponent text = Component.translatable("recipe.magitech.tool_upgrade.no_tool");
                guiGraphics.drawString(this.font, text.withColor(0x202020), l - font.width(text) / 2 + 58, i1 + 23, 0xFFFFFF, false);
            }
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (stack != menu.container.getItem(0)) {
            reloadUI();
            stack = menu.container.getItem(0);
        }
        if (!tagItems.isEmpty()) {
            tickCounter++;
            if (tickCounter >= SWITCH_INTERVAL) {
                tickCounter = 0;
                currentIndex = (currentIndex + 1) % tagItems.size();
            }
        }
    }

    private void reloadUI() {
        FlowLayout root = this.uiAdapter.rootComponent;
        root.clearChildren();
        ToolStatsPanel.addPanel(root, Positioning.absolute(leftPos + bgWidth, topPos), menu.container.getItem(0), Component.translatable("recipe.magitech.tool_stats_panel"), getPanelText());
        this.uiAdapter.inflateAndMount();
    }

    private static List<Component> getPanelText() {
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("recipe.magitech.tool_upgrade.panel.title").withStyle(Style.EMPTY.withUnderlined(true)));
        components.add(Component.translatable("recipe.magitech.tool_upgrade.panel.text"));
        return components;
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        ItemStack itemStack = menu.container.getItem(0);
        int i = this.leftPos + 44;
        int j = this.topPos + 31;
        int k = 3;
        if (this.menu.canUpgrade()) {

            for (int l = 0; l < k; l++) {
                if (x >= i && x < i + 117 && y >= j + 18 * l && y < j + 18 + 18 * l) {
                    List<Component> upgradeTooltips = RenderHelper.getUpgradeTooltips(List.of(new UpgradeInstance(1, menu.upgrades.get(l))), ((PartToolItem) itemStack.getItem()).getToolType(), itemStack.getItem() instanceof SpellCasterItem);
                    upgradeTooltips.addFirst(Component.translatable("recipe.magitech.tool_upgrade.click_to_apply").withStyle(style -> style.withColor(0xF0D080)));
                    guiGraphics.renderTooltip(this.font,
                            upgradeTooltips, Optional.empty(), x, y);
                }
            }
        }
        if (!menu.canUpgrade() && menu.hasUpgradePoint(itemStack) && itemStack.getItem() instanceof PartToolItem && !ToolMaterialUtil.isCorrectMaterialForUpgrade(ComponentHelper.getTier(itemStack), ComponentHelper.getUpgradePoint(itemStack), menu.container.getItem(1).getItem())) {
            if (x >= i + 50 && x < i + 66 && y >= j + 31 && y < j + 47) {
                guiGraphics.renderTooltip(font, new ItemStack(tagItems.get(currentIndex)), x, y);
            }
        }
    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int lastVisibleElementIndex) {
        ItemStack stack = this.menu.container.getItem(0);
        if (stack.getItem() instanceof PartToolItem) {
            List<UpgradeInstance> upgrades = ComponentHelper.getUpgrades(stack);
                List<Upgrade> currentUpgrades = menu.upgrades;
                for (int i = 0; i < lastVisibleElementIndex; i++) {
                    int i1 = y + i * 18;
                    int level = 0;

                    for (UpgradeInstance upgrade : upgrades) {
                        if (currentUpgrades.get(i).equals(upgrade.upgrade())) {
                            level = upgrade.level();
                            break;
                        }
                    }
                    int yOffset = 200;
                    if (mouseX >= x && mouseX < x + 117 && mouseY >= i1 && mouseY < i1 + 18) {
                        yOffset += 24;
                    }
                    ResourceLocation id = UpgradeRegister.getId(currentUpgrades.get(i));
                    guiGraphics.blit(BG_LOCATION, x, i1, 0, yOffset, 117, 18);
                    guiGraphics.drawString(this.font, Component.translatable("upgrade." + id.getNamespace() + "." + id.getPath()).withColor(0xE0E0E0).append(Component.literal(" Lv." + level).withColor(0xF0D080)), x + 5, i1 + 5, 0xFFFFFF, false);
                }
        }
    }

    /**
     * Called when a mouse button is clicked within the GUI element.
     * <p>
     *
     * @param mouseX the X coordinate of the mouse.
     * @param mouseY the Y coordinate of the mouse.
     * @param button the button that was clicked.
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
            int i = this.leftPos + 44;
            int j = this.topPos + 31;
            int k = 3;

            for (int l = 0; l < k; l++) {
                double d0 = mouseX - (double) (i);
                double d1 = mouseY - (double) (j + l * 18);
                Player player = ClientHelper.getPlayer();
                if (player == null) return false;
                if (d0 >= 0.0 && d1 >= 0.0 && d0 < 117.0 && d1 < 18.0 && this.menu.clickMenuButton(player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, l);
                    return true;
                }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
