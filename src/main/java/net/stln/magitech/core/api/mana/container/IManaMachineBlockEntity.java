package net.stln.magitech.core.api.mana.container;

/**
 * マナの生成・消費を行う機械ブロックエンティティの API です。
 * API for machine block entities that produce and consume mana.
 */
public interface IManaMachineBlockEntity extends IManaContainerBlockEntity {

    /**
     * 現在までに生成したマナ量を返します。
     * Returns the amount of mana produced so far.
     */
    long getProducedMana();

    /**
     * 現在までに消費したマナ量を返します。
     * Returns the amount of mana consumed so far.
     */
    long getConsumedMana();

    /**
     * 生成マナの累計に量を加算します。
     * Adds an amount to the produced-mana total.
     */
    void addProducedMana(long amount);

    /**
     * 消費マナの累計に量を加算します。
     * Adds an amount to the consumed-mana total.
     */
    void addConsumedMana(long amount);
}
