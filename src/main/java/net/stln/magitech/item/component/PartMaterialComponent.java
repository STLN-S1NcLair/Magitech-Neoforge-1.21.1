package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.material.ToolMaterial;

import java.util.ArrayList;
import java.util.List;

public record PartMaterialComponent(List<ToolMaterial> materials) {

    public static final Codec<PartMaterialComponent> CODEC = RecordCodecBuilder.create(partMaterialComponentInstance ->
            partMaterialComponentInstance.group(
                    ResourceLocation.CODEC.listOf().fieldOf("materials").forGetter(PartMaterialComponent::getMaterialIds)
            ).apply(partMaterialComponentInstance, PartMaterialComponentUtil::generatefromId)
    );
    public static final StreamCodec<ByteBuf, PartMaterialComponent> STREAM_CODEC = ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
            PartMaterialComponentUtil::generatefromId, PartMaterialComponent::getMaterialIds
    );

    public List<ResourceLocation> getMaterialIds() {
        List<ResourceLocation> ids = new ArrayList<>();
        for (ToolMaterial material : materials) {
            if (material != null) {
                ids.add(material.getId());
            }
        }
        return ids;
    }

    public List<ToolMaterial> getMaterials() {
        return materials;
    }
}

