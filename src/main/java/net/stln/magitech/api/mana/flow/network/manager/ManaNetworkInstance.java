package net.stln.magitech.api.mana.flow.network.manager;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.api.mana.flow.ManaVisualEffectHelper;
import net.stln.magitech.api.mana.flow.network.HandlerEndpoint;
import net.stln.magitech.api.mana.flow.network.NetworkSnapshot;
import net.stln.magitech.api.mana.flow.network.NetworkTree;
import net.stln.magitech.api.mana.flow.network.NetworkTreeHelper;
import net.stln.magitech.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.api.mana.handler.IManaHandler;
import net.stln.magitech.network.ManaNodeTransferPayload;
import net.stln.magitech.sound.SoundInit;

import java.util.*;
import java.util.stream.Collectors;

public class ManaNetworkInstance {

    private NetworkSnapshot snapshot;
    private boolean dirty;
    private int tickCounter = 0;

    public ManaNetworkInstance(NetworkSnapshot snapshot) {
        this.snapshot = snapshot;
        this.tickCounter = new Random().nextInt(120);
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

    public void tick(Level level) {
        tickCounter++;
        balance(level);
        // ネットワークの定期更新処理
        if (tickCounter > 120) {
            tickCounter = 0;
            this.markDirty();
        }
    }

    private void balance(Level level) {
        // ネットワーク内のマナの流れを計算して、各端点にマナを供給する処理
        Map<IBlockManaHandler, HandlerEndpoint> handlerToEndpoint = snapshot.endpoints().stream()
                .map(endpoint -> {
                    // ブロック位置からIManaHandlerを取得する処理
                    IBlockManaHandler handler = ManaTransferHelper.getManaContainer(level, endpoint.pos(), endpoint.direction());

                    return handler != null ? Map.entry(handler, endpoint) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Set<IBasicManaHandler> handlers = snapshot.endpoints().stream()
                .map(endpoint -> {
                    // ブロック位置からIManaHandlerを取得する処理
                    IBlockManaHandler handler = ManaTransferHelper.getManaContainer(level, endpoint.pos(), endpoint.direction());

                    return handler != null ? handler : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (HandlerEndpoint endpoint : snapshot.endpoints()) {

            IBlockManaHandler handler = ManaTransferHelper.getManaContainer(level, endpoint.pos(), endpoint.direction());

            if (handler == null) continue;

            Set<IBasicManaHandler> inserted = ManaTransferHelper.balance(handler, handlers);

            for (HandlerEndpoint target : snapshot.endpoints()) {
                IBlockManaHandler h = ManaTransferHelper.getManaContainer(level, target.pos(), target.direction());
                if (inserted.contains(h)) {
                    Set<NetworkTree.Edge> path =
                            NetworkTreeHelper.getWirelessPath(snapshot.networkTree(), endpoint.pos(), target.pos());
                    ManaVisualEffectHelper.spawnPathParticles(level, endpoint.pos(), target.pos(), path, this.tickCounter);
                }
            }
        }
    }
}
