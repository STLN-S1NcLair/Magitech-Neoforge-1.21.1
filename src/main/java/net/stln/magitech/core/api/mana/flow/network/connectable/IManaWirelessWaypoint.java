package net.stln.magitech.core.api.mana.flow.network.connectable;

/**
 * 無線接続に対応するマナネットワーク中継点の API です。
 * API for mana-network waypoints that support wireless connections.
 */
public interface IManaWirelessWaypoint extends IManaWaypoint {
    /**
     * 無線接続の到達範囲をブロック単位で返します。
     * Returns the wireless connection range in blocks.
     */
    int getRange();
}
