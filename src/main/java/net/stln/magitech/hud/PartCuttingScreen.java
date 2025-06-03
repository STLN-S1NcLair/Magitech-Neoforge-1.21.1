package net.stln.magitech.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.stln.magitech.Magitech;
import net.stln.magitech.hud.PartCuttingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.stln.magitech.recipe.PartCuttingRecipe;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PartCuttingScreen extends AbstractContainerScreen<PartCuttingMenu> {
    private static final ResourceLocation BG_LOCATION = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/part_cutting.png");
    private static final int SCROLLER_WIDTH = 8;
    private static final int SCROLLER_HEIGHT = 8;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 18;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 46;
    private static final int RECIPES_Y = 31;
    private float scrollOffs;
    /**
     * Is {@code true} if the player clicked on the scroll wheel in the GUI.
     */
    private boolean scrolling;
    /**
     * The index of the first recipe to display.
     * The number of recipes displayed at any time is 12 (4 recipes per row, and 3 rows). If the player scrolled down one row, this value would be 4 (representing the index of the first slot on the second row).
     */
    private int startIndex;
    private boolean displayRecipes;

    public PartCuttingScreen(PartCuttingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        menu.registerUpdateListener(this::containerChanged);
        this.imageWidth = 176;
        this.imageHeight = 199;
        this.titleLabelY = 4;
        this.inventoryLabelY = 106;
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
        guiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = (int)(41.0F * this.scrollOffs);
        int offset = this.isScrollBarActive() ? 0 : 8;
        guiGraphics.blit(BG_LOCATION, i + 121, j + 31 + k, offset, 240, 8, 8);
        int l = this.leftPos + 46;
        int i1 = this.topPos + 30;
        int j1 = this.startIndex + 12;
        this.renderButtons(guiGraphics, mouseX, mouseY, l, i1, j1);
        this.renderRecipes(guiGraphics, l, i1, j1);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        if (this.displayRecipes) {
            int i = this.leftPos + 46;
            int j = this.topPos + 30;
            int k = this.startIndex + 12;
            List<RecipeHolder<PartCuttingRecipe>> list = this.menu.getRecipes();

            for (int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); l++) {
                int i1 = l - this.startIndex;
                int j1 = i + i1 % 4 * 18;
                int k1 = j + i1 / 4 * 18 + 2;
                if (x >= j1 && x < j1 + 18 && y >= k1 && y < k1 + 18) {
                    guiGraphics.renderTooltip(this.font, list.get(l).value().assemble(new SingleRecipeInput(this.menu.inputSlot.getItem()), this.minecraft.level.registryAccess()), x, y);
                }
            }
        }
    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int lastVisibleElementIndex) {
        for (int i = this.startIndex; i < lastVisibleElementIndex && i < this.menu.getNumRecipes(); i++) {
            int j = i - this.startIndex;
            int k = x + j % 4 * 18;
            int l = j / 4;
            int i1 = y + l * 18 + 2;
            ResourceLocation resourcelocation;
            int offx = 0;
            int offy = 0;
            if (i == this.menu.getSelectedRecipeIndex()) {
                offy = 18;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 18 && mouseY < i1 + 18) {
                offx = 18;
            }

            guiGraphics.blit(BG_LOCATION, k, i1 - 1, offx, offy + 200, 18, 18);
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int startIndex) {
        List<RecipeHolder<PartCuttingRecipe>> list = this.menu.getRecipes();

        for (int i = this.startIndex; i < startIndex && i < this.menu.getNumRecipes(); i++) {
            int j = i - this.startIndex;
            int k = x + 1 + j % 4 * 18;
            int l = j / 4;
            int i1 = y + l * 18 + 2;
            PartCuttingRecipe recipe = list.get(i).value();
            int count = recipe.getCount();
            ItemStack assembled = recipe.assemble(new SingleRecipeInput(this.menu.inputSlot.getItem()), this.minecraft.level.registryAccess());
            guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(count), k, i1, 0x999999, false);
            guiGraphics.renderItem(assembled, k, i1);
        }
    }

    /**
     * Called when a mouse button is clicked within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     *
     * @param mouseX the X coordinate of the mouse.
     * @param mouseY the Y coordinate of the mouse.
     * @param button the button that was clicked.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        if (this.displayRecipes) {
            int i = this.leftPos + 46;
            int j = this.topPos + 30;
            int k = this.startIndex + 12;

            for (int l = this.startIndex; l < k; l++) {
                int i1 = l - this.startIndex;
                double d0 = mouseX - (double)(i + i1 % 4 * 18);
                double d1 = mouseY - (double)(j + i1 / 4 * 18);
                if (d0 >= 0.0 && d1 >= 0.0 && d0 < 18.0 && d1 < 18.0 && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, l);
                    return true;
                }
            }

            i = this.leftPos + 119;
            j = this.topPos + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Called when the mouse is dragged within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     *
     * @param mouseX the X coordinate of the mouse.
     * @param mouseY the Y coordinate of the mouse.
     * @param button the button that is being dragged.
     * @param dragX  the X distance of the drag.
     * @param dragY  the Y distance of the drag.
     */
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float)scrollY / (float)i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5) * 4;
        }

        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && this.menu.getNumRecipes() > 12;
    }

    protected int getOffscreenRows() {
        return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
    }

    private void containerChanged() {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }
}
