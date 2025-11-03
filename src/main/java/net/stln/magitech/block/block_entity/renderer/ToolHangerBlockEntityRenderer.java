package net.stln.magitech.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.block.ToolHangerBlock;
import net.stln.magitech.block.ZardiusCrucibleBlock;
import net.stln.magitech.block.block_entity.ToolHangerBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class ToolHangerBlockEntityRenderer implements BlockEntityRenderer<ToolHangerBlockEntity> {
    public ToolHangerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ToolHangerBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
        ItemStackHandler itemStack = blockEntity.inventory;
        int leng = itemStack.getSlots();

        for (int i = 0; i < leng; i++) {
            if (itemStack.getStackInSlot(i).isEmpty()) {
                continue;
            }
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            Direction direction = blockEntity.getBlockState().getValue(ToolHangerBlock.FACING);
            double rotation = Math.toRadians(direction.toYRot());

            Vec3 rotatedOffset = ToolHangerBlock.getToolRenderPos(i, blockEntity.getBlockState());

            poseStack.pushPose();
            poseStack.translate(0.5 + rotatedOffset.x, 0.35, 0.5 + rotatedOffset.z);
            poseStack.scale(0.85f, 0.85f, 0.85f);
            poseStack.mulPose(Axis.YN.rotation((float) (rotation + Math.toRadians(180))));
            poseStack.mulPose(Axis.ZN.rotationDegrees(-135));

            itemRenderer.renderStatic(itemStack.getStackInSlot(i), ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(),
                    blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
