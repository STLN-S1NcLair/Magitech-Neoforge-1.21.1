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
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.gui.InfusionAltarMenu;
import net.stln.magitech.content.recipe.AthanorPillarInfusionRecipe;
import net.stln.magitech.content.recipe.InfusionRecipe;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.recipe.input.BaseAndIngredientsRecipeInput;
import net.stln.magitech.content.recipe.input.GroupedMultiStackRecipeInput;
import net.stln.magitech.content.recipe.input.MultiStackRecipeInput;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.effect.visual.preset.AreaVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfusionAltarBlockEntity extends ManaMachineBlockEntity implements IPedestalBlockEntity {

    public final ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    public int tickCounter = 0;

    protected static final int MAX_PEDESTAL_RANGE = 2;

    protected int progress = 0;
    protected int maxProgress = 200;

    public final ContainerData machineData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> InfusionAltarBlockEntity.this.getProgress();
                case 1 -> InfusionAltarBlockEntity.this.getMaxProgress();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> InfusionAltarBlockEntity.this.setProgress(value);
                case 1 -> InfusionAltarBlockEntity.this.setMaxProgress(value);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };


    public InfusionAltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.INFUSION_ALTAR_ENTITY.get(), pos, blockState, 50000, 3000);
    }

    public void clearContents() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
            setChanged();
        }
    }

    @Override
    public void clientTick(Level level, BlockPos pos, BlockState state) {
        tickCounter++;
        checkRecipeAndUpdate();
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        checkRecipeAndUpdate();
    }

    protected BaseAndIngredientsRecipeInput input = null;
    protected List<BlockPos> pedestalPositions = List.of();

    // Tickごとにレシピをチェックして進行度を更新する。レシピが成立しない場合は進行度をリセット
    protected void checkRecipeAndUpdate() {
        Optional<RecipeHolder<InfusionRecipe>> optional = getMatchRecipe();
        if (!optional.isPresent()) {
            setProgress(0);
            return;
        }
        RecipeHolder<InfusionRecipe> recipe = optional.get();
        progress++;
        if (level.isClientSide) {
            progressVFX();
        }
        if (progress >= maxProgress) {
            craft(recipe);
        }
    }

    protected Optional<RecipeHolder<InfusionRecipe>> getMatchRecipe() {
        // 周囲の台座のアイテムスタックのリストのべき集合を取得
        List<List<ItemStack>> pedestalStacks = getPedestalStacks();
        // べき集合の中からレシピにマッチするものがあるかチェック
        for (List<ItemStack> stacks : pedestalStacks) {
            input = new BaseAndIngredientsRecipeInput(this.inventory.getStackInSlot(0), stacks);
            RecipeManager manager = level.getRecipeManager();
            Optional<RecipeHolder<InfusionRecipe>> recipeHolder = manager.getRecipeFor(RecipeInit.INFUSION_TYPE.get(), input, level);
            if (recipeHolder.isPresent()) {
                List<BlockPos> pedestalPositions = new ArrayList<>();
                for (BlockPos pos : getPedestalPositions()) {
                    if (level.getBlockEntity(pos) instanceof IPedestalBlockEntity pedestal) {
                        ItemStackHandler inv = pedestal.getInventory();
                        boolean hasItem = false;
                        for (int i = 0; i < inv.getSlots(); i++) {
                            if (!inv.getStackInSlot(i).isEmpty()) {
                                hasItem = true;
                                break;
                            }
                        }
                        if (hasItem) {
                            pedestalPositions.add(pos);
                        }
                    }
                }
                this.pedestalPositions = pedestalPositions;
                return recipeHolder;
            }
        }
        return Optional.empty();
    }

    // 周囲の台座のアイテムスタックのリストのべき集合を返す。要素数が多い順にソート
    protected List<List<ItemStack>> getPedestalStacks() {
        List<BlockPos> pedestalPositions = getPedestalPositions();
        List<ItemStack> stacks = new ArrayList<>();
        for (BlockPos pos : pedestalPositions) {
            if (level.getBlockEntity(pos) instanceof IPedestalBlockEntity pedestal) {
                ItemStackHandler inv = pedestal.getInventory();
                for (int i = 0; i < inv.getSlots(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        stacks.add(stack);
                    }
                }
            }
        }
        int size = (int) Math.pow(2, stacks.size());
        List<List<ItemStack>> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<ItemStack> subset = new ArrayList<>();
            for (int j = 0; j < stacks.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(stacks.get(j));
                }
            }
            result.add(subset);
        }
        result.sort((a, b) -> Integer.compare(b.size(), a.size()));
        return result;
    }

    protected List<BlockPos> getPedestalPositions() {
        List<BlockPos> positions = new java.util.ArrayList<>();
        BlockPos origin = this.getBlockPos();
        for (int x = -MAX_PEDESTAL_RANGE; x <= MAX_PEDESTAL_RANGE; x++) {
            for (int z = -MAX_PEDESTAL_RANGE; z <= MAX_PEDESTAL_RANGE; z++) {
                if (x == 0 && z == 0) continue;
                BlockPos pos = origin.offset(x, 0, z);
                if (level.getBlockEntity(pos) instanceof IPedestalBlockEntity) {
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    protected void craft(RecipeHolder<InfusionRecipe> recipeHolder) {
        if (!level.isClientSide) {
            InfusionRecipe recipe = recipeHolder.value();
            // 保存されたinputを使用して消費を実行
            if (input == null) return;

            List<SizedIngredient> ingredients = recipe.getSizedIngredients();
            List<Integer> ingredientStackIndices = new ArrayList<>();

            // バックトラッキングで割り当てを探索
            if (!findAssignment(ingredients, 0, ingredientStackIndices)) {
                setProgress(0);
                return;
            }

            // 割り当てが成功したら、実際にアイテムを消費
            // ベース素材を消費
            inventory.extractItem(0, recipe.getBase().count(), false);

            // 割り当てられたスタックからアイテムを消費
            for (int i = 0; i < ingredients.size(); i++) {
                SizedIngredient ingredient = ingredients.get(i);
                int stackIndex = ingredientStackIndices.get(i);
                ItemStack stack = input.stacks().get(stackIndex);
                stack.shrink(ingredient.count());
            }

            // 結果を出力
            ItemStack result = recipe.assemble(input, level.registryAccess());
            Vec3 spawnPos = this.getBlockPos().getCenter().add(0, 1, 0);
            level.addFreshEntity(new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, result));
        } else {
            craftVFX();
        }
        setProgress(0);
    }

    // バックトラッキングで各ingredientをスタックに割り当てる
    protected boolean findAssignment(List<SizedIngredient> ingredients, int ingredientIdx, List<Integer> assignment) {
        // ベースケース：すべてのingredientが割り当てられた
        if (ingredientIdx == ingredients.size()) {
            return true;
        }

        SizedIngredient ingredient = ingredients.get(ingredientIdx);

        // このingredientを各スタックに試す
        for (int stackIdx = 0; stackIdx < input.stacks().size(); stackIdx++) {
            ItemStack stack = input.stacks().get(stackIdx);

            // スタックがこのingredientにマッチするか確認
            if (stack.isEmpty() || !ingredient.test(stack)) {
                continue;
            }

            // このスタックが既に別のingredientに割り当てられていないか確認
            if (assignment.contains(stackIdx)) {
                continue;
            }

            // スタックに十分なアイテムがあるか確認
            if (stack.getCount() >= ingredient.count()) {
                // この割り当てを試す
                assignment.add(stackIdx);

                // 次のingredientで再帰
                if (findAssignment(ingredients, ingredientIdx + 1, assignment)) {
                    return true;
                }

                // バックトラック
                assignment.remove(assignment.size() - 1);
            }
        }

        return false;
    }

    protected void progressVFX() {
        float theta = Mth.lerp(((float) tickCounter % 160) / 160, 0, Mth.TWO_PI);
        final int amount = 3;
        theta += Mth.TWO_PI / amount * (tickCounter % amount);
        Vec3 dir = new Vec3(Math.cos(theta), 0, Math.sin(theta));
        Vec3 base = this.getBlockPos().getBottomCenter().add(0, 1.25, 0);
        Vec3 pos = base.add(dir.scale(0.2));
        PointVFX.spray(level, pos, Element.MANA, (lvl, vec, elm) -> PresetHelper.smaller(PresetHelper.longer(SquareParticles.squareParticle(lvl, vec, elm))), dir.add(0, 1, 0).normalize(), 1, 0.01F, 0F);
        if (tickCounter % 3 == 0) {
            PointVFX.spray(level, base, Element.MANA, (lvl, vec, elm) -> SquareParticles.squareParticle(lvl, vec, elm), new Vec3(0, 0, 0), 1, 0.05F, 0F);
        }
    }

    protected void craftVFX() {
        Vec3 pos = this.getBlockPos().getBottomCenter().add(0, 1, 0);
        AreaVFX.areaLight(level, Element.MANA, pos, 0.3F, 0.2F, 20);
        PointVFX.spray(level, pos, Element.MANA, (lvl, vec, elm) -> PresetHelper.bigger(SquareParticles.squareGravityParticle(lvl, vec, elm, 0.1F)), new Vec3(0, 1, 0), 10, 0.15F, 0.1F);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("progress", getProgress());
        tag.putInt("maxProgress", getMaxProgress());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        if (tag.contains("progress")) {
            setProgress(tag.getInt("progress"));
        }
        if (tag.contains("maxProgress")) {
            setMaxProgress(tag.getInt("maxProgress"));
        }
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
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new InfusionAltarMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.magitech.infusion_altar");
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
    public ItemStackHandler getInventory() {
        return inventory;
    }


    @Override
    public int getTickCounter() {
        return tickCounter;
    }

    public int getContainerSize() {
        return 1;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side == Direction.DOWN) {
            return ManaFlowRule.BothWays(-1.0F);
        }
        return ManaFlowRule.None();
    }
}
