package net.stln.magitech.content.network;

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
import net.stln.magitech.helper.VectorHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.UUID;

public record TraitEntityKillVFXPayload(Vector3f pos, UUID uuid,
                                        Trait trait) implements CustomPacketPayload {
    public static final ResourceLocation PAYLOAD_ID = Magitech.id("trait_entity_kill_vfx");
    public static final Type<TraitEntityKillVFXPayload> TYPE = new Type<>(PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, TraitEntityKillVFXPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            TraitEntityKillVFXPayload::pos,
            UUIDUtil.STREAM_CODEC,
            TraitEntityKillVFXPayload::uuid,
            Trait.STREAM_CODEC,
            TraitEntityKillVFXPayload::trait,
            TraitEntityKillVFXPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
