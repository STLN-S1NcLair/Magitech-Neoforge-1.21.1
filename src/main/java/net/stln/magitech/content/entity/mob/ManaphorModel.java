package net.stln.magitech.content.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class ManaphorModel extends GeoModel<ManaphorEntity> {
    private static final double FULL_LEAN_SPEED_FACTOR = 0.35;
    private static final float MAX_LEAN_ANGLE = 60.0F * Mth.DEG_TO_RAD;
    private static final ResourceLocation TEXTURE = Magitech.id("textures/entity/manaphor.png");
    private static final ResourceLocation GEO = Magitech.id("geo/entity/manaphor.geo.json");
    private static final ResourceLocation ANIM = Magitech.id("animations/entity/manaphorl.animation.json");

    @Override
    public ResourceLocation getModelResource(ManaphorEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(ManaphorEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ManaphorEntity animatable) {
        return ANIM;
    }

    @Override
    public void setCustomAnimations(ManaphorEntity animatable, long instanceId, AnimationState<ManaphorEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone body = getAnimationProcessor().getBone("body");
        if (body == null) {
            return;
        }

        double horizontalSpeed = Math.hypot(animatable.getX() - animatable.xo, animatable.getZ() - animatable.zo);
        double fullLeanSpeed = Math.max(animatable.getAttributeValue(Attributes.FLYING_SPEED) * FULL_LEAN_SPEED_FACTOR, 1.0E-4);
        float speedRatio = Mth.clamp((float) (horizontalSpeed / fullLeanSpeed), 0.0F, 1.0F);
        body.setRotX(-speedRatio * MAX_LEAN_ANGLE);
    }
}
