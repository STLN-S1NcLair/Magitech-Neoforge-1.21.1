package net.stln.magitech.content.block.block_entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.block.EnvirometerBlock;
import net.stln.magitech.content.block.block_entity.EnvirometerBlockEntity;
import net.stln.magitech.core.api.field_effect.FieldEffectHelper;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.data.FieldEffectClientCache;
import net.stln.magitech.effect.visual.FieldEffectIconRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class EnvirometerBlockEntityRenderer implements BlockEntityRenderer<EnvirometerBlockEntity> {
    private static final float ICON_SIZE = 0.62F;
    private static final float ICON_ALPHA = 1.0F;
    private static final int EFFECT_FADE_TICKS = 40;

    private final Map<Level, Map<BlockPos, EffectTransition>> effectTransitions = new WeakHashMap<>();

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
        Map<BlockPos, EffectTransition> levelTransitions = effectTransitions.computeIfAbsent(
                level,
                ignored -> new HashMap<>()
        );
        EffectTransition transition = levelTransitions.get(entity.getBlockPos());
        if (transition == null) {
            if (effect == null) {
                return;
            }
            transition = new EffectTransition(effect, level.getGameTime());
            levelTransitions.put(entity.getBlockPos(), transition);
        } else {
            transition.update(effect, level.getGameTime());
        }

        if (transition.isEmpty()) {
            levelTransitions.remove(entity.getBlockPos());
            return;
        }

        BlockState state = entity.getBlockState();
        Direction facing = state.getValue(EnvirometerBlock.FACING);
        transition.render(
                poseStack,
                bufferSource,
                entity.getBlockPos().getCenter(),
                facing,
                partialTick
        );
        if (transition.isFinished()) {
            if (transition.targetEffect() == null) {
                levelTransitions.remove(entity.getBlockPos());
            } else {
                transition.clearSourceEffect();
            }
        }
    }

    private static final class EffectTransition {
        private FieldEffectType sourceEffect;
        private FieldEffectType targetEffect;
        private int elapsedTicks;
        private long lastGameTime;

        private EffectTransition(FieldEffectType targetEffect, long gameTime) {
            this.targetEffect = targetEffect;
            this.lastGameTime = gameTime;
        }

        private void update(FieldEffectType effect, long gameTime) {
            advance(gameTime);
            if (targetEffect != effect) {
                sourceEffect = targetEffect;
                targetEffect = effect;
                elapsedTicks = 0;
            }
        }

        private void advance(long gameTime) {
            long elapsed = gameTime - lastGameTime;
            if (elapsed > 0L) {
                elapsedTicks = Math.min(
                        EFFECT_FADE_TICKS,
                        elapsedTicks + (int) Math.min((long) EFFECT_FADE_TICKS, elapsed)
                );
            }
            lastGameTime = gameTime;
        }

        private void render(
                PoseStack poseStack,
                MultiBufferSource bufferSource,
                Vec3 blockCenter,
                Direction facing,
                float partialTick
        ) {
            float progress = Math.clamp(
                    (elapsedTicks + Math.clamp(partialTick, 0.0F, 1.0F)) / EFFECT_FADE_TICKS,
                    0.0F,
                    1.0F
            );
            if (sourceEffect != null) {
                FieldEffectIconRenderer.renderWorld(
                        poseStack,
                        bufferSource,
                        blockCenter,
                        facing,
                        sourceEffect,
                        ICON_SIZE,
                        ICON_ALPHA * (1.0F - progress)
                );
            }
            if (targetEffect != null) {
                FieldEffectIconRenderer.renderWorld(
                        poseStack,
                        bufferSource,
                        blockCenter,
                        facing,
                        targetEffect,
                        ICON_SIZE,
                        ICON_ALPHA * progress
                );
            }
        }

        private boolean isEmpty() {
            return sourceEffect == null && targetEffect == null;
        }

        private boolean isFinished() {
            return elapsedTicks >= EFFECT_FADE_TICKS;
        }

        private FieldEffectType targetEffect() {
            return targetEffect;
        }

        private void clearSourceEffect() {
            sourceEffect = null;
        }
    }
}
