package net.stln.magitech.api.mana;

public interface IManaContainerBlockEntity {

    // Handlerに依存せずに共通で使うもの: マナ量、流量

    long getMana();

    long getMaxMana();

    long getMaxFlow();

    // 1tickあたりの現在の転送量を取得
    long getCurrentTickTransfer();

    void setMana(long mana);

    void setMaxMana(long maxMana);

    void setMaxFlow(long maxFlow);

    void setCurrentTickTransfer(long value);
}
