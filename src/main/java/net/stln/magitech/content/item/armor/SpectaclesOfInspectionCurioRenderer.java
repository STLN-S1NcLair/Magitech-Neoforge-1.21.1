package net.stln.magitech.content.item.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class SpectaclesOfInspectionCurioRenderer implements ICurioRenderer {
    private final SpectaclesOfInspectionRenderer renderer = new SpectaclesOfInspectionRenderer();

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(renderLayerParent.getModel() instanceof HumanoidModel<?>)) {
            return;
        }

        LivingEntity entity = slotContext.entity();
        @SuppressWarnings("unchecked")
        HumanoidModel<LivingEntity> baseModel = (HumanoidModel<LivingEntity>) renderLayerParent.getModel();
        baseModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        baseModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        ICurioRenderer.followHeadRotations(entity, baseModel.head);

        this.renderer.prepForRender(entity, stack, EquipmentSlot.HEAD, baseModel, buffer, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
        SpectaclesOfInspectionItem animatable = this.renderer.getAnimatable();
        ResourceLocation texture = this.renderer.getGeoModel().getTextureResource(animatable);
        RenderType renderType = this.renderer.getRenderType(animatable, texture, buffer, partialTick);
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        this.renderer.defaultRender(poseStack, animatable, buffer, renderType, vertexConsumer, 0, partialTick, packedLight);
    }
}
