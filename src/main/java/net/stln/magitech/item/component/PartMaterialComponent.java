package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.material.ToolMaterialLike;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public record PartMaterialComponent(List<ToolMaterial> materials) {
    public static final Codec<PartMaterialComponent> CODEC = ToolMaterial.CODEC.listOf().xmap(PartMaterialComponent::new, PartMaterialComponent::materials);
    public static final StreamCodec<RegistryFriendlyByteBuf, PartMaterialComponent> STREAM_CODEC = ToolMaterial.STREAM_CODEC.apply(ByteBufCodecs.list()).map(PartMaterialComponent::new, PartMaterialComponent::materials);

    public static final PartMaterialComponent EMPTY = new PartMaterialComponent(List.of());

    public PartMaterialComponent(@NotNull ToolMaterialLike... materials) {
        this(Arrays.stream(materials).map(ToolMaterialLike::asToolMaterial).toList());
    }
}

