package net.stln.magitech.api.mana.flow;


import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.api.mana.flow.network.ConnectionMode;
import net.stln.magitech.api.mana.flow.network.NetworkTree;
import net.stln.magitech.network.ManaNodeTransferPayload;
import net.stln.magitech.sound.SoundInit;

import java.util.*;
import java.util.stream.Collectors;

public class ManaVisualEffectHelper {

    public static void spawnPathParticles(Level level, BlockPos start, BlockPos end, Set<NetworkTree.Edge> path, int tickCounter) {
        for (NetworkTree.Edge edge : path) {
            BlockPos parent = edge.parent();
            BlockPos child = edge.child();
            if (level.random.nextFloat() < 0.5f) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) level,
                        new ChunkPos(parent),
                        new ManaNodeTransferPayload(parent, child)
                );
            }
        }
        if (tickCounter % 30 == 0) {
            Vec3 midPoint = Vec3.atCenterOf(start).add(Vec3.atCenterOf(end)).scale(0.5);
            level.playSound(null, midPoint.x, midPoint.y, midPoint.z, SoundInit.MANA_NODE.get(), SoundSource.BLOCKS, 0.3F, 1.0F);
        }
    }
}
