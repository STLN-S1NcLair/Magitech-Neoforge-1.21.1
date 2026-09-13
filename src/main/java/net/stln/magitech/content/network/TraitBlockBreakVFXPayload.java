package net.stln.magitech.content.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.trait.Trait;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record TraitBlockBreakVFXPayload(BlockPos pos, UUID uuid,
                                        Trait trait) implements CustomPacketPayload {
    public static final ResourceLocation PAYLOAD_ID = Magitech.id("trait_block_break_vfx");
    public static final Type<TraitBlockBreakVFXPayload> TYPE = new Type<>(PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, TraitBlockBreakVFXPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            TraitBlockBreakVFXPayload::pos,
            UUIDUtil.STREAM_CODEC,
            TraitBlockBreakVFXPayload::uuid,
            Trait.STREAM_CODEC,
            TraitBlockBreakVFXPayload::trait,
            TraitBlockBreakVFXPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
