package net.stln.magitech.api.mana;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IManaNode {
    /**
     * 周囲の接続状況を再確認するよう要求する
     */
    void requestRescan();

    /**
     * 自身をBlockEntityとして取得するヘルパー
     * (default実装しておくとキャストの手間が省けて便利です)
     */
    default BlockEntity getBlockEntity() {
        return (BlockEntity) this;
    }
}
