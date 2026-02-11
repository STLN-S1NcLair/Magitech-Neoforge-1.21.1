package net.stln.magitech.api.mana.flow.network.manager;

import net.stln.magitech.api.mana.flow.network.NetworkSnapshot;

import java.util.Random;

public class ManaNetworkInstance {

    private NetworkSnapshot snapshot;
    private boolean dirty;
    private int tickCounter = 0;

    public ManaNetworkInstance(NetworkSnapshot snapshot) {
        this.snapshot = snapshot;
        this.tickCounter = new Random().nextInt(100);
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

    public void tick() {
        tickCounter++;
        // ネットワークの定期更新処理
        if (tickCounter > 100) {
            tickCounter = 0;
            this.markDirty();
        }
    }
}
