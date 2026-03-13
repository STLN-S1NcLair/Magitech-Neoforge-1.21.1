package net.stln.magitech.content.entity.magicentity.volkarin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class VolkarinRenderer extends SpellProjectileRenderer<VolkarinEntity> {

    public VolkarinRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new VolkarinModel());
    }

    @Override
    public Color getRenderColor(VolkarinEntity animatable, float partialTick, int packedLight) {
        return Color.WHITE;
    }

    @Override
    public @Nullable RenderType getRenderType(VolkarinEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderHelper.additiveNoCull(texture);
    }
}
