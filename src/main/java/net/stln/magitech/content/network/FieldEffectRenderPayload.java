package net.stln.magitech.content.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.field_effect.data.RangeEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record FieldEffectRenderPayload(Action action, Optional<BlockPos> source, List<RangeEntry> ranges) implements CustomPacketPayload {
    public static final ResourceLocation PAYLOAD_ID = Magitech.id("field_effect_render_payload");
    public static final Type<FieldEffectRenderPayload> TYPE = new Type<>(PAYLOAD_ID);

    public FieldEffectRenderPayload {
        source = Objects.requireNonNullElse(source, Optional.empty());
        ranges = List.copyOf(ranges == null ? List.of() : ranges);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, FieldEffectRenderPayload> STREAM_CODEC = StreamCodec.composite(
            Action.STREAM_CODEC,
            FieldEffectRenderPayload::action,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC),
            FieldEffectRenderPayload::source,
            RangeEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FieldEffectRenderPayload::ranges,
            FieldEffectRenderPayload::new
    );

    public static FieldEffectRenderPayload clearAll() {
        return new FieldEffectRenderPayload(Action.CLEAR, Optional.empty(), List.of());
    }

    public static FieldEffectRenderPayload fullSync(List<RangeEntry> ranges) {
        return new FieldEffectRenderPayload(Action.FULL_SYNC, Optional.empty(), ranges);
    }

    public static FieldEffectRenderPayload upsertSource(BlockPos source, List<RangeEntry> ranges) {
        return new FieldEffectRenderPayload(Action.UPSERT_SOURCE, Optional.of(source), ranges);
    }

    public static FieldEffectRenderPayload removeSource(BlockPos source) {
        return new FieldEffectRenderPayload(Action.REMOVE_SOURCE, Optional.of(source), List.of());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Action {
        CLEAR("clear"),
        FULL_SYNC("full_sync"),
        UPSERT_SOURCE("upsert_source"),
        REMOVE_SOURCE("remove_source");

        public static final StreamCodec<RegistryFriendlyByteBuf, Action> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public @NotNull Action decode(RegistryFriendlyByteBuf buffer) {
                return Action.byName(buffer.readUtf());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, Action value) {
                buffer.writeUtf(value.id);
            }
        };

        private final String id;

        Action(String id) {
            this.id = id;
        }

        public static Action byName(String id) {
            for (Action value : values()) {
                if (value.id.equals(id)) {
                    return value;
                }
            }
            return FULL_SYNC;
        }
    }
}
