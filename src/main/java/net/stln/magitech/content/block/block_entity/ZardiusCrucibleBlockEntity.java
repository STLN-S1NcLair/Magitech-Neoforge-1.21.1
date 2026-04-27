package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.capability.FallbackFluidTank;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ZardiusCrucibleBlock;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.recipe.ZardiusCrucibleRecipe;
import net.stln.magitech.content.recipe.input.CrucibleRecipeInput;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ZardiusCrucibleBlockEntity extends ManaMachineBlockEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(8) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    public final FallbackFluidTank tank = new FallbackFluidTank(2, 2000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    public int craftingTime = 0;
    public int maxCraftingTime = 200;
    public FluidStack oldFluidStack = FluidStack.EMPTY;
    public int tickCounter = 0;
    private double fluidAnimBefore = 0;
    private double fluidAnimAfter = 0;
    private float startMarker = 0f;
    private double lastRenderValue = 0;

    public ZardiusCrucibleBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), pos, blockState);
    }

    public NonNullList<ItemStack> getRenderStack() {
        NonNullList<ItemStack> stack = NonNullList.create();
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                stack.add(this.inventory.getStackInSlot(i));
            }
        }
        if (stack.isEmpty()) {
            stack.add(ItemStack.EMPTY);
        }
        return stack;
    }

    private FluidStack lastFluid() {
        return this.tank.getLastFluid();
    }

    private int lastFluidAmount() {
        return lastFluid().getAmount();
    }

    private int tankCapacity() {
        return this.tank.getCapacity();
    }

    private boolean hasSingleCraftFluid() {
        return this.tank.getTanks() == 1 && !lastFluid().isEmpty();
    }

    private boolean canCraftWithTwoTanks(ZardiusCrucibleRecipe recipe) {
        if (tank.getTanks() != 2) return false;
        FluidStack inputFluid = tank.getFluidInTank(0);
        FluidStack outputFluid = tank.getFluidInTank(1);
        boolean inputOk = recipe.getFluidIngredient().test(inputFluid);
        boolean outputOk = outputFluid.isEmpty() || FluidStack.isSameFluid(outputFluid, recipe.getResultFluid());
        int outputCapacity = tank.getTankCapacity(1) - outputFluid.getAmount();
        boolean enoughSpace = outputCapacity >= recipe.getResultFluid().getAmount();
        return inputOk && outputOk && enoughSpace;
    }

    private void processRecipe(Level level, BlockPos pos, BlockState state, ZardiusCrucibleRecipe recipe, CrucibleRecipeInput input) {
        if (!(hasSingleCraftFluid() || canCraftWithTwoTanks(recipe))) {
            this.craftingTime = 0;
            BlockState offState = state.setValue(ZardiusCrucibleBlock.LIT, false);
            level.setBlock(pos, offState, 3);
            setChanged();
            return;
        }

        BlockState newState;
        if (state.getBlock() instanceof ZardiusCrucibleBlock crucibleBlock && crucibleBlock.isOnFire(state, level, pos)) {
            if (this.getMana() < recipe.getMana()) {
                this.craftingTime = 0;
                newState = state.setValue(ZardiusCrucibleBlock.LIT, false);
            } else {
                if (this.craftingTime < this.maxCraftingTime) {
                    this.craftingTime++;
                    newState = state.setValue(ZardiusCrucibleBlock.LIT, true);
                } else {
                    if (!consumeRecipeIngredients(recipe.getSizedIngredients())) {
                        this.craftingTime = 0;
                        newState = state.setValue(ZardiusCrucibleBlock.LIT, false);
                    } else {
                        newState = executeCrafting(level, pos, state, recipe, input);
                    }
                }
            }
        } else {
            this.craftingTime = 0;
            newState = state.setValue(ZardiusCrucibleBlock.LIT, false);
        }
        level.setBlock(pos, newState, 3);
        setChanged();
    }

    /**
     * クラフト実行処理（液体操作・アイテム出力・サウンド・状態更新）
     */
    private BlockState executeCrafting(Level level, BlockPos pos, BlockState state, ZardiusCrucibleRecipe recipe, CrucibleRecipeInput input) {
        // 液体操作
        if (tank.getTanks() == 2 && canCraftWithTwoTanks(recipe)) {
            FluidStack inputFluid = tank.getFluidInTank(0);
            tank.drain(new FluidStack(inputFluid.getFluid(), recipe.getFluidIngredient().amount(), inputFluid.getTag()), IFluidHandler.FluidAction.EXECUTE);
            tank.fill(recipe.getResultFluid(), IFluidHandler.FluidAction.EXECUTE);
        } else {
            FluidStack primaryFluid = lastFluid();
            if (!primaryFluid.isEmpty()) {
                this.tank.drain(primaryFluid.copyWithAmount(recipe.getFluidIngredient().amount()), IFluidHandler.FluidAction.EXECUTE);
            }
            this.tank.fill(recipe.getResultFluid(), IFluidHandler.FluidAction.EXECUTE);
        }
        // アイテム出力
        ItemStack result = recipe.assemble(input, level.registryAccess());
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, result);
        level.addFreshEntity(itemEntity);
        // サウンド
        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0f, 0.6f + (level.random.nextFloat() * 0.8f));
        // 状態更新
        this.craftingTime = 0;
        setChanged();
        return state.setValue(ZardiusCrucibleBlock.LIT, false);
    }

    private boolean consumeRecipeIngredients(List<SizedIngredient> ingredients) {
        List<Integer> slotIndices = new ArrayList<>();
        List<ItemStack> nonEmptyStacks = new ArrayList<>();
        for (int slot = 0; slot < this.inventory.getSlots(); slot++) {
            ItemStack stack = this.inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                slotIndices.add(slot);
                nonEmptyStacks.add(stack.copy());
            }
        }

        int stackCount = nonEmptyStacks.size();
        int ingredientCount = ingredients.size();
        int source = 0;
        int firstStackNode = 1;
        int firstIngredientNode = firstStackNode + stackCount;
        int sink = firstIngredientNode + ingredientCount;
        int nodeCount = sink + 1;

        int[][] capacity = new int[nodeCount][nodeCount];
        int[][] originalCapacity = new int[nodeCount][nodeCount];
        int totalRequired = 0;

        for (int i = 0; i < stackCount; i++) {
            int stackSize = nonEmptyStacks.get(i).getCount();
            capacity[source][firstStackNode + i] = stackSize;
            originalCapacity[source][firstStackNode + i] = stackSize;
        }

        for (int j = 0; j < ingredientCount; j++) {
            int required = Math.max(0, ingredients.get(j).count());
            capacity[firstIngredientNode + j][sink] = required;
            originalCapacity[firstIngredientNode + j][sink] = required;
            totalRequired += required;
        }

        for (int i = 0; i < stackCount; i++) {
            ItemStack stack = nonEmptyStacks.get(i);
            int stackNode = firstStackNode + i;
            int stackSize = stack.getCount();

            for (int j = 0; j < ingredientCount; j++) {
                if (ingredients.get(j).ingredient().test(stack)) {
                    capacity[stackNode][firstIngredientNode + j] = stackSize;
                    originalCapacity[stackNode][firstIngredientNode + j] = stackSize;
                }
            }
        }

        int flow = 0;
        int[] parent = new int[nodeCount];

        while (true) {
            java.util.Arrays.fill(parent, -1);
            parent[source] = source;

            java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
            queue.add(source);

            while (!queue.isEmpty() && parent[sink] == -1) {
                int from = queue.poll();
                for (int to = 0; to < nodeCount; to++) {
                    if (parent[to] == -1 && capacity[from][to] > 0) {
                        parent[to] = from;
                        queue.add(to);
                    }
                }
            }

            if (parent[sink] == -1) {
                break;
            }

            int add = Integer.MAX_VALUE;
            int node = sink;
            while (node != source) {
                int prev = parent[node];
                add = Math.min(add, capacity[prev][node]);
                node = prev;
            }

            node = sink;
            while (node != source) {
                int prev = parent[node];
                capacity[prev][node] -= add;
                capacity[node][prev] += add;
                node = prev;
            }

            flow += add;
        }

        if (flow != totalRequired) {
            return false;
        }

        for (int i = 0; i < stackCount; i++) {
            int stackNode = firstStackNode + i;
            int consumeCount = 0;
            for (int j = 0; j < ingredientCount; j++) {
                int ingredientNode = firstIngredientNode + j;
                int used = originalCapacity[stackNode][ingredientNode] - capacity[stackNode][ingredientNode];
                consumeCount += used;
            }

            if (consumeCount > 0) {
                int slot = slotIndices.get(i);
                this.inventory.extractItem(slot, consumeCount, false);
            }
        }

        return true;
    }

    private void playBoilSound(Level level, BlockPos pPos, BlockState pState) {
        RandomSource random = level.random;
        FluidStack primaryFluid = lastFluid();
        if (primaryFluid.is(Tags.Fluids.LAVA)) {
            if (pState.getValue(ZardiusCrucibleBlock.LIT)) {
                if (random.nextFloat() > 0.95) {
                    level.playSound(null, pPos, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.5f, 0.6f + (random.nextFloat() * 0.8f));
                }
            }
            if (random.nextFloat() > 0.99) {
                level.playSound(null, pPos, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.5f, 0.6f + (random.nextFloat() * 0.8f));
            }

        } else if (!primaryFluid.isEmpty()) {
            if (pState.getValue(ZardiusCrucibleBlock.LIT)) {
                if (random.nextFloat() > 0.8) {
                    level.playSound(null, pPos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 5, 0.4f + random.nextFloat());
                }
                if (random.nextFloat() > 0.7) {
                    level.playSound(null, pPos, SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON, SoundSource.BLOCKS, 1, 0.6f + (random.nextFloat() * 0.8f));
                }
            }
            if (random.nextFloat() > 0.95) {
                level.playSound(null, pPos, SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON, SoundSource.BLOCKS, 1, 0.6f + (random.nextFloat() * 0.8f));
            }
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        addDroppedItem();

        if (hasSingleCraftFluid()) {
            List<ItemStack> stacks = IntStream.rangeClosed(0, inventory.getSlots() - 1).mapToObj(inventory::getStackInSlot).toList();
            CrucibleRecipeInput input = new CrucibleRecipeInput(stacks, lastFluid().copy());
            level.getRecipeManager()
                    .getRecipeFor(RecipeInit.ZARDIUS_CRUCIBLE_TYPE.get(), input, level)
                    .map(RecipeHolder::value)
                    .ifPresent(recipe -> processRecipe(level, pos, state, recipe, input));
        } else if (this.craftingTime > 0 || state.getValue(ZardiusCrucibleBlock.LIT)) {
            this.craftingTime = 0;
            level.setBlock(pos, state.setValue(ZardiusCrucibleBlock.LIT, false), 3);
            setChanged();
        }

        if (state.getBlock() instanceof ZardiusCrucibleBlock crucibleBlock && crucibleBlock.isOnFire(state, level, pos)) {
            // 沸騰する音を鳴らす
            playBoilSound(level, pos, state);
        }
    }

    public void clientTick(Level level, BlockPos pPos, BlockState pState) {
        tickCounter++;
        if (tickCounter > 2000 && startMarker > 1000 && startMarker < tickCounter - 200) {
            tickCounter -= 1000;
            startMarker -= 1000;
        }
        if (pState.getValue(ZardiusCrucibleBlock.LIT)) {
            int capacity = Math.max(1, tankCapacity());
            float height = (float) lastRenderValue / capacity * 0.75f + 0.2f;
            for (int i = 0; i < 10; i++) {
                level.addParticle(new SquareParticleEffect(
                                new Vector3f(0.5F, 1.0F, 0.5F),
                                new Vector3f(0.5F, 0.5F, 1.0F),
                                1.5F,
                                Mth.randomBetweenInclusive(level.random, 5, 10),
                                Mth.randomBetween(level.random, -0.05F, 0.05F), 15, 1.0F
                        ),
                        pPos.getX() + 0.5 + Mth.randomBetween(level.random, -0.375F, 0.375F),
                        pPos.getY() + height,
                        pPos.getZ() + 0.5 + Mth.randomBetween(level.random, -0.375F, 0.375F),
                        Mth.randomBetween(level.random, -0.075F, 0.075F),
                        Mth.randomBetween(level.random, 0.05F, 0.2F),
                        Mth.randomBetween(level.random, -0.075F, 0.075F)
                );
                level.addParticle(new SquareParticleEffect(
                                new Vector3f(1.0F, 0.5F, 1.0F),
                                new Vector3f(0.5F, 1.0F, 1.0F),
                                1.5F,
                                Mth.randomBetweenInclusive(level.random, 5, 10),
                                Mth.randomBetween(level.random, -0.05F, 0.05F), 15, 1.0F
                        ),
                        pPos.getX() + 0.5 + Mth.randomBetween(level.random, -0.375F, 0.375F),
                        pPos.getY() + height,
                        pPos.getZ() + 0.5 + Mth.randomBetween(level.random, -0.375F, 0.375F),
                        Mth.randomBetween(level.random, -0.075F, 0.075F),
                        Mth.randomBetween(level.random, 0.05F, 0.2F),
                        Mth.randomBetween(level.random, -0.075F, 0.075F)
                );
            }
        }
    }

    private void addDroppedItem() {
        List<ItemEntity> itemList = this.getDroppedItem();
        if (!itemList.isEmpty()) {
            for (ItemEntity itemEntity : itemList) {
                ItemStack stack = itemEntity.getItem();
                if (this.addItemStack(stack, stack.getCount())) { // スタック全体を入れる
                    itemEntity.setItem(ItemStack.EMPTY);
                    setChanged();
                }
            }
        }
    }

    private List<ItemEntity> getDroppedItem() {
        return new ArrayList<>(this.level.getEntitiesOfClass(ItemEntity.class,
                new AABB(this.worldPosition.getX() + 0.125, this.worldPosition.getY() + 0.1875, this.worldPosition.getZ() + 0.125,
                        this.worldPosition.getX() + 0.875, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 0.875),
                EntitySelector.ENTITY_STILL_ALIVE));
    }

    public void addItem(Player player, ItemStack pItemStack, int count) {
        if (pItemStack.isEmpty()) {
            for (int i = 0; i < 8; i++) {
                if (!this.inventory.getStackInSlot(7 - i).isEmpty()) {
                    ItemStack removeStack = this.inventory.getStackInSlot(7 - i);
                    player.addItem(removeStack);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5f, 2.0f);
                    if (!removeStack.isEmpty()) {
                        ItemEntity itementity = player.drop(removeStack, false);
                        if (itementity != null) {
                            itementity.setNoPickUpDelay();
                            itementity.setTarget(player.getUUID());
                        }
                    }
                    break;
                }
            }
        } else if (!player.isCrouching() && pItemStack.getCapability(Capabilities.FluidHandler.ITEM) != null) {
            IFluidHandlerItem iFluidHandlerItem = pItemStack.split(1).getCapability(Capabilities.FluidHandler.ITEM);
            boolean isEmpty = true;
            for (int i = 0; i < iFluidHandlerItem.getTanks(); i++) {
                if (!iFluidHandlerItem.getFluidInTank(i).isEmpty()) {
                    isEmpty = false;
                }
            }
            FluidStack primaryFluid = lastFluid();
            Integer emptyOrSameSlot = getItemFluidEmptyOrSameSlot(iFluidHandlerItem, primaryFluid.getFluid());
            if (!primaryFluid.isEmpty() && isEmpty && emptyOrSameSlot != null && iFluidHandlerItem.isFluidValid(emptyOrSameSlot, primaryFluid)) {
                // 大釜からアイテムに液体を移す
                playFluidDrainSound(primaryFluid, iFluidHandlerItem);
                int drainAmount = Math.min(iFluidHandlerItem.getTankCapacity(emptyOrSameSlot) - iFluidHandlerItem.getFluidInTank(emptyOrSameSlot).getAmount(), 1000);
                FluidStack stack = this.tank.drain(primaryFluid.copyWithAmount(Math.max(0, drainAmount)), IFluidHandler.FluidAction.EXECUTE);
                iFluidHandlerItem.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);
            } else {
                FluidStack itemFluid = getItemFluidStack(iFluidHandlerItem);
                if (!itemFluid.isEmpty()) {
                    int drainAmount = Math.min(itemFluid.getAmount(), 1000);
                    FluidStack probeStack = itemFluid.copyWithAmount(drainAmount);
                    int accepted = this.tank.fill(probeStack, IFluidHandler.FluidAction.SIMULATE);
                    if (accepted > 0) {
                        // アイテムから大釜に液体を移す
                        playFluidFillSound(this.tank, iFluidHandlerItem);
                        FluidStack stack;
                        if (!player.getAbilities().instabuild) {
                            stack = iFluidHandlerItem.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                        } else {
                            stack = iFluidHandlerItem.drain(accepted, IFluidHandler.FluidAction.SIMULATE);
                        }
                        this.tank.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
            if (pItemStack.isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, iFluidHandlerItem.getContainer());
            } else {
                player.addItem(iFluidHandlerItem.getContainer());
            }
        } else {
            int insertCount = Math.min(Math.max(0, count), pItemStack.getCount());
            if (insertCount > 0) {
                addItemStack(pItemStack, insertCount);
            }
        }
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    private void playFluidFillSound(FallbackFluidTank fluidTank, IFluidHandlerItem iFluidHandlerItem) {
        FluidStack itemFluid = getItemFluidStack(iFluidHandlerItem);
        int probeAmount = Math.min(itemFluid.getAmount(), 1000);
        if (probeAmount > 0 && fluidTank.fill(itemFluid.copyWithAmount(probeAmount), IFluidHandler.FluidAction.SIMULATE) > 0) {
            if (itemFluid.is(Tags.Fluids.LAVA)) {
                this.level.playSound(null, this.worldPosition, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.PLAYERS);
            } else {
                this.level.playSound(null, this.worldPosition, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
            }
        }
    }

    private void playFluidDrainSound(FluidStack tankFluid, IFluidHandlerItem iFluidHandlerItem) {
        boolean isMax = true;
        for (int i = 0; i < iFluidHandlerItem.getTanks(); i++) {
            if (iFluidHandlerItem.getFluidInTank(i).getAmount() != iFluidHandlerItem.getTankCapacity(i)) {
                isMax = false;
            }
        }
        if (!tankFluid.isEmpty() && !isMax && hasSameFluidOrEmpty(tankFluid, iFluidHandlerItem)) {
            if ((!tankFluid.isEmpty() ? tankFluid : getItemFluidStack(iFluidHandlerItem)).is(Tags.Fluids.LAVA)) {
                this.level.playSound(null, this.worldPosition, SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS);
            } else {
                this.level.playSound(null, this.worldPosition, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
            }
        }
    }

    private boolean hasSameFluidOrEmpty(FluidStack tankFluid, IFluidHandlerItem iFluidHandlerItem) {
        FluidStack itemFluid = getItemFluidStack(iFluidHandlerItem);
        if (tankFluid.isEmpty() || itemFluid.isEmpty()) {
            return true;
        }
        return FluidStack.isSameFluid(tankFluid, itemFluid);
    }

    private FluidStack getItemFluidStack(IFluidHandlerItem iFluidHandlerItem) {
        FluidStack itemFluid = FluidStack.EMPTY;
        for (int i = 0; i < iFluidHandlerItem.getTanks(); i++) {
            if (!iFluidHandlerItem.getFluidInTank(i).isEmpty()) {
                itemFluid = iFluidHandlerItem.getFluidInTank(i);
                break;
            }
        }
        return itemFluid;
    }

    private Integer getItemFluidEmptyOrSameSlot(IFluidHandlerItem iFluidHandlerItem, Fluid fluid) {
        Integer slot = null;
        for (int i = 0; i < iFluidHandlerItem.getTanks(); i++) {
            if (iFluidHandlerItem.getFluidInTank(i).isEmpty() || iFluidHandlerItem.getFluidInTank(i).is(fluid)) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private boolean addItemStack(ItemStack pItemStack, int count) {
        int remaining = count;
        for (int i = 0; i < 8 && remaining > 0; i++) {
            if (this.inventory.getStackInSlot(i).isEmpty()) {
                int toInsert = Math.min(remaining, pItemStack.getMaxStackSize());
                ItemStack insertStack = pItemStack.copy();
                insertStack.setCount(toInsert);
                this.inventory.setStackInSlot(i, insertStack);
                remaining -= toInsert;
                setChanged();
            }
        }
        pItemStack.setCount(remaining);
        return remaining < count; // 1つでも入ったらtrue
    }

    public void clearContents() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
            setChanged();
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public double getFluidAnim(ZardiusCrucibleBlockEntity blockEntity, float partialTicks) {
        float time = tickCounter + partialTicks;
        int currentAmount = blockEntity.lastFluidAmount();

        // 変化を検出したら補間開始
        if (fluidAnimAfter != currentAmount) {
            fluidAnimBefore = lastRenderValue; // 前回描画時の値を保存しておいたものを使う
            fluidAnimAfter = currentAmount;
            startMarker = time;
        }

        double elapsed = time - startMarker;
        double duration = 15.0;

        // 補間完了後はそのまま返す
        if (elapsed >= duration) {
            lastRenderValue = fluidAnimAfter;
            return fluidAnimAfter;
        }

        // イージング補間: y = -0.5x^3 + 1.5x
        double t = elapsed / duration;
        double interp = -0.5 * Math.pow(t, 3) + 1.5 * t;

        lastRenderValue = fluidAnimBefore * (1.0 - interp) + fluidAnimAfter * interp;
        return lastRenderValue;
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.magitech.zardius_crucible");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> stacks = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getSlots(); i++) {
            stacks.set(i, inventory.getStackInSlot(i));
        }
        return stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, items.get(i));
        }
    }

    @Override
    public int getContainerSize() {
        return 8;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tank.save(registries, tag);
        tag.putInt("crafting_time", craftingTime);
        tag.putInt("max_crafting_time", maxCraftingTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        tank.load(registries, tag);
        craftingTime = tag.getInt("crafting_time");
        maxCraftingTime = tag.getInt("max_crafting_time");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (state.getValue(ZardiusCrucibleBlock.FACING) == side) {
            return ManaFlowRule.bothWays(-1.0F);
        }
        return ManaFlowRule.none();
    }
}
