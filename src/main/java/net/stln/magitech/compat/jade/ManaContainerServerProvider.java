package net.stln.magitech.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.mana.IBlockManaHandler;
import net.stln.magitech.block.block_entity.ManaContainerBlockEntity;
import net.stln.magitech.block.block_entity.ManaNodeBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum ManaContainerServerProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return Magitech.id("mana_container");
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        // サーバー側でBlockEntityを取得
        if (accessor.getBlockEntity() instanceof ManaContainerBlockEntity container) {
            // 現在の値をNBTに保存
            data.putLong("mana", container.getMana());
            data.putLong("maxMana", container.getMaxMana());
        }
    }
}
