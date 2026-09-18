package net.stln.magitech.content.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.EnvirometerBlock;
import net.stln.magitech.content.block.block_entity.EnvirometerBlockEntity;
import net.stln.magitech.core.api.field_effect.FieldEffectHelper;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.data.FieldEffectClientCache;
import net.stln.magitech.effect.visual.FieldEffectIconRenderer;

public class EnvirometerBlockEntityRenderer implements BlockEntityRenderer<EnvirometerBlockEntity> {
    private static final float ICON_SIZE = 0.62F;
    private static final float ICON_ALPHA = 0.9F;

    public EnvirometerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(
            EnvirometerBlockEntity entity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        Level level = entity.getLevel();
        if (level == null) {
            return;
        }

        FieldEffectType effect = FieldEffectHelper.getFieldEffect(
                level,
                FieldEffectClientCache.getInstance().getFieldEffectInstance(entity.getBlockPos())
        );
        if (effect == null) {
            return;
        }

        BlockState state = entity.getBlockState();
        Direction facing = state.getValue(EnvirometerBlock.FACING);
        FieldEffectIconRenderer.renderWorld(poseStack, bufferSource, facing, effect, ICON_SIZE, ICON_ALPHA);
    }
}
