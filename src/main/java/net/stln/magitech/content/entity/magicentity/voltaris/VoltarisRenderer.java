package net.stln.magitech.content.entity.magicentity.voltaris;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.util.Color;

public class VoltarisRenderer extends SpellProjectileRenderer<VoltarisEntity> {

    public VoltarisRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new VoltarisModel());
    }

    @Override
    public Color getRenderColor(VoltarisEntity animatable, float partialTick, int packedLight) {
        return Color.WHITE;
    }

    @Override
    public @Nullable RenderType getRenderType(VoltarisEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderHelper.additiveCull(texture);
    }
}
