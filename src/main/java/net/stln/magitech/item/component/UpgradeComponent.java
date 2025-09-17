package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.upgrade.UpgradeInstance;
import net.stln.magitech.item.tool.upgrade.UpgradeRegister;

import java.util.List;

public record UpgradeComponent(List<UpgradeInstance> upgrades) {

    public static final Codec<UpgradeComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UpgradeInstance.CODEC.listOf().fieldOf("upgrades").forGetter(UpgradeComponent::upgrades)
            ).apply(instance, UpgradeComponent::new)
    );
    public static final StreamCodec<ByteBuf, UpgradeComponent> STREAM_CODEC = UpgradeInstance.STREAM_CODEC.apply(ByteBufCodecs.list()).map(
            UpgradeComponent::new, UpgradeComponent::upgrades
    );

    public List<UpgradeInstance> getUpgrades() {
        return upgrades;
    }

    public List<ResourceLocation> getUpgradeIds() {
        List<ResourceLocation> ids = upgrades.stream().map((upgradeInstance1 -> UpgradeRegister.getId(upgradeInstance1.upgrade))).toList();
        return ids;
    }

    public UpgradeComponent addUpgrade(UpgradeInstance upgrade) {
        List<UpgradeInstance> oldUpgrades = new java.util.ArrayList<>(List.copyOf(upgrades));
        List<UpgradeInstance> newUpgrades = new java.util.ArrayList<>();
        int level = upgrade.level;
        for (int i = 0; i < oldUpgrades.size(); i++) {
            if (oldUpgrades.get(i).upgrade.equals(upgrade.upgrade)) {
                level += oldUpgrades.get(i).level;
            } else {
                newUpgrades.add(oldUpgrades.get(i));
            }
        }
        newUpgrades.add(new UpgradeInstance(level, upgrade.upgrade));

        return new UpgradeComponent(newUpgrades);
    }
}

