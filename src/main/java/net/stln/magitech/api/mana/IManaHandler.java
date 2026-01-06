package net.stln.magitech.api.mana;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;

public interface IManaHandler {

    static long transferMana(@Nullable IManaHandler from, @Nullable IManaHandler to, long amount) {

        // 両方存在すれば転送処理
        if (from != null && to != null) {

            // シミュレーション: どれだけ抜けるか？
            long extracted = from.extractMana(amount, true);
            // シミュレーション: どれだけ入るか？
            long accepted = to.receiveMana(extracted, true);

            // 実行
            long actualTransfer = accepted;
            if (actualTransfer > 0) {
                from.extractMana(actualTransfer, false); // 実際に減らす (内部で書き換え)
                to.receiveMana(actualTransfer, false); // 実際に増やす (内部で書き換え)
            }
            return actualTransfer;
        }
        return 0;
    }

    // できる限り多く転送
    static long transferMana(@Nullable IManaHandler from, @Nullable IManaHandler to) {

        // 両方存在すれば転送処理
        if (from != null && to != null) {

            // シミュレーション: どれだけ抜けるか？
            long extracted = from.extractMana(Long.MAX_VALUE, true);
            // シミュレーション: どれだけ入るか？
            long accepted = to.receiveMana(extracted, true);

            // 実行
            long actualTransfer = accepted;
            if (actualTransfer > 0) {
                from.extractMana(actualTransfer, false); // 実際に減らす (内部で書き換え)
                to.receiveMana(actualTransfer, false); // 実際に増やす (内部で書き換え)
            }
            return actualTransfer;
        }
        return 0;
    }

    long getMana();

    void setMana(long mana);

    long getMaxMana();

    long getPrevMana();   // 前回の量

    long getMaxFlow();    // 1tickあたりの最大転送量

    // マナを受け取る
    long receiveMana(long maxReceive, boolean simulate);

    // マナを取り出す
    long extractMana(long maxExtract, boolean simulate);

    // 制限を無視してやり取りする
    default void addMana(long amount) {
        setMana(getMana() + amount);
    }

    // 制限を無視してやり取りする
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
