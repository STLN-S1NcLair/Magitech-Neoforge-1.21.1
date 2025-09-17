package net.stln.magitech.gui;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.Magitech;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ToolRepairingScreen extends AbstractContainerScreen<ToolRepairingMenu> {
    private static final ResourceLocation CRAFTING_TABLE_LOCATION = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/tool_repairing.png");
    ItemStack stack = null;
    private OwoUIAdapter<FlowLayout> uiAdapter;
    private int bgWidth = 176;
    private int panelWidth = 160;

    public ToolRepairingScreen(ToolRepairingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = bgWidth + panelWidth;
        this.imageHeight = 199;
        this.titleLabelY = 4;
        this.inventoryLabelY = 106;
    }

    private static List<Component> getPanelText() {
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("recipe.magitech.tool_repairing.panel.title").withStyle(Style.EMPTY.withUnderlined(true)));
        components.add(Component.translatable("recipe.magitech.tool_repairing.panel.text"));
        return components;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (stack != menu.slots.get(0).getItem()) {
            reloadUI();
            stack = menu.slots.get(0).getItem();
        }
    }

    @Override
    protected void init() {
        super.init();
        this.uiAdapter = OwoUIAdapter.create(this, Containers::verticalFlow);
        reloadUI();
    }

    private void reloadUI() {
        FlowLayout root = this.uiAdapter.rootComponent;
        root.clearChildren();
        ToolStatsPanel.addPanel(root, Positioning.absolute(leftPos + bgWidth, topPos), menu.slots.get(0).getItem(), Component.translatable("recipe.magitech.tool_stats_panel"), getPanelText());
        this.uiAdapter.inflateAndMount();
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
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CRAFTING_TABLE_LOCATION, i, j, 0, 0, this.bgWidth, this.imageHeight);
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
    }
}