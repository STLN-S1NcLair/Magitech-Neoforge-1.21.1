package net.stln.magitech.feature.tool.upgrade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;

import java.util.List;

public record UpgradeInstance(int level, Upgrade upgrade) {
    public static final Codec<UpgradeInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("level").forGetter((upgradeInstance) -> upgradeInstance.level),
                    ResourceLocation.CODEC.fieldOf("upgrade").forGetter((upgradeInstance) -> UpgradeRegister.getId(upgradeInstance.upgrade))
            ).apply(instance, UpgradeInstance::new)
    );

    public static final StreamCodec<ByteBuf, UpgradeInstance> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpgradeInstance::level,
            ResourceLocation.STREAM_CODEC, (instance) -> UpgradeRegister.getId(instance.upgrade),
            UpgradeInstance::new
    );

    public List<ToolPropertyModifier> getModifiers() {
        return upgrade().getModifiers(level());
    }

    public UpgradeInstance(int level, ResourceLocation upgrade) {
        this(level, UpgradeRegister.getUpgradeFromAll(upgrade));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpgradeInstance(int level1, Upgrade upgrade1))) return false;
        return upgrade.equals(upgrade1) && level == level1;
    }

    @Override
    public int hashCode() {
        return upgrade.hashCode();
    }
}
