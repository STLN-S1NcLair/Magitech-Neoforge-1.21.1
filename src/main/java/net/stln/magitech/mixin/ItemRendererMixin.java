package net.stln.magitech.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.MaterialInit;
import net.stln.magitech.item.tool.ToolMaterial;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Unique
    private ResourceLocation getPartModelId(ToolMaterial material, ToolType toolType, ToolPart part) {
        return ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "item/tool/" + toolType.get() + "/" + material.getId() + "_" + part.get());
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel p_model, CallbackInfo ci) {
//        if (itemStack.getComponents().has(ModComponentInit.TOOL_PART_COMPONENT)) {
//
//            ToolType type = ((PartToolItem) stack.getItem()).getToolType();
//
//            List<ToolMaterial> partMaterials = itemStack.get(ModComponentInit.TOOL_PART_COMPONENT).getPartMaterials();
//            for (int i = 0; i < partMaterials.size(); i++) {
//
//                ToolMaterial partMaterial = partMaterials.get(i);
//
//                ToolPart toolPart = PartMaterialDictionary.getToolPartFromIndex(type, i);

//                minecraft.getItemRenderer().render(new ItemStack(Items.IRON_INGOT), displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, minecraft.getModelManager().getModel(getPartModelId(partMaterial, type, toolPart)));
        if (!itemStack.is(Items.IRON_INGOT)) {
            minecraft.getItemRenderer().render(new ItemStack(Items.IRON_INGOT), displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, ((FabricBakedModelManager)minecraft.getModelManager()).getModel(
                    getPartModelId(MaterialInit.ENDER_METAL, ToolType.LIGHT_SWORD, ToolPart.LIGHT_BLADE)));
        }
//            }
//
//        }
    }
}
