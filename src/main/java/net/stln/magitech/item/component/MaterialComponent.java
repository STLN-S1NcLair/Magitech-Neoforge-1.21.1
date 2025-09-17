package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.material.ToolMaterial;
import org.jetbrains.annotations.NotNull;

public record MaterialComponent(@NotNull ToolMaterial material) {

    public static final Codec<MaterialComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("material").forGetter(MaterialComponent::getMaterialId)
            ).apply(instance, MaterialComponentUtil::generatefromId)
    );
    public static final StreamCodec<ByteBuf, MaterialComponent> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(
            MaterialComponentUtil::generatefromId, MaterialComponent::getMaterialId
    );

    public @NotNull ResourceLocation getMaterialId() {
        return material.getId();
    }
}

