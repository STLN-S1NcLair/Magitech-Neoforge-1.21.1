package net.stln.magitech.api.mana.flow.network.connectable;

public interface IManaWirelessWaypoint extends IManaWaypoint {
    int getRange();

    int maxWirelessConnections();
}
