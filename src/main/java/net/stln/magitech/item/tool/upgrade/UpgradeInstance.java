package net.stln.magitech.item.tool.upgrade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class UpgradeInstance {

    public static final Codec<UpgradeInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("level").forGetter((upgradeInstance) -> upgradeInstance.level),
                    ResourceLocation.CODEC.fieldOf("upgrade").forGetter((upgradeInstance) -> UpgradeRegister.getId(upgradeInstance.upgrade))
            ).apply(instance, UpgradeInstance::new)
    );

    public static final StreamCodec<ByteBuf, UpgradeInstance> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpgradeInstance::getLevel,
            ResourceLocation.STREAM_CODEC, (instance) -> UpgradeRegister.getId(instance.upgrade),
            UpgradeInstance::new
    );
    public int level;
    public Upgrade upgrade;

    public UpgradeInstance(int level, Upgrade upgrade) {
        this.level = level;
        this.upgrade = upgrade;
    }
    public UpgradeInstance(int level, ResourceLocation upgrade) {
        this.level = level;
        this.upgrade = UpgradeRegister.getUpgradeFromAll(upgrade);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpgradeInstance)) return false;
        UpgradeInstance other = (UpgradeInstance) o;
        return upgrade.equals(other.upgrade) && level == other.level;
    }

    @Override
    public int hashCode() {
        return upgrade.hashCode();
    }
}
