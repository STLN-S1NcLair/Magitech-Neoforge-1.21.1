package net.stln.magitech.content.entity.magicentity.nullixis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class NullixisRenderer extends SpellProjectileRenderer<NullixisEntity> {

    public NullixisRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new NullixisModel());
    }

    @Override
    protected void applyRotations(NullixisEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.translate(0.0, animatable.getBbHeight() / 2, 0.0);
    }
}
