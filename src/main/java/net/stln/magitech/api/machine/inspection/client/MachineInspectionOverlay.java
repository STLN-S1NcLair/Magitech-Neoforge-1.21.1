package net.stln.magitech.api.machine.inspection.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.machine.inspection.MachineInspectionApi;
import net.stln.magitech.api.machine.inspection.MachineInspectionData;
import net.stln.magitech.api.machine.inspection.MachineInspectionRenderer;
import net.stln.magitech.api.machine.inspection.IMachineInspectionTarget;
import net.stln.magitech.api.machine.inspection.network.MachineInspectionRequestPayload;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public final class MachineInspectionOverlay implements LayeredDraw.Layer {
    public static final MachineInspectionOverlay INSTANCE = new MachineInspectionOverlay();

    private MachineInspectionOverlay() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null
                || minecraft.level == null
                || minecraft.screen != null
                || minecraft.options.hideGui
                || !MachineInspectionApi.canDisplay(minecraft.player)) {
            MachineInspectionClient.clear();
            MachineInspectionClient.tick();
            return;
        }

        if (!(minecraft.hitResult instanceof BlockHitResult blockHitResult)) {
            MachineInspectionClient.clear();
            MachineInspectionClient.tick();
            return;
        }

        BlockPos targetPosition = blockHitResult.getBlockPos();
        MachineInspectionClient.updateTarget(resolveDisplayPosition(minecraft.level, targetPosition));
        PacketDistributor.sendToServer(new MachineInspectionRequestPayload(targetPosition));
        MachineInspectionClient.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
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
        var block = minecraft.level.getBlockState(titlePosition).getBlock();
        Component title = block.getName();
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        MachineInspectionRenderer.render(
                guiGraphics,
                title,
                new ItemStack(block.asItem()),
                data,
                partialTick,
                MachineInspectionClient.getFadeAlpha(partialTick)
        );
    }

    private static BlockPos resolveDisplayPosition(Level level, BlockPos targetPosition) {
        if (level.getBlockEntity(targetPosition) instanceof IMachineInspectionTarget inspectionTarget) {
            BlockPos displayPosition = inspectionTarget.getInspectionPosition();
            if (displayPosition != null) {
                return displayPosition;
            }
        }
        return targetPosition;
    }
}
