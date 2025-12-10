package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.ZardiusCrucibleBlock;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ZardiusCrucibleRecipe;
import net.stln.magitech.recipe.input.CrucibleRecipeInput;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ZardiusCrucibleBlockEntity extends BlockEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(8) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    public final FluidTank fluidTank = new FluidTank(2000) {
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

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        addDroppedItem();
        List<ItemStack> stacks = IntStream.rangeClosed(0, inventory.getSlots() - 1).mapToObj(inventory::getStackInSlot).toList();
        CrucibleRecipeInput input = new CrucibleRecipeInput(stacks, fluidTank.getFluid());
        level.getRecipeManager()
                .getRecipeFor(RecipeInit.ZARDIUS_CRUCIBLE_TYPE.get(), input, level)
                .map(RecipeHolder::value)
                .ifPresent(recipe -> processRecipe(level, pos, state, recipe, input));

        if (state.getBlock() instanceof ZardiusCrucibleBlock crucibleBlock && crucibleBlock.isOnFire(state, level, pos)) {
            // 沸騰する音を鳴らす
            playBoilSound(level, pos, state);
        }
    }

    private void processRecipe(Level level, BlockPos pos, BlockState state, ZardiusCrucibleRecipe recipe, CrucibleRecipeInput input) {
        BlockState newState;
        // 火がついているか判定
        if (state.getBlock() instanceof ZardiusCrucibleBlock crucibleBlock && crucibleBlock.isOnFire(state, level, pos)) {
            // 完成品が残っていないか確認
            if (inventory.getStackInSlot(0).getCount() >= 2) return;
            var requiredAmount = recipe.fluidIngredient().amount();
            // 液体を処理できるか判定する
            boolean canProcessFluid = recipe.resultFluid()
                    .map(fluidStack -> {
                        // 大釜の液体量が完成品と同量か判定
                        if (fluidTank.getFluidAmount() != fluidStack.getAmount()) return false;
                        // 大釜から液体を取り出せるか判定
                        return fluidTank.drain(requiredAmount, IFluidHandler.FluidAction.SIMULATE).getAmount() == requiredAmount;
                    })
                    .orElseGet(() -> {
                        // 大釜から液体を取り出せるか判定
                        return fluidTank.drain(requiredAmount, IFluidHandler.FluidAction.SIMULATE).getAmount() >= requiredAmount;
                    });
            if (canProcessFluid) {
                // 時間を進める
                if (this.craftingTime < this.maxCraftingTime) {
                    this.craftingTime++;
                    newState = state.setValue(ZardiusCrucibleBlock.LIT, true);
                } else {
                    ItemStack result = recipe.assemble(input, level.registryAccess());
                    this.fluidTank.drain(requiredAmount, IFluidHandler.FluidAction.EXECUTE);
                    recipe.resultFluid().ifPresent(fluidStack -> this.fluidTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE));
                    this.clearContents();
                    this.addItemStack(result, result.getCount());
                    this.craftingTime = 0;
                    level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0f, 0.6f + (level.random.nextFloat() * 0.8f));
                    newState = state.setValue(ZardiusCrucibleBlock.LIT, false);
                }
            } else {
                this.craftingTime = 0;
                newState = state.setValue(ZardiusCrucibleBlock.LIT, false);
            }
        } else {
            this.craftingTime = 0;
            newState = state.setValue(ZardiusCrucibleBlock.LIT, false);
        }
        level.setBlock(pos, newState, 3);
        setChanged();
    }

    private void playBoilSound(Level level, BlockPos pPos, BlockState pState) {
        RandomSource random = level.random;
        if (this.fluidTank.getFluid().is(Tags.Fluids.LAVA)) {
            if (pState.getValue(ZardiusCrucibleBlock.LIT)) {
                if (random.nextFloat() > 0.95) {
                    level.playSound(null, pPos, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.5f, 0.6f + (random.nextFloat() * 0.8f));
                }
            }
            if (random.nextFloat() > 0.99) {
                level.playSound(null, pPos, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.5f, 0.6f + (random.nextFloat() * 0.8f));
            }

        } else if (!this.fluidTank.isEmpty()) {
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

    public void clientTick(Level level, BlockPos pPos, BlockState pState) {
        tickCounter++;
        if (tickCounter > 2000 && startMarker > 1000 && startMarker < tickCounter - 200) {
            tickCounter -= 1000;
            startMarker -= 1000;
        }
        if (pState.getValue(ZardiusCrucibleBlock.LIT)) {
            float height = (float) lastRenderValue / fluidTank.getCapacity() * 0.75f + 0.2f;
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
                if (this.addItemStack(stack, 1)) {
                    itemEntity.setItem(stack);
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
        boolean hasSingleItems = true;
        for (int i = 0; i < 8; i++) {
            if (this.inventory.getStackInSlot(i).getCount() > 1) {
                hasSingleItems = false;
                break;
            }
        }
        if (pItemStack.isEmpty() || !hasSingleItems) {
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
            Integer emptyOrSameSlot = getItemFluidEmptyOrSameSlot(iFluidHandlerItem, this.fluidTank.getFluid().getFluid());
            if (!fluidTank.isEmpty() && isEmpty && emptyOrSameSlot != null && iFluidHandlerItem.isFluidValid(emptyOrSameSlot, this.fluidTank.getFluid())) {
                // 大釜からアイテムに液体を移す
                playFluidDrainSound(this.fluidTank, iFluidHandlerItem);
                FluidStack stack;
                stack = this.fluidTank.drain(Math.min(iFluidHandlerItem.getTankCapacity(emptyOrSameSlot) - iFluidHandlerItem.getFluidInTank(emptyOrSameSlot).getAmount(), 1000), IFluidHandler.FluidAction.EXECUTE);
                iFluidHandlerItem.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);
            } else if (hasSameFluidOrEmpty(fluidTank, iFluidHandlerItem)) {
                // アイテムから大釜に液体を移す
                playFluidFillSound(this.fluidTank, iFluidHandlerItem);
                int drainAmount = Math.min(getItemFluidStack(iFluidHandlerItem).getAmount(), 1000);
                if (fluidTank.getFluidAmount() + drainAmount <= fluidTank.getCapacity()) { // 液体を入れる容量があれば
                    FluidStack stack;
                    if (!player.getAbilities().instabuild) {
                        stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                    } else {
                        stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                    }
                    this.fluidTank.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            if (pItemStack.isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, iFluidHandlerItem.getContainer());
            } else {
                player.addItem(iFluidHandlerItem.getContainer());
            }
        } else {
            addItemStack(pItemStack, 1);
        }
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    private void playFluidFillSound(FluidTank fluidTank, IFluidHandlerItem iFluidHandlerItem) {
        boolean isEmpty = true;
        for (int i = 0; i < iFluidHandlerItem.getTanks(); i++) {
            if (!iFluidHandlerItem.getFluidInTank(i).isEmpty()) {
                isEmpty = false;
            }
        }
        if (fluidTank.getSpace() != 0 && !isEmpty && hasSameFluidOrEmpty(fluidTank, iFluidHandlerItem)) {
            if ((!fluidTank.isEmpty() ? fluidTank.getFluid() : getItemFluidStack(iFluidHandlerItem)).is(Tags.Fluids.LAVA)) {
                this.level.playSound(null, this.worldPosition, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.PLAYERS);
            } else {
                this.level.playSound(null, this.worldPosition, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
            }
        }
    }

    private void playFluidDrainSound(FluidTank fluidTank, IFluidHandlerItem iFluidHandlerItem) {
        boolean isMax = true;
        for (int i = 0; i < iFluidHandlerItem.getTanks(); i++) {
            if (iFluidHandlerItem.getFluidInTank(i).getAmount() != iFluidHandlerItem.getTankCapacity(i)) {
                isMax = false;
            }
        }
        if (!fluidTank.isEmpty() && !isMax && hasSameFluidOrEmpty(fluidTank, iFluidHandlerItem)) {
            if ((!fluidTank.isEmpty() ? fluidTank.getFluid() : getItemFluidStack(iFluidHandlerItem)).is(Tags.Fluids.LAVA)) {
                this.level.playSound(null, this.worldPosition, SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS);
            } else {
                this.level.playSound(null, this.worldPosition, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
            }
        }
    }

    private boolean hasSameFluidOrEmpty(FluidTank fluidTank, IFluidHandlerItem iFluidHandlerItem) {
        FluidStack itemFluid = getItemFluidStack(iFluidHandlerItem);
        if (fluidTank.isEmpty() || itemFluid.isEmpty()) {
            return true;
        }
        return FluidStack.isSameFluid(fluidTank.getFluid(), itemFluid);
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
        for (int i = 0; i < 8; i++) {
            if (this.inventory.getStackInSlot(i).isEmpty()) {
                this.inventory.setStackInSlot(i, pItemStack.split(count));
                setChanged();
                return true;
            }
        }
        return false;
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
        int currentAmount = blockEntity.fluidTank.getFluidAmount();

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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        fluidTank.writeToNBT(registries, tag);
        tag.putInt("crafting_time", craftingTime);
        tag.putInt("max_crafting_time", maxCraftingTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        fluidTank.readFromNBT(registries, tag);
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
}
