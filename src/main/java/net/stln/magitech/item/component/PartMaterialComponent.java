package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.material.ToolMaterial;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public record PartMaterialComponent(List<ToolMaterial> materials) {
    public static final Codec<PartMaterialComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.listOf().fieldOf("materials").forGetter(PartMaterialComponent::getMaterialIds)
            ).apply(instance, PartMaterialComponentUtil::generatefromId)
    );
    public static final StreamCodec<ByteBuf, PartMaterialComponent> STREAM_CODEC = ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list())
            .map(PartMaterialComponentUtil::generatefromId, PartMaterialComponent::getMaterialIds);

    public static final PartMaterialComponent EMPTY = new PartMaterialComponent(List.of());

    public PartMaterialComponent(@NotNull ToolMaterial... materials) {
        this(Arrays.asList(materials));
    }

    public @NotNull List<@NotNull ResourceLocation> getMaterialIds() {
        return materials.stream().map(ToolMaterial::getId).toList();
    }
}

