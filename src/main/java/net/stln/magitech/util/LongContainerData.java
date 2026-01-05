package net.stln.magitech.util;

import net.minecraft.world.inventory.ContainerData;

/**
 * long型のデータを扱うためのContainerDataラッパー。
 * 1つのlong値を2つのintスロット（下位ビット、上位ビット）に分割して管理します。
 */
public abstract class LongContainerData implements ContainerData {

    /**
     * 指定されたインデックスのlong値を取得します。
     *
     * @param index longとしてのインデックス (0, 1, 2...)
     */
    public abstract long getLong(int index);

    /**
     * 指定されたインデックスにlong値を設定します。
     *
     * @param index longとしてのインデックス
     * @param value 設定する値
     */
    public abstract void setLong(int index, long value);

    /**
     * 管理するlong変数の個数を返します。
     */
    public abstract int getLongCount();

    // --- 以下、ContainerData (intベース) の実装 ---

    @Override
    public final int get(int index) {
        int longIndex = index / 2; // intスロット2つでlong1つ分
        long value = getLong(longIndex);

        if (index % 2 == 0) {
            // 偶数インデックス: 下位32bit
            return (int) (value & 0xFFFFFFFFL);
        } else {
            // 奇数インデックス: 上位32bit
            return (int) (value >>> 32);
        }
    }

    @Override
    public final void set(int index, int value) {
        int longIndex = index / 2;
        long currentValue = getLong(longIndex);

        if (index % 2 == 0) {
            // 下位32bitの更新: 上位ビットは現在の値を維持し、下位を書き換え
            long upper = currentValue & 0xFFFFFFFF00000000L;
            long lower = Integer.toUnsignedLong(value);
            setLong(longIndex, upper | lower);
        } else {
            // 上位32bitの更新: 下位ビットは現在の値を維持し、上位を書き換え
            long lower = currentValue & 0xFFFFFFFFL;
            long upper = (long) value << 32;
            setLong(longIndex, upper | lower);
        }
    }

    @Override
    public final int getCount() {
        // longの個数 × 2 が intスロットの総数
        return getLongCount() * 2;
    }
}