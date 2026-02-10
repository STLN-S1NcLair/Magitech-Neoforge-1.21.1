package net.stln.magitech.api.mana.flow;

public interface IManaNode {
    /**
     * 周囲の接続状況を再確認するよう要求する
     */
    void requestRescan();
}
