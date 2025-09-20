package net.stln.magitech.item.tool.material;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.trait.Trait;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ToolMaterial(ToolStats stats, ToolStats spellCasterStats, Trait trait) implements ToolMaterialLike {
    public static final Codec<ToolMaterial> CODEC = MagitechRegistries.TOOL_MATERIAL.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolMaterial> STREAM_CODEC = ByteBufCodecs.registry(MagitechRegistries.Keys.TOOL_MATERIAL);

    public @NotNull ResourceLocation getId() {
        return Objects.requireNonNull(MagitechRegistries.TOOL_MATERIAL.getKey(this));
    }

    public @NotNull String getDescriptionId() {
        return getId().toLanguageKey("material");
    }

    public @NotNull Component getDescription() {
        return Component.translatable(getDescriptionId());
    }

    @Override
    public @NotNull ToolMaterial asToolMaterial() {
        return this;
    }
}
