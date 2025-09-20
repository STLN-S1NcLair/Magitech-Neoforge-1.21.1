package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.material.ToolMaterialLike;
import org.jetbrains.annotations.NotNull;

public record MaterialComponent(@NotNull ToolMaterial material) {
    public static final Codec<MaterialComponent> CODEC = ToolMaterial.CODEC.xmap(MaterialComponent::new, MaterialComponent::material);
    public static final StreamCodec<RegistryFriendlyByteBuf, MaterialComponent> STREAM_CODEC = ToolMaterial.STREAM_CODEC.map(MaterialComponent::new, MaterialComponent::material);

    public MaterialComponent(@NotNull ToolMaterialLike holder) {
        this(holder.asToolMaterial());
    }
}

