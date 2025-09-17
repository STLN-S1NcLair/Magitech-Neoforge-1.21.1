package net.stln.magitech.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.model.ModelRegistrar;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.util.ComponentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemRenderer.class)
public class MagitechItemRendererMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel p_model, CallbackInfo ci) {
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        ModelManager modelManager = minecraft.getModelManager();

        if (itemStack.getItem() instanceof PartToolItem partToolItem) {
            ToolType toolType = partToolItem.getToolType();
            List<ToolMaterial> partMaterials = ComponentHelper.getPartMaterials(itemStack);
            for (int i = 0; i < partMaterials.size(); i++) {
                ToolMaterial partMaterial = partMaterials.get(i);

                ToolPart toolPart = ToolMaterialRegister.getToolPartFromIndex(toolType, i);

                if (toolPart != null) {
                    itemRenderer.render(new ItemStack(Items.IRON_INGOT), displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, modelManager.getModel(ModelResourceLocation.standalone(ModelRegistrar.getPartModelId(partMaterial, toolType.getId(), toolPart.get()))));
                }
            }
        }
        if (itemStack.getItem() instanceof PartItem partItem) {
            ComponentHelper.getMaterial(itemStack)
                    .ifPresent(toolMaterial -> itemRenderer.render(new ItemStack(Items.IRON_INGOT), displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, modelManager.getModel(ModelResourceLocation.standalone(ModelRegistrar.getPartItemModelId(toolMaterial, partItem.getPart().get())))));
        }
    }
}
