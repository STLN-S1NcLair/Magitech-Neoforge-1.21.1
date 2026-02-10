package net.stln.magitech.api.mana.handler;

import java.math.BigDecimal;
import java.math.MathContext;

public interface IManaHandler {

    long getMana();

    void setMana(long mana);

    long getMaxMana();

    long getMaxFlow();    // 1tickあたりの最大転送量

    // マナを受け取る
    long insertMana(long maxInsert, boolean simulate);

    // マナを取り出す
    long extractMana(long maxExtract, boolean simulate);

    // 制限を無視してやり取りする
    default void addMana(long amount) {
        setMana(getMana() + amount);
    }

    default boolean isFull() {
        return getMana() == getMaxMana();
    }

    default double fillRatio() {
        long mana = getMana();
        long maxMana = getMaxMana();
        if (maxMana == 0) return 0.0d;

        // longをBigDecimalに変換して割り算
        // MathContext.DECIMAL64 は double 相当の精度で計算結果を丸めます
        return BigDecimal.valueOf(mana)
                .divide(BigDecimal.valueOf(maxMana), MathContext.DECIMAL64)
                .doubleValue();
    }
}
