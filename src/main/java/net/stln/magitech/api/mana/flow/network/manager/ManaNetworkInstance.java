package net.stln.magitech.api.mana.flow.network.manager;

import net.stln.magitech.api.mana.flow.network.NetworkSnapshot;

public class ManaNetworkInstance {

    private NetworkSnapshot snapshot;
    private boolean dirty;

    public ManaNetworkInstance(NetworkSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public NetworkSnapshot getSnapshot() {
        return snapshot;
    }

    public void markDirty() {
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void update(NetworkSnapshot snapshot) {
        this.snapshot = snapshot;
        this.dirty = false;
    }
}
