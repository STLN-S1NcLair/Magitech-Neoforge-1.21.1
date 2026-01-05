package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.api.mana.IBlockManaHandler;
import net.stln.magitech.api.mana.IManaHandler;
import net.stln.magitech.api.mana.IManaNode;
import net.stln.magitech.api.mana.ManaNodeLogicHelper;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.ManaNodeBlock;
import net.stln.magitech.network.ManaNodeTransferPayload;
import net.stln.magitech.sound.SoundInit;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ManaNodeBlockEntity extends BlockEntity implements IManaNode {

    private final ManaNodeLogicHelper nodeLogic;

    public ManaNodeBlockEntity(BlockEntityType type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.nodeLogic = new ManaNodeLogicHelper(this, null);
    }

    public ManaNodeBlockEntity(BlockPos pos, BlockState blockState) {
        this(BlockInit.MANA_NODE_ENTITY.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaNodeBlockEntity entity) {
        entity.nodeLogic.tick(level, pos, state);
    }

    @Override
    public void requestRescan() {
        this.nodeLogic.requestRescan();
    }
}