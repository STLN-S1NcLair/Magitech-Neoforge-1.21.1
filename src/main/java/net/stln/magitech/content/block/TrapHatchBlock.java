package net.stln.magitech.content.block;

import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.helper.CombatHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrapHatchBlock extends Block {

    public static final BooleanProperty OPENED = BooleanProperty.create("opened");
    public static final VoxelShape CLOSED_SHAPE = Block.box(0, 13, 0, 16, 16, 16);
    private static final VoxelShape OPENED_SHAPE = Shapes.or(Block.box(0, 8, 0, 3, 16, 16), Block.box(13, 8, 0, 16, 16, 16));
    private static final int LINK_RANGE = 32;
    private static final int HOLD_TICKS = 40;
    private static final int IDLE_SCAN_TICKS = 1;

    // 開放期限(ゲーム時刻)を保持して、古い scheduled tick で早閉じしないようにする。
    private static final Map<GlobalPos, Long> OPEN_UNTIL = new HashMap<>();

    public TrapHatchBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPENED, Boolean.FALSE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // 破壊・選択用の形状。
        if (state.getValue(OPENED)) {
            return OPENED_SHAPE;
        }
        return CLOSED_SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        // ヒット判定も同じ形に合わせる。
        if (state.getValue(OPENED)) {
            return OPENED_SHAPE;
        }
        return CLOSED_SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // 開いているときは物理衝突なし、閉じているときは上部でぶつかる。
        if (state.getValue(OPENED)) {
            return Shapes.empty();
        }
        return CLOSED_SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        BlockState current = level.getBlockState(pos);
        if (!current.is(this)) {
            OPEN_UNTIL.remove(globalKey(level, pos));
            return;
        }

        if (hasTriggerEntity(level, pos)) {
            triggerLinkedHatches(level, pos);
            return;
        }

        GlobalPos key = globalKey(level, pos);
        long now = level.getGameTime();
        long until = OPEN_UNTIL.getOrDefault(key, 0L);

        if (until > now) {
            level.scheduleTick(pos, this, (int) Math.max(1L, until - now));
            return;
        }

        if (current.getValue(OPENED)) {
            setOpened(level, pos, current, false);
        }

        OPEN_UNTIL.remove(key);
        level.scheduleTick(pos, this, IDLE_SCAN_TICKS);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        // 設置後は低頻度スキャンで反応待ちする。
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, IDLE_SCAN_TICKS);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            OPEN_UNTIL.remove(globalKey(level, pos));
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private boolean hasTriggerEntity(ServerLevel level, BlockPos pos) {
        List<Entity> entities = CombatHelper.getEntitiesInBox(level, null, pos.getBottomCenter().add(0, 2, 0), new Vec3(1, 2, 1));
        return !entities.isEmpty();
    }

    private void triggerLinkedHatches(ServerLevel level, BlockPos origin) {
        long openUntil = level.getGameTime() + HOLD_TICKS;
        for (int dx = -LINK_RANGE; dx <= LINK_RANGE; dx++) {
            for (int dz = -LINK_RANGE; dz <= LINK_RANGE; dz++) {
                BlockPos targetPos = origin.offset(dx, 0, dz);
                BlockState targetState = level.getBlockState(targetPos);
                if (!targetState.is(this)) {
                    continue;
                }

                OPEN_UNTIL.put(globalKey(level, targetPos), openUntil);

                if (!targetState.getValue(OPENED)) {
                    setOpened(level, targetPos, targetState, true);
                    fallBlock(level, targetPos.above());
                    fallBlock(level, targetPos.above(2));
                }

                level.scheduleTick(targetPos, this, HOLD_TICKS);
            }
        }
    }

    private void setOpened(ServerLevel level, BlockPos pos, BlockState state, boolean opened) {
        if (state.getValue(OPENED) == opened) {
            return;
        }

        level.setBlockAndUpdate(pos, state.setValue(OPENED, opened));
        SoundHelper.broadcastSound(level, pos.getCenter(),
                opened ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE,
                SoundSource.BLOCKS);
    }

    private static GlobalPos globalKey(Level level, BlockPos pos) {
        return GlobalPos.of(level.dimension(), pos.immutable());
    }

    private static void fallBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(level, pos) <= 0) return;
        FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, pos, state);
        level.addFreshEntity(fallingblockentity);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPENED);
    }
}
