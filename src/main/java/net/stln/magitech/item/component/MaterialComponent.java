package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.material.ToolMaterial;

public record MaterialComponent(ToolMaterial material) {

    public static final Codec<MaterialComponent> CODEC = RecordCodecBuilder.create(partMaterialComponentInstance ->
            partMaterialComponentInstance.group(
                    ResourceLocation.CODEC.fieldOf("upgradeInstance").forGetter(MaterialComponent::getMaterialId)
            ).apply(partMaterialComponentInstance, MaterialComponentUtil::generatefromId)
    );
    public static final StreamCodec<ByteBuf, MaterialComponent> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(
            MaterialComponentUtil::generatefromId, MaterialComponent::getMaterialId
    );

    public ResourceLocation getMaterialId() {
        return material.getId();
    }

    public ToolMaterial getMaterial() {
        return material;
    }
}

