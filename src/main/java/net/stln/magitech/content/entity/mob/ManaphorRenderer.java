package net.stln.magitech.content.entity.mob;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ManaphorRenderer extends GeoEntityRenderer<ManaphorEntity> {
    public ManaphorRenderer(EntityRendererProvider.Context context) {
        super(context, new ManaphorModel());
        this.shadowRadius = 0.25F;
    }

    @Override
    public @Nullable RenderType getRenderType(ManaphorEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderHelper.additiveNoCull(texture);
    }
}
