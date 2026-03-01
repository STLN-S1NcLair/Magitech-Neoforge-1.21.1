package net.stln.magitech.content.item.component;


import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ManaContainerComponent(long mana, long maxMana, long maxFlow) {
    public static final Codec<ManaContainerComponent> CODEC = Codec.LONG.listOf(3, 3).xmap(
            list -> new ManaContainerComponent(list.get(0), list.get(1), list.get(2)),
            component -> java.util.List.of(component.mana, component.maxMana, component.maxFlow)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ManaContainerComponent> STREAM_CODEC = StreamCodec.of(
            (buf, component) -> {
                buf.writeLong(component.mana);
                buf.writeLong(component.maxMana);
                buf.writeLong(component.maxFlow);
            },
            buf -> new ManaContainerComponent(
                    buf.readLong(),
                    buf.readLong(),
                    buf.readLong()
            )
    );
}

