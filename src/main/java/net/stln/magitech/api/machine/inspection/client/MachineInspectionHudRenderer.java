package net.stln.magitech.api.machine.inspection.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.api.machine.inspection.MachineInspectionApi;
import net.stln.magitech.api.machine.inspection.MachineInspectionData;
import net.stln.magitech.api.machine.inspection.MachineInspectionRenderer;

/**
 * 検査データをワールド上のブロック情報へ結び付けてHUD描画へ渡します。
 * Resolves world-side block information and forwards inspection data to the HUD renderer.
 */
public final class MachineInspectionHudRenderer {
    private MachineInspectionHudRenderer() {
    }

    public static void render(GuiGraphics guiGraphics, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null
                || minecraft.level == null
                || minecraft.screen != null
                || minecraft.options.hideGui
                || !MachineInspectionApi.canDisplay(minecraft.player)) {
            return;
        }

        MachineInspectionData data = MachineInspectionClient.getDataForRender();
        if (data == null) {
            return;
        }

        BlockPos titlePosition = minecraft.level.getBlockState(data.displayPosition()).isAir()
                ? data.targetPosition()
                : data.displayPosition();
        Block block = minecraft.level.getBlockState(titlePosition).getBlock();
        Component title = block.getName();
        MachineInspectionRenderer.render(
                guiGraphics,
                title,
                new ItemStack(block.asItem()),
                data,
                partialTick,
                MachineInspectionClient.getFadeAlpha(partialTick)
        );
    }
}
