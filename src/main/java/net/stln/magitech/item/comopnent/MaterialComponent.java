package net.stln.magitech.item.comopnent;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.item.tool.ToolMaterial;

public record MaterialComponent(ToolMaterial material) {

    public static final Codec<MaterialComponent> CODEC = RecordCodecBuilder.create(partMaterialComponentInstance ->
            partMaterialComponentInstance.group(
                    Codec.STRING.fieldOf("material").forGetter(MaterialComponent::getMaterialId)
            ).apply(partMaterialComponentInstance, MaterialComponentUtil::generatefromId)
    );
    public static final StreamCodec<ByteBuf, MaterialComponent> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
            MaterialComponentUtil::generatefromId, MaterialComponent::getMaterialId
    );

    public String getMaterialId() {
        return material.getId();
    }

    public ToolMaterial getMaterial() {
        return material;
    }
}

