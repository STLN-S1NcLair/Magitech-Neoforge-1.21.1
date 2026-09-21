package net.stln.magitech.content.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

public record FieldEffectProcessingVFXPayload(
        Target target,
        BlockPos blockPos,
        int entityId,
        int primaryColor,
        int secondaryColor
) implements CustomPacketPayload {
    public static final ResourceLocation ID = Magitech.id("field_effect_processing_vfx");
    public static final Type<FieldEffectProcessingVFXPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<ByteBuf, FieldEffectProcessingVFXPayload> STREAM_CODEC = StreamCodec.composite(
            Target.STREAM_CODEC,
            FieldEffectProcessingVFXPayload::target,
            BlockPos.STREAM_CODEC,
            FieldEffectProcessingVFXPayload::blockPos,
            ByteBufCodecs.VAR_INT,
            FieldEffectProcessingVFXPayload::entityId,
            ByteBufCodecs.INT,
            FieldEffectProcessingVFXPayload::primaryColor,
            ByteBufCodecs.INT,
            FieldEffectProcessingVFXPayload::secondaryColor,
            FieldEffectProcessingVFXPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Target {
        BLOCK(0),
        ITEM(1),
        PEDESTAL(2);

        public static final StreamCodec<ByteBuf, Target> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(
                Target::byId,
                target -> target.id
        );

        private final int id;

        Target(int id) {
            this.id = id;
        }

        private static Target byId(int id) {
            for (Target target : values()) {
                if (target.id == id) {
                    return target;
                }
            }
            return BLOCK;
        }
    }
}
