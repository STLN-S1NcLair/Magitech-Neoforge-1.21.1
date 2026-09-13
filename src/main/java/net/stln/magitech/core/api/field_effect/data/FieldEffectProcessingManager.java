package net.stln.magitech.core.api.field_effect.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.content.block.block_entity.IPedestalBlockEntity;
import net.stln.magitech.core.api.field_effect.FieldEffectHelper;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * フィールド効果の対象ごとの処理進行度を管理します。
 * Manages per-target processing progress for field effects.
 */
public final class FieldEffectProcessingManager {
    private static final int ITEM_ENTITY_CLEANUP_INTERVAL = 20;

    private static final Map<ServerLevel, FieldEffectProcessingManager> INSTANCES = new WeakHashMap<>();

    private final Map<BlockPos, ProcessState> blockProgress = new HashMap<>();
    private final Map<BlockPos, ProcessState> pedestalProgress = new HashMap<>();
    private final Map<UUID, ProcessState> itemEntityProgress = new HashMap<>();

    private long tickCount;

    private FieldEffectProcessingManager() {
    }

    /**
     * ワールドに対応する処理進行度管理インスタンスを取得します。
     * Gets the processing-progress manager associated with a level.
     */
    public static FieldEffectProcessingManager get(ServerLevel level) {
        return INSTANCES.computeIfAbsent(level, ignored -> new FieldEffectProcessingManager());
    }

    /**
     * ワールド tick に合わせてフィールド効果の処理対象を更新します。
     * Updates field-effect processing targets for a world tick.
     */
    public void tick(ServerLevel level) {
        tickCount++;

        FieldEffectManager fieldEffectManager = FieldEffectManager.get(level);
        Set<BlockPos> seenBlocks = new HashSet<>();
        Set<BlockPos> seenPedestalItems = new HashSet<>();
        Map<UUID, Entity> seenEntities = new HashMap<>();

        fieldEffectManager.forEachPosInRanges(level, (pos, instance) -> {
            FieldEffectType type = FieldEffectHelper.getFieldEffect(level, instance);
            if (type == null) {
                return;
            }

            BlockPos blockPos = pos.immutable();
            seenBlocks.add(blockPos);
            processBlock(level, blockPos, type);
            collectPedestalItems(level, fieldEffectManager, blockPos, seenPedestalItems);

            level.getEntities(null, new AABB(blockPos)).forEach(entity ->
                    seenEntities.putIfAbsent(entity.getUUID(), entity)
            );
        });

        for (Entity entity : seenEntities.values()) {
            BlockPos entityPos = entity.blockPosition().immutable();
            FieldInfluenceInstance instance = fieldEffectManager.getFieldEffectInstance(entityPos);
            FieldEffectType type = FieldEffectHelper.getFieldEffect(level, instance);
            if (type == null) {
                continue;
            }

            type.affectEntity(entity);
            if (entity instanceof ItemEntity itemEntity) {
                processItemEntity(level, itemEntity, type);
            }
        }

        blockProgress.keySet().removeIf(pos -> !seenBlocks.contains(pos));
        pedestalProgress.keySet().removeIf(pos -> !seenPedestalItems.contains(pos));

        if (tickCount % ITEM_ENTITY_CLEANUP_INTERVAL == 0) {
            itemEntityProgress.keySet().removeIf(uuid -> level.getEntity(uuid) == null);
        }
    }

    private void processBlock(ServerLevel level, BlockPos pos, FieldEffectType type) {
        BlockState state = level.getBlockState(pos);
        if (!type.canProcess(level, pos)) {
            blockProgress.remove(pos);
            return;
        }

        int processTime = sanitizeProcessTime(type.getProcessTime(level, pos));
        ProcessState progress = blockProgress.get(pos);
        if (progress == null || !progress.matches(type, processTime, state)) {
            progress = ProcessState.forBlock(type, processTime, state);
            blockProgress.put(pos, progress);
        }

        if (++progress.elapsedTicks >= progress.totalTicks) {
            List<ItemStack> result = type.processBlock(level, pos);
            if (result == null) {
                result = List.of();
            }
            for (ItemStack stack : result) {
                if (stack != null && !stack.isEmpty()) {
                    spawnItem(level, pos, stack);
                }
            }
            blockProgress.remove(pos);
        }
    }

    private void collectPedestalItems(
            ServerLevel level,
            FieldEffectManager fieldEffectManager,
            BlockPos fieldPos,
            Set<BlockPos> seenPedestalItems
    ) {
        processPedestalAt(level, fieldEffectManager, fieldPos.below(), seenPedestalItems);
    }

    private void processPedestalAt(
            ServerLevel level,
            FieldEffectManager fieldEffectManager,
            BlockPos pedestalPos,
            Set<BlockPos> seenPedestalItems
    ) {
        BlockPos anchorPos = pedestalPos.above();
        FieldInfluenceInstance instance = fieldEffectManager.getFieldEffectInstance(anchorPos);
        FieldEffectType type = FieldEffectHelper.getFieldEffect(level, instance);
        if (type == null) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pedestalPos);
        if (!(blockEntity instanceof IPedestalBlockEntity pedestal)) {
            return;
        }

        ItemStackHandler inventory = pedestal.getInventory();
        BlockPos targetPos = pedestalPos.immutable();
        ItemStack input = inventory.getStackInSlot(0);
        if (input.isEmpty()) {
            pedestalProgress.remove(targetPos);
            return;
        }

        seenPedestalItems.add(targetPos);
        processPedestalItem(level, pedestalPos, inventory, input.copy(), type);
    }

    private void processPedestalItem(
            ServerLevel level,
            BlockPos pedestalPos,
            ItemStackHandler inventory,
            ItemStack input,
            FieldEffectType type
    ) {
        List<ItemStack> inputs = List.of(input);
        if (!type.canProcess(level, inputs)) {
            pedestalProgress.remove(pedestalPos);
            return;
        }

        int processTime = sanitizeProcessTime(type.getProcessTime(level, inputs));
        ProcessState progress = pedestalProgress.get(pedestalPos);
        if (progress == null || !progress.matches(type, processTime, inputs)) {
            progress = ProcessState.forItems(type, processTime, inputs);
            pedestalProgress.put(pedestalPos, progress);
        }

        if (++progress.elapsedTicks >= progress.totalTicks) {
            List<ItemStack> results = type.processItem(level, new ArrayList<>(inputs));
            if (results == null) {
                results = List.of();
            }
            writePedestalResult(inventory, results);
            pedestalProgress.remove(pedestalPos);
        }
    }

    private static void writePedestalResult(ItemStackHandler inventory, List<ItemStack> results) {
        ItemStack result = ItemStack.EMPTY;
        for (ItemStack processedStack : results) {
            if (processedStack != null && !processedStack.isEmpty()) {
                result = processedStack.copy();
                break;
            }
        }
        inventory.setStackInSlot(0, result);
    }

    private static void spawnItem(ServerLevel level, BlockPos pos, ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(
                level,
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D,
                stack.copy()
        );
        level.addFreshEntity(itemEntity);
    }

    private static void replaceItem(ServerLevel level, ItemStack stack, ItemEntity input) {
        Vec3 pos = input.position();
        ItemEntity itemEntity = new ItemEntity(level, pos.x, pos.y, pos.z, stack.copy(), input.getDeltaMovement().x, input.getDeltaMovement().y, input.getDeltaMovement().z);
        level.addFreshEntity(itemEntity);
    }

    private void processItemEntity(ServerLevel level, ItemEntity itemEntity, FieldEffectType type) {
        UUID uuid = itemEntity.getUUID();
        ItemStack stack = itemEntity.getItem();
        ProcessState currentProgress = itemEntityProgress.get(uuid);
        if (currentProgress != null && !currentProgress.matches(type, stack)) {
            itemEntityProgress.remove(uuid);
        }
        if (stack.isEmpty() || !type.canProcess(level, java.util.List.of(stack))) {
            return;
        }

        int processTime = sanitizeProcessTime(type.getProcessTime(level, java.util.List.of(stack)));
        ProcessState progress = itemEntityProgress.get(uuid);
        if (progress == null || !progress.matches(type, processTime, stack)) {
            progress = ProcessState.forItem(type, processTime, stack);
            itemEntityProgress.put(uuid, progress);
        }

        if (++progress.elapsedTicks >= progress.totalTicks) {
            List<ItemStack> result = type.processItem(level, stack);
            for (ItemStack processedStack : result) {
                if (processedStack != null && !processedStack.isEmpty()) {
                    replaceItem(level, processedStack, itemEntity);
                }
            }
            itemEntity.discard();
            itemEntityProgress.remove(uuid);
        }
    }

    private static int sanitizeProcessTime(int processTime) {
        return Math.max(1, processTime);
    }

    private static final class ProcessState {
        private final FieldEffectType type;
        private final int totalTicks;
        private final BlockState blockState;
        private final List<ItemStack> itemStacks;
        private int elapsedTicks;

        private ProcessState(FieldEffectType type, int totalTicks, BlockState blockState, List<ItemStack> itemStacks) {
            this.type = type;
            this.totalTicks = totalTicks;
            this.blockState = blockState;
            this.itemStacks = itemStacks == null
                    ? List.of()
                    : itemStacks.stream().map(ItemStack::copy).toList();
        }

        private static ProcessState forBlock(FieldEffectType type, int totalTicks, BlockState state) {
            return new ProcessState(type, totalTicks, state, List.of());
        }

        private static ProcessState forItem(FieldEffectType type, int totalTicks, ItemStack stack) {
            return forItems(type, totalTicks, List.of(stack));
        }

        private static ProcessState forItems(FieldEffectType type, int totalTicks, List<ItemStack> stacks) {
            return new ProcessState(type, totalTicks, null, stacks);
        }

        private boolean matches(FieldEffectType type, int totalTicks, BlockState state) {
            return this.type == type
                    && this.totalTicks == totalTicks
                    && this.blockState != null
                    && this.blockState.equals(state);
        }

        private boolean matches(FieldEffectType type, int totalTicks, ItemStack stack) {
            return this.type == type
                    && this.totalTicks == totalTicks
                    && matches(type, totalTicks, List.of(stack));
        }

        private boolean matches(FieldEffectType type, ItemStack stack) {
            return this.type == type
                    && matchesItems(List.of(stack));
        }

        private boolean matches(FieldEffectType type, int totalTicks, List<ItemStack> stacks) {
            return this.type == type
                    && this.totalTicks == totalTicks
                    && matchesItems(stacks);
        }

        private boolean matchesItems(List<ItemStack> stacks) {
            if (itemStacks.size() != stacks.size()) {
                return false;
            }
            for (int i = 0; i < itemStacks.size(); i++) {
                ItemStack expected = itemStacks.get(i);
                ItemStack actual = stacks.get(i);
                if (actual == null
                        || expected.getCount() != actual.getCount()
                        || !ItemStack.isSameItemSameComponents(expected, actual)) {
                    return false;
                }
            }
            return true;
        }
    }
}
