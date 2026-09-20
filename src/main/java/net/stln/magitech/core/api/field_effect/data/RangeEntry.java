package net.stln.magitech.core.api.field_effect.data;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;

/**
 * 直方体のフィールド効果範囲と、その発生源・影響を表します。
 * Represents a cuboid field-effect range, its source, and its influences.
 */
public record RangeEntry(BlockPos from, BlockPos to, BlockPos source, FieldInfluenceInstance instance) {
    /**
     * 保存形式用の Codec です。
     * Codec used for persistent data serialization.
     */
    public static final Codec<RangeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("from").forGetter(RangeEntry::from),
            BlockPos.CODEC.fieldOf("to").forGetter(RangeEntry::to),
            BlockPos.CODEC.fieldOf("source").forGetter(RangeEntry::source),
            FieldInfluenceInstance.CODEC.fieldOf("instance").forGetter(RangeEntry::instance)
    ).apply(instance, RangeEntry::new));

    /**
     * ネットワーク同期用の StreamCodec です。
     * StreamCodec used for network synchronization.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, RangeEntry> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            RangeEntry::from,
            BlockPos.STREAM_CODEC,
            RangeEntry::to,
            BlockPos.STREAM_CODEC,
            RangeEntry::source,
            FieldInfluenceInstance.STREAM_CODEC,
            RangeEntry::instance,
            RangeEntry::new
    );

    /**
     * 指定位置がこの範囲に含まれるか判定します。
     * Determines whether a position is contained in this range.
     */
    public boolean contains(BlockPos pos) {
        return pos.getX() >= from.getX() && pos.getX() <= to.getX()
                && pos.getY() >= from.getY() && pos.getY() <= to.getY()
                && pos.getZ() >= from.getZ() && pos.getZ() <= to.getZ();
    }
}
