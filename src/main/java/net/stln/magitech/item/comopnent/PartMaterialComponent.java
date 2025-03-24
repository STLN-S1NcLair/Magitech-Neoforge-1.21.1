package net.stln.magitech.item.comopnent;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.item.tool.ToolMaterial;
import net.stln.magitech.item.tool.ToolMaterialDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record PartMaterialComponent(List<ToolMaterial> materials) {

    public List<String> getMaterialIds() {
        List<String> ids = new ArrayList<>();
        for (ToolMaterial material : materials) {
            ids.add(material.getId());
        }
        return ids;
    }

    public static final Codec<PartMaterialComponent> CODEC = RecordCodecBuilder.create(partMaterialComponentInstance ->
        partMaterialComponentInstance.group(
                Codec.STRING.listOf().fieldOf("materials").forGetter(PartMaterialComponent::getMaterialIds)
        ).apply(partMaterialComponentInstance, PartMaterialComponentUtil::generatefromId)
    );

    public static final StreamCodec<ByteBuf, PartMaterialComponent> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(
            PartMaterialComponentUtil::generatefromId, PartMaterialComponent::getMaterialIds
    );
}

