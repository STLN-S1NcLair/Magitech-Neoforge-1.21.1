package net.stln.magitech.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.util.EnergyFormatter;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum ManaContainerJadeProvider implements IBlockComponentProvider {
    INSTANCE;

    public static final ResourceLocation UID = Magitech.id("mana_container");
    // 注: ブロックアトラスにあるテクスチャを指定すること
    public static final ResourceLocation TEXTURE = Magitech.id("mana");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        // サーバーから送られてきたデータがあるか確認
        // (まだ届いていない一瞬の間は false になることがある)
        if (accessor.getServerData().contains("mana")) {
            CompoundTag data = accessor.getServerData();

            long mana = data.getLong("mana");
            long maxMana = data.getLong("maxMana");

            // ゼロ除算対策
            float progress = maxMana > 0 ? (float) mana / maxMana : 0;

            MutableComponent text = EnergyFormatter.formatEnergy(mana, maxMana);

            tooltip.add(new TexturedProgressElement(
                    progress,
                    text,
                    TEXTURE
            ));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}