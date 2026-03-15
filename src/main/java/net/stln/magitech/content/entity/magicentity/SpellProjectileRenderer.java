package net.stln.magitech.content.entity.magicentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.effect.visual.RenderTypeTokenInit;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.LodestoneRenderTypeBuilder;

@OnlyIn(Dist.CLIENT)
public class SpellProjectileRenderer<T extends SpellProjectileEntity> extends GeoEntityRenderer<T> {

    public SpellProjectileRenderer(EntityRendererProvider.Context ctx, GeoModel<T> geoModel) {
        super(ctx, geoModel);
    }

    @Override
    public Color getRenderColor(T animatable, float partialTick, int packedLight) {
        return Color.WHITE;
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderHelper.additiveCull(texture);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        Vec3 velocity = animatable.getDeltaMovement();

        if (!velocity.equals(Vec3.ZERO)) {
            double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

            // Yaw (水平方向の回転) → 北を基準にする
            float yaw2 = (float) (-Math.toDegrees(Math.atan2(-velocity.x, velocity.z)));

            // Pitch (上下の回転)
            float pitch = (float) -Math.toDegrees(Math.atan2(-velocity.y, horizontalSpeed));

            // 回転を適用
            poseStack.rotateAround(Axis.YP.rotationDegrees(yaw2), 0, animatable.getBbHeight() / 2, 0);
            poseStack.rotateAround(Axis.XN.rotationDegrees(pitch), 0, animatable.getBbHeight() / 2, 0);
            poseStack.translate(0.0, animatable.getBbHeight() / 2, 0.0);
        }
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    protected LodestoneRenderTypeBuilder renderTypeBuilder() {
        return LodestoneRenderTypes.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeTokenInit.TRAIL);
    }
}
