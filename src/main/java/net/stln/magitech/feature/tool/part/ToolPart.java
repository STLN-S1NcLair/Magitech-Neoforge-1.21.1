package net.stln.magitech.feature.tool.part;


import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.stln.magitech.MagitechRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record ToolPart(float order, Supplier<Item> partItem) implements ToolPartLike {
    public static final Codec<ToolPart> CODEC = MagitechRegistries.TOOL_PART.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolPart> STREAM_CODEC = ByteBufCodecs.registry(MagitechRegistries.Keys.TOOL_PART);

    @Override
    public @NotNull ToolPart asToolPart() {
        return this;
    }
}
//public enum ToolPart {
//    LIGHT_BLADE("light_blade"),
//    HEAVY_BLADE("heavy_blade"),
//    LIGHT_HANDLE("light_handle"),
//    HEAVY_HANDLE("heavy_handle"),
//    TOOL_BINDING("tool_binding"),
//    HANDGUARD("handguard"),
//    STRIKE_HEAD("strike_head"),
//    SPIKE_HEAD("spike_head"),
//    REINFORCED_STICK("reinforced_stick"),
//    PLATE("plate"),
//    CATALYST("catalyst"),
//    CONDUCTOR("conductor");
//
//    private final String id;
//
//    ToolPart(String id) {
//        this.id = id;
//    }
//
//    public String get() {
//        return this.id;
//    }
//}
