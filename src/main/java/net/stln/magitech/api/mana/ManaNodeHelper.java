package net.stln.magitech.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.network.ManaNodeTransferPayload;
import net.stln.magitech.sound.SoundInit;

import java.util.*;

public class ManaNodeHelper {
    /**
     * 経路リストに従って、各区間ごとにパケットを送信する
     */
    private static void spawnPathParticles(Level level, List<BlockPos> path, int tickCount) {
        // パスは [Start, Relay1, Relay2, ..., End] の順
        for (int i = 0; i < path.size() - 1; i++) {
            BlockPos from = path.get(i);
            BlockPos to = path.get(i + 1);
                if (level.random.nextFloat() < 0.5f) {
            PacketDistributor.sendToPlayersTrackingChunk(
                    (ServerLevel) level,
                    new ChunkPos(from),
                    new ManaNodeTransferPayload(from, to)
            );
        }
        }
        if (tickCount % 30 == 0) {
            Vec3 midPoint = Vec3.atCenterOf(path.getFirst()).add(Vec3.atCenterOf(path.getLast())).scale(0.5);
            level.playSound(null, midPoint.x, midPoint.y, midPoint.z, SoundInit.MANA_NODE.get(), SoundSource.BLOCKS, 0.3F, 1.0F);
        }
    }
}