package net.stln.magitech.core.api.mana.handler;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * マナの保存・入出力を行う基本ハンドラーの API です。
 * Base API for storing, inserting, and extracting mana.
 */
public interface IManaHandler {

    /**
     * 現在のマナ量を返します。
     * Returns the current mana amount.
     */
    long getMana();

    /**
     * マナ量を設定します。
     * Sets the mana amount.
     */
    void setMana(long mana);

    /**
     * 最大マナ容量を返します。
     * Returns the maximum mana capacity.
     */
    long getMaxMana();

    /**
     * 1 tick あたりの最大転送量を返します。
     * Returns the maximum transfer amount per tick.
     */
    long getMaxFlow();    // 1tickあたりの最大転送量 / Maximum transfer amount per tick

    /**
     * マナの挿入可能量を返し、実行時は挿入します。
     * Returns the insertable amount and inserts it when not simulating.
     */
    long insertMana(long maxInsert, boolean simulate);

    /**
     * マナの排出可能量を返し、実行時は排出します。
     * Returns the extractable amount and extracts it when not simulating.
     */
    long extractMana(long maxExtract, boolean simulate);

    /**
     * 流量制限を無視してマナ量を変更します。
     * Changes the mana amount while ignoring flow limits.
     */
    default void addMana(long amount) {
        long current = getMana();
        long next;
        try {
            next = Math.addExact(current, amount);
        } catch (ArithmeticException e) {
            next = amount >= 0 ? Long.MAX_VALUE : Long.MIN_VALUE;
        }
        setMana(next);
    }

    /**
     * マナが最大容量に達しているか判定します。
     * Determines whether the mana is at maximum capacity.
     */
    default boolean isFull() {
        return getMana() == getMaxMana();
    }

    /**
     * 現在の充填率を計算します。
     * Calculates the current fill ratio.
     */
    default double fillRatio() {
        long mana = getMana();
        long maxMana = getMaxMana();
        if (maxMana == 0) return 0.0d;

        // longをBigDecimalに変換して割り算 / Converts long values to BigDecimal before division
        // MathContext.DECIMAL64 は double 相当の精度で計算結果を丸めます / MathContext.DECIMAL64 rounds the result with precision comparable to double
        return BigDecimal.valueOf(mana)
                .divide(BigDecimal.valueOf(maxMana), MathContext.DECIMAL64)
                .doubleValue();
    }
}
