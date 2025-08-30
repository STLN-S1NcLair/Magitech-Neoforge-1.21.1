package net.stln.magitech.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.block_entity.ManaContainerBlockEntity;

import java.util.Optional;

public class ManaContainerInfoOverlay implements LayeredDraw.Layer {

    private static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/mana_gauge.png");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!Minecraft.getInstance().options.hideGui && !Minecraft.getInstance().player.isSpectator() && !Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen() && Minecraft.getInstance().screen == null) {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight() / 2;
            Player player = Minecraft.getInstance().player;
            Level level = player.level();
            BlockHitResult traceResult = level.clip(new ClipContext(player.getEyePosition(1f),
                    (player.getEyePosition(1f).add(player.getViewVector(1f).scale(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue()))),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            if (traceResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = traceResult.getBlockPos();
                BlockState blockState = level.getBlockState(blockPos);
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof ManaContainerBlockEntity containerBlockEntity) {
                    guiGraphics.renderTooltip(Minecraft.getInstance().font, containerBlockEntity.getManaInfo(), Optional.empty(), x + 20, y);
                }
            }
        }
    }
}
