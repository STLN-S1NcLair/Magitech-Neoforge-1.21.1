package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.block.AthanorPillarBlock;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.ManaNodeBlock;
import net.stln.magitech.particle.particle_option.ManaZapParticleEffect;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.recipe.AthanorPillarInfusionRecipe;
import net.stln.magitech.recipe.GroupedMultiStackRecipeInput;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.StructureHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AthanorPillarBlockEntity extends BlockEntity {

    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    int craftingProgress = 0;
    int craftingTotalTime = 200;
    public int tickCounter = 0;

    public AthanorPillarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.ATHANOR_PILLAR_ENTITY.get(), pos, blockState);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state, AthanorPillarBlockEntity blockEntity) {
        BlockState blockState = level.getBlockState(pos);
        if ((blockState.getBlock() instanceof AthanorPillarBlock athanorPillarBlock) && hasCorrectStructure(level, pos)) {
            List<List<ItemStack>> stacks = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                List<ItemStack> group = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    group.add(((AlchemetricPylonBlockEntity) level.getBlockEntity(getPylonPos(pos, i, j))).inventory.getStackInSlot(0));
                }
                stacks.add(group);
            }
            GroupedMultiStackRecipeInput input = new GroupedMultiStackRecipeInput(stacks);
            RecipeManager manager = level.getRecipeManager();
            Optional<RecipeHolder<AthanorPillarInfusionRecipe>> recipeHolder = manager.getRecipeFor(RecipeInit.ATHANOR_PILLAR_INFUSION_TYPE.get(), input, level);

            if (recipeHolder.isPresent() && blockEntity.inventory.getStackInSlot(0).is(recipeHolder.get().value().getBase().getItem())) {
                boolean flag = true;
                for (int i = 0; i < 5; i++) {
                    BlockPos vesselPos = getVesselPos(pos, i);
                    if (((ManaVesselBlockEntity) level.getBlockEntity(vesselPos)).getMana() < recipeHolder.get().value().getMana() / 5) {
                        flag = false;
                    }
                }
                if (flag) {
                    if (blockEntity.craftingProgress == blockEntity.craftingTotalTime) {
                        blockEntity.inventory.setStackInSlot(0, recipeHolder.get().value().assemble(input, level.registryAccess()));
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 4; j++) {
                                BlockPos pylonPos = getPylonPos(pos, i, j);
                                ((AlchemetricPylonBlockEntity) level.getBlockEntity(pylonPos)).inventory.setStackInSlot(0, ItemStack.EMPTY);
                                setChanged(level, pylonPos, level.getBlockState(pylonPos));
                            }
                        }
                        for (int i = 0; i < 5; i++) {
                            BlockPos vesselPos = getVesselPos(pos, i);
                            BlockState state1 = level.getBlockState(vesselPos);
                            ((ManaVesselBlockEntity) level.getBlockEntity(vesselPos)).subMana(recipeHolder.get().value().getMana() / 5);
                            setChanged(level, vesselPos, state1);
                            level.sendBlockUpdated(vesselPos, state1, state1, 3);
                            blockEntity.craftingProgress = 0;
                        }
                        blockEntity.craftingProgress = 0;
                        setChanged(level, pos, state);
                    }
                    blockEntity.craftingProgress++;
                    if (blockEntity.craftingProgress % 5 == 0) {
                        level.playSound(null, pos, SoundInit.ATHANOR_PILLAR_INFUSION.get(), SoundSource.BLOCKS, 1.0F, Mth.nextFloat(level.random, 0.9F, 1.1F));
                    }
                    if (level.random.nextBoolean() && level.random.nextBoolean()) {
                        level.playSound(null, pos, SoundInit.ATHANOR_PILLAR_ZAP.get(), SoundSource.BLOCKS, 1.0F, Mth.nextFloat(level.random, 0.9F, 1.1F));
                    }
                    BlockState old = state;
                    state = state.setValue(AthanorPillarBlock.LIT, true);
                    level.setBlock(pos, state, 3);
                    setChanged(level, pos, state);
                    level.sendBlockUpdated(pos, old, state, 3);
                } else {
                    reset(level, pos, state, blockEntity);
                }
            } else {
                reset(level, pos, state, blockEntity);
            }
        }
    }

    public void clientTick(Level level, BlockPos pos, BlockState state, AthanorPillarBlockEntity blockEntity) {
        tickCounter++;
        RandomSource random = level.random;
        if (hasCorrectStructure(level, pos)) {
            Vec3 center = pos.getCenter();
            for (int i = 0; i < 3; i++) {
                double x = center.x + Mth.nextDouble(random, -0.6, 0.6);
                double y = center.y + Mth.nextDouble(random, -0.6, 0.6);
                double z = center.z + Mth.nextDouble(random, -0.6, 0.6);
                level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, 3, 0), x, y, z, 0, 0, 0);
            }
            for (int i = 0; i < 3; i++) {
                double x = center.x + Mth.nextDouble(random, -0.2, 0.2);
                double y = center.y + 0.5;
                double z = center.z + Mth.nextDouble(random, -0.2, 0.2);
                level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, 1, Mth.nextFloat(random, -0.1F, 0.1F)), x, y, z, 0, Mth.nextDouble(random, 0, 0.05), 0);
            }
            for (int i = 0; i < 3; i++) {
                double x2 = center.x + Mth.nextDouble(random, -0.5, 0.5);
                double y2 = center.y + Mth.nextDouble(random, -0.5, 0.5);
                double z2 = center.z + Mth.nextDouble(random, -0.5, 0.5);
                level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, 1, Mth.nextFloat(random, -0.1F, 0.1F)), x2, y2, z2, 0, 0.03, 0);
            }
            Vector3f color = new Vector3f(1.0F, 1.0F, 1.0F);
            if ((long) level.random.nextInt(100) <= level.getGameTime() % 100L) {
                Vec3 vec3 = Vec3.atCenterOf(pos);
                double d0 = vec3.x + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
                double d1 = vec3.y + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
                double d2 = vec3.z + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
                double d3 = Mth.nextDouble(level.random, -0.2, 0.2);
                double d4 = Mth.nextDouble(level.random, -0.2, 0.2);
                double d5 = Mth.nextDouble(level.random, -0.2, 0.2);
                level.addParticle(new UnstableSquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.5F), new Vector3f(0.0F, 1.0F, 0.8F), 1.0F, 1, 0), d0, d1, d2, d3, d4, d5);
                if (random.nextFloat() < 0.2) {
                    level.addParticle(new ManaZapParticleEffect(color, color,
                            new Vector3f((float) (Mth.nextFloat(level.random, -2F, 2F) + vec3.x), (float) (Mth.nextFloat(level.random, -2F, 2F) + vec3.y), (float) (Mth.nextFloat(level.random, -2F, 2F) + vec3.z)),
                            1.0F, 2, 0), vec3.x, vec3.y, vec3.z, 0, 0, 0);
                }
            }
            if (hasItemInInventory(level, pos) && state.getValue(AthanorPillarBlock.LIT) && random.nextBoolean()) {
                zapParticle(level, pos, random, color);
            }
        } else if (random.nextFloat() < 0.2) {
            BlockPos errorPos = detectIncorrectStructurePos(level, pos);
            if (errorPos != null) {
                Vec3 center = errorPos.getCenter();
                double x = center.x + Mth.nextDouble(random, -0.6, 0.6);
                double y = center.y + Mth.nextDouble(random, -0.6, 0.6);
                double z = center.z + Mth.nextDouble(random, -0.6, 0.6);
                Vec3 v1 = Vec3.atLowerCornerOf(errorPos);
                Vec3 v2 = v1.add(1, 0, 0);
                Vec3 v3 = v1.add(1, 0, 1);
                Vec3 v4 = v1.add(0, 0, 1);
                Vec3 v5 = v1.add(0, 1, 0);
                Vec3 v6 = v1.add(1, 1, 0);
                Vec3 v7 = v1.add(1, 1, 1);
                Vec3 v8 = v1.add(0, 1, 1);
                SquareParticleEffect effect = new SquareParticleEffect(new Vector3f(1.0F, 0.2F, 0.2F), new Vector3f(1.0F, 0.2F, 0.2F), 1.0F, 1, 0);
                EffectUtil.lineEffect(level, effect, new Vec3(x, y, z), pos.getCenter(), 5, true);

                EffectUtil.lineEffect(level, effect, v1, v2, 7, true);
                EffectUtil.lineEffect(level, effect, v2, v3, 7, true);
                EffectUtil.lineEffect(level, effect, v3, v4, 7, true);
                EffectUtil.lineEffect(level, effect, v4, v1, 7, true);

                EffectUtil.lineEffect(level, effect, v5, v6, 7, true);
                EffectUtil.lineEffect(level, effect, v6, v7, 7, true);
                EffectUtil.lineEffect(level, effect, v7, v8, 7, true);
                EffectUtil.lineEffect(level, effect, v8, v5, 7, true);

                EffectUtil.lineEffect(level, effect, v1, v5, 7, true);
                EffectUtil.lineEffect(level, effect, v2, v6, 7, true);
                EffectUtil.lineEffect(level, effect, v3, v7, 7, true);
                EffectUtil.lineEffect(level, effect, v4, v8, 7, true);
            }
        }

        if (blockEntity.craftingProgress > blockEntity.craftingTotalTime - 3) {
            for (int i = 0; i < 100; i++) {

                Vec3 vec3 = Vec3.atCenterOf(pos);
                double d0 = Mth.nextDouble(level.random, -2.5, 2.5);
                double d1 = Mth.nextDouble(level.random, -2.0, 2.0);
                double d2 = Mth.nextDouble(level.random, -2.5, 2.5);
                d0 *= Math.abs(d0);
                d1 *= Math.abs(d1);
                d2 *= Math.abs(d2);
                d0 += vec3.x;
                d1 += vec3.y + 2;
                d2 += vec3.z;
                double d3 = Mth.nextDouble(level.random, -0.05, 0.05);
                double d4 = Mth.nextDouble(level.random, -0.05, 0.05);
                double d5 = Mth.nextDouble(level.random, -0.05, 0.05);
                level.addParticle(new SquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.5F), new Vector3f(0.0F, 1.0F, 0.8F), 1.0F, 2, 0), d0, d1, d2, d3, d4, d5);
                level.addParticle(new SquareParticleEffect(new Vector3f(0.0F, 1.0F, 0.8F), new Vector3f(0.3F, 0.6F, 1.0F), 1.0F, 2, 0), d0, d1, d2, d3, d4, d5);
            }
        }
    }

    public static BlockPos getPylonPos(BlockPos pos, int i, int j) {
        return switch (4 * i + j) {
            case 0 -> pos.offset(2, 0, 2);
            case 1 -> pos.offset(-2, 0, 2);
            case 2 -> pos.offset(2, 0, -2);
            case 3 -> pos.offset(-2, 0, -2);
            case 4 -> pos.offset(3, 2, 0);
            case 5 -> pos.offset(0, 2, 3);
            case 6 -> pos.offset(-3, 2, 0);
            case 7 -> pos.offset(0, 2, -3);
            case 8 -> pos.offset(4, 3, 4);
            case 9 -> pos.offset(-4, 3, 4);
            case 10 -> pos.offset(4, 3, -4);
            case 11 -> pos.offset(-4, 3, -4);
            default -> null;
        };
    }

    public static BlockPos getVesselPos(BlockPos pos, int i) {
        return switch (i) {
            case 0 -> pos.offset(3, 0, 3);
            case 1 -> pos.offset(-3, 0, 3);
            case 2 -> pos.offset(3, 0, -3);
            case 3 -> pos.offset(-3, 0, -3);
            case 4 -> pos.offset(0, 5, 0);
            default -> null;
        };
    }

    private static void zapParticle(Level level, BlockPos pos, RandomSource random, Vector3f color) {
        Vec3 vec3 = Vec3.atCenterOf(pos);
        BlockPos pillar = new BlockPos(pos);
        BlockPos posBottom1 = getPylonPos(pos, 0, 0);
        BlockPos posBottom2 = getPylonPos(pos, 0, 1);
        BlockPos posBottom3 = getPylonPos(pos, 0, 2);
        BlockPos posBottom4 = getPylonPos(pos, 0, 3);
        BlockPos posMid1 = getPylonPos(pos, 1, 0);
        BlockPos posMid2 = getPylonPos(pos, 1, 1);
        BlockPos posMid3 = getPylonPos(pos, 1, 2);
        BlockPos posMid4 = getPylonPos(pos, 1, 3);
        BlockPos posTop1 = getPylonPos(pos, 2, 0);
        BlockPos posTop2 = getPylonPos(pos, 2, 1);
        BlockPos posTop3 = getPylonPos(pos, 2, 2);
        BlockPos posTop4 = getPylonPos(pos, 2, 3);
        Vector3f vessel1 = vec3.add(3, 1, 3).toVector3f();
        Vector3f vessel2 = vec3.add(-3, 1, 3).toVector3f();
        Vector3f vessel3 = vec3.add(3, 1, -3).toVector3f();
        Vector3f vessel4 = vec3.add(-3, 1, -3).toVector3f();
        Vector3f vessel5 = vec3.add(0, 4, 0).toVector3f();
        Vector3f bottom1 = posBottom1.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f bottom2 = posBottom2.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f bottom3 = posBottom3.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f bottom4 = posBottom4.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f mid1 = posMid1.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f mid2 = posMid2.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f mid3 = posMid3.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f mid4 = posMid4.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f top1 = posTop1.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f top2 = posTop2.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f top3 = posTop3.getCenter().add(0, 0.6, 0).toVector3f();
        Vector3f top4 = posTop4.getCenter().add(0, 0.6, 0).toVector3f();
        boolean bottomFlag1 = hasItemInInventory(level, posBottom1);
        boolean bottomFlag2 = hasItemInInventory(level, posBottom2);
        boolean bottomFlag3 = hasItemInInventory(level, posBottom3);
        boolean bottomFlag4 = hasItemInInventory(level, posBottom4);
        boolean midFlag1 = hasItemInInventory(level, posMid1);
        boolean midFlag2 = hasItemInInventory(level, posMid2);
        boolean midFlag3 = hasItemInInventory(level, posMid3);
        boolean midFlag4 = hasItemInInventory(level, posMid4);
        boolean topFlag1 = hasItemInInventory(level, posTop1);
        boolean topFlag2 = hasItemInInventory(level, posTop2);
        boolean topFlag3 = hasItemInInventory(level, posTop3);
        boolean topFlag4 = hasItemInInventory(level, posTop4);
        if (bottomFlag1) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, bottom1, 1.0F, 1, 0), vessel1.x, vessel1.y, vessel1.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, bottom1, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posBottom1);
        } else {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, vessel1, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
        }
        if (bottomFlag2) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, bottom2, 1.0F, 1, 0), vessel2.x, vessel2.y, vessel2.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, bottom2, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posBottom2);
        } else {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, vessel2, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
        }
        if (bottomFlag3) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, bottom3, 1.0F, 1, 0), vessel3.x, vessel3.y, vessel3.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, bottom3, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posBottom3);
        } else {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, vessel3, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
        }
        if (bottomFlag4) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, bottom4, 1.0F, 1, 0), vessel4.x, vessel4.y, vessel4.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, bottom4, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posBottom4);
        } else {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, vessel4, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
        }
        if (midFlag1) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, mid1, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, mid1, 1.0F, 1, 0), vessel5.x, vessel5.y, vessel5.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posMid1);
        }
        if (midFlag2) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, mid2, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, mid2, 1.0F, 1, 0), vessel5.x, vessel5.y, vessel5.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posMid2);
        }
        if (midFlag3) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, mid3, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, mid3, 1.0F, 1, 0), vessel5.x, vessel5.y, vessel5.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posMid3);
        }
        if (midFlag4) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, mid4, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, mid4, 1.0F, 1, 0), vessel5.x, vessel5.y, vessel5.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posMid4);
        }
        if (topFlag1) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, top1, 1.0F, 1, 0), vessel1.x, vessel1.y, vessel1.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, top1, 1.0F, 1, 0), vessel5.x, vessel5.y, vessel5.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posTop1);
        }
        if (topFlag2) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, top2, 1.0F, 1, 0), vessel2.x, vessel2.y, vessel2.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, top2, 1.0F, 1, 0), vessel5.x, vessel5.y, vessel5.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posTop2);
        }
        if (topFlag3) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, top3, 1.0F, 1, 0), vessel3.x, vessel3.y, vessel3.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, top3, 1.0F, 1, 0), vessel5.x, vessel5.y, vessel5.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posTop3);
        }
        if (topFlag4) {
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, top4, 1.0F, 1, 0), vessel4.x, vessel4.y, vessel4.z, 0, 0, 0), 0.3F);
            randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, top4, 1.0F, 1, 0), vessel5.x, vessel5.y, vessel5.z, 0, 0, 0), 0.3F);
            itemParticle(level, random, posTop4);
        }
        randomParticle(random, () -> level.addAlwaysVisibleParticle(new ManaZapParticleEffect(color, color, vessel5, 1.0F, 1, 0), vec3.x, vec3.y + 1, vec3.z, 0, 0, 0), 0.3F);
        itemParticle(level, random, pillar);
        nodeParticle(level, random, vessel1);
        nodeParticle(level, random, vessel2);
        nodeParticle(level, random, vessel3);
        nodeParticle(level, random, vessel4);
        nodeParticle(level, random, vessel5);
    }

    private static boolean hasItemInInventory(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AlchemetricPylonBlockEntity blockEntity) {
            return !blockEntity.inventory.getStackInSlot(0).isEmpty();
        }
        if (level.getBlockEntity(pos) instanceof AthanorPillarBlockEntity blockEntity) {
            return !blockEntity.inventory.getStackInSlot(0).isEmpty();
        }
        return false;
    }

    private static void itemParticle(Level level, RandomSource random, BlockPos blockPos) {
        Vec3 center = blockPos.getCenter();
        for (int i = 0; i < 5; i++) {
            double x = center.x + Mth.nextDouble(random, -0.4, 0.4);
            double y = center.y + 0.3;
            double z = center.z + Mth.nextDouble(random, -0.4, 0.4);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, 2, Mth.nextFloat(random, -0.1F, 0.1F)),
                    x, y, z, (x - center.x) / 40, Mth.nextDouble(random, 0, 0.075), (z - center.z) / 40);
        }
        for (int i = 0; i < 3; i++) {
            double x = center.x + Mth.nextDouble(random, -0.2, 0.2);
            double y = center.y + 0.4 + Mth.nextDouble(random, -0.2, 0.2);
            double z = center.z + Mth.nextDouble(random, -0.2, 0.2);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1F, 3, 0),
                    x, y, z, 0, 0, 0);
        }
    }

    private static void nodeParticle(Level level, RandomSource random, Vector3f blockPos) {
        for (int i = 0; i < 5; i++) {
            double x = blockPos.x + Mth.nextDouble(random, -0.2, 0.2);
            double y = blockPos.y;
            double z = blockPos.z + Mth.nextDouble(random, -0.2, 0.2);
            BlockState blockState = level.getBlockState(BlockPos.containing(blockPos.x, blockPos.y, blockPos.z));
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, 2, Mth.nextFloat(random, -0.1F, 0.1F)),
                    x, y, z, (x - blockPos.x) / 40, Mth.nextDouble(random, 0, 0.075) * (blockState.hasProperty(ManaNodeBlock.FACING) ? blockState.getValue(ManaNodeBlock.FACING).getStepY() : 1), (z - blockPos.z) / 40);
        }
        for (int i = 0; i < 3; i++) {
            double x = blockPos.x + Mth.nextDouble(random, -0.2, 0.2);
            double y = blockPos.y + Mth.nextDouble(random, -0.2, 0.2);
            double z = blockPos.z + Mth.nextDouble(random, -0.2, 0.2);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1F, 3, 0),
                    x, y, z, 0, 0, 0);
        }
    }

    private static void randomParticle(RandomSource random, Runnable particleAction, float chance) {
        if (random.nextFloat() < chance) {
            particleAction.run();
        }
    }

    private static void reset(Level level, BlockPos pos, BlockState state, AthanorPillarBlockEntity blockEntity) {
        state = state.setValue(AthanorPillarBlock.LIT, false);
        level.setBlock(pos, state, 3);
        setChanged(level, pos, state);
        blockEntity.craftingProgress = 0;
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
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
        tag.put("inventory", this.inventory.serializeNBT(registries));
        tag.putInt("progress", this.craftingProgress);
        tag.putInt("totalProgress", this.craftingTotalTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        this.craftingProgress = tag.getInt("progress");
        this.craftingTotalTime = tag.getInt("totalProgress");
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

//        private static class StructureHolder {
//
//        private static final Map<BlockPos, BlockState> STRUCTURE = Map.<BlockPos, BlockState>ofEntries(
//                Map.entry(new BlockPos(0, 0, 0), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(1, 0, 0), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 0), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH)),
//                Map.entry(new BlockPos(3, 0, 0), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH)),
//                Map.entry(new BlockPos(4, 0, 0), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 0), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH)),
//                Map.entry(new BlockPos(6, 0, 0), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH)),
//                Map.entry(new BlockPos(7, 0, 0), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 0), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 0, 1), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(1, 0, 1), BlockInit.FLUORITE_BLOCK.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 1), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(3, 0, 1), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 0, 1), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 1), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 0, 1), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 0, 1), BlockInit.FLUORITE_BLOCK.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 1), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 0, 2), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.EAST)),
//                Map.entry(new BlockPos(1, 0, 2), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 2), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(3, 0, 2), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 0, 2), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 2), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 0, 2), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 0, 2), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 2), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.WEST)),
//
//                Map.entry(new BlockPos(0, 0, 3), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.EAST)),
//                Map.entry(new BlockPos(1, 0, 3), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 3), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(3, 0, 3), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 0, 3), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 3), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 0, 3), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 0, 3), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 3), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.WEST)),
//
//                Map.entry(new BlockPos(0, 0, 4), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(1, 0, 4), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 4), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(3, 0, 4), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 0, 4), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 4), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 0, 4), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 0, 4), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 4), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 0, 5), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.EAST)),
//                Map.entry(new BlockPos(1, 0, 5), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 5), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(3, 0, 5), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 0, 5), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 5), BlockInit.ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 0, 5), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 0, 5), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 5), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.WEST)),
//
//                Map.entry(new BlockPos(0, 0, 6), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.EAST)),
//                Map.entry(new BlockPos(1, 0, 6), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 6), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(3, 0, 6), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 0, 6), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 6), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 0, 6), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 0, 6), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 6), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.WEST)),
//
//                Map.entry(new BlockPos(0, 0, 7), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(1, 0, 7), BlockInit.FLUORITE_BLOCK.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 7), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(3, 0, 7), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 0, 7), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 7), BlockInit.FLUORITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 0, 7), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 0, 7), BlockInit.FLUORITE_BLOCK.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 7), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 0, 8), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(1, 0, 8), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(2, 0, 8), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH)),
//                Map.entry(new BlockPos(3, 0, 8), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH)),
//                Map.entry(new BlockPos(4, 0, 8), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 0, 8), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH)),
//                Map.entry(new BlockPos(6, 0, 8), BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH)),
//                Map.entry(new BlockPos(7, 0, 8), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 0, 8), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//
//
//                Map.entry(new BlockPos(0, 1, 0), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 1, 0), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 1, 0), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(1, 1, 1), BlockInit.MANA_VESSEL.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 1, 1), BlockInit.MANA_VESSEL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(2, 1, 2), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 1, 2), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 1, 4), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 1, 4), BlockInit.ATHANOR_PILLAR.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 1, 4), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(2, 1, 6), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//                Map.entry(new BlockPos(6, 1, 6), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(1, 1, 7), BlockInit.MANA_VESSEL.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 1, 7), BlockInit.MANA_VESSEL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 1, 8), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 1, 8), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 1, 8), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//
//                Map.entry(new BlockPos(0, 2, 0), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 2, 0), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 2, 0), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(1, 2, 1), BlockInit.MANA_NODE.get().defaultBlockState().setValue(ManaNodeBlock.FACING, Direction.UP)),
//                Map.entry(new BlockPos(4, 2, 1), BlockInit.POLISHED_ALCHECRYSITE_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(7, 2, 1), BlockInit.MANA_NODE.get().defaultBlockState().setValue(ManaNodeBlock.FACING, Direction.UP)),
//
//                Map.entry(new BlockPos(0, 2, 4), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(1, 2, 4), BlockInit.POLISHED_ALCHECRYSITE_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(7, 2, 4), BlockInit.POLISHED_ALCHECRYSITE_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(8, 2, 4), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(1, 2, 7), BlockInit.MANA_NODE.get().defaultBlockState().setValue(ManaNodeBlock.FACING, Direction.UP)),
//                Map.entry(new BlockPos(4, 2, 7), BlockInit.POLISHED_ALCHECRYSITE_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(7, 2, 7), BlockInit.MANA_NODE.get().defaultBlockState().setValue(ManaNodeBlock.FACING, Direction.UP)),
//
//                Map.entry(new BlockPos(0, 2, 8), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 2, 8), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 2, 8), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//
//                Map.entry(new BlockPos(0, 3, 0), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 3, 0), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 3, 0), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(4, 3, 1), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 3, 4), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(1, 3, 4), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//                Map.entry(new BlockPos(7, 3, 4), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 3, 4), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(4, 3, 7), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 3, 8), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 3, 8), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 3, 8), BlockInit.POLISHED_ALCHECRYSITE.get().defaultBlockState()),
//
//
//                Map.entry(new BlockPos(0, 4, 0), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 4, 0), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 4, 0), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 4, 4), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 4, 4), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 4, 8), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 4, 8), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(8, 4, 8), BlockInit.ALCHEMETRIC_PYLON.get().defaultBlockState()),
//
//
//                Map.entry(new BlockPos(4, 5, 0), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(0, 5, 4), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//                Map.entry(new BlockPos(4, 5, 4), BlockInit.MANA_NODE.get().defaultBlockState().setValue(ManaNodeBlock.FACING, Direction.DOWN)),
//                Map.entry(new BlockPos(8, 5, 4), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(4, 5, 8), BlockInit.ALCHECRYSITE_BRICK_WALL.get().defaultBlockState()),
//
//
//                Map.entry(new BlockPos(4, 6, 0), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(4, 6, 1), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//
//                Map.entry(new BlockPos(4, 6, 2), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//
//                Map.entry(new BlockPos(4, 6, 3), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//
//                Map.entry(new BlockPos(0, 6, 4), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//                Map.entry(new BlockPos(1, 6, 4), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(2, 6, 4), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(3, 6, 4), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(4, 6, 4), BlockInit.MANA_VESSEL.get().defaultBlockState()),
//                Map.entry(new BlockPos(5, 6, 4), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(6, 6, 4), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(7, 6, 4), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//                Map.entry(new BlockPos(8, 6, 4), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState()),
//
//                Map.entry(new BlockPos(4, 6, 5), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//
//                Map.entry(new BlockPos(4, 6, 6), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//
//                Map.entry(new BlockPos(4, 6, 7), BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
//
//                Map.entry(new BlockPos(4, 6, 8), BlockInit.ALCHECRYSITE_BRICKS.get().defaultBlockState())
//        );
//    }

    private static final IMultiblock STRUCTURE = PatchouliAPI.get().makeMultiblock(
            new String[][]
                    {
                            {
                                    "    B    ",
                                    "    A    ",
                                    "    A    ",
                                    "    A    ",
                                    "BAAAVAAAB",
                                    "    A    ",
                                    "    A    ",
                                    "    A    ",
                                    "    B    "
                            },
                            {
                                    "    L    ",
                                    "         ",
                                    "         ",
                                    "         ",
                                    "L   D   L",
                                    "         ",
                                    "         ",
                                    "         ",
                                    "    L    "
                            },
                            {
                                    "M   L   M",
                                    "         ",
                                    "         ",
                                    "         ",
                                    "L       L",
                                    "         ",
                                    "         ",
                                    "         ",
                                    "M   L   M"
                            },
                            {
                                    "P   L   P",
                                    "    M    ",
                                    "         ",
                                    "         ",
                                    "LM     ML",
                                    "         ",
                                    "         ",
                                    "    M    ",
                                    "P   L   P"
                            },
                            {
                                    "L   P   L",
                                    " U  T  U ",
                                    "         ",
                                    "         ",
                                    "PT     TP",
                                    "         ",
                                    "         ",
                                    " U  T  U ",
                                    "L   P   L"
                            },
                            {
                                    "L   L   L",
                                    " V     V ",
                                    "  M   M  ",
                                    "         ",
                                    "L   I   L",
                                    "         ",
                                    "  M   M  ",
                                    " V     V ",
                                    "L   L   L"
                            },
                            {
                                    "PPEEPEEPP",
                                    "PFPOOOPFP",
                                    "SPPBBBPPN",
                                    "SOBCCCBON",
                                    "POBC0CBOP",
                                    "SOBCCCBON",
                                    "SPPBBBPPN",
                                    "PFPOOOPFP",
                                    "PPWWPWWPP"
                            }
                    },
            '0', BlockInit.ALCHECRYSITE.get(),
            'C', BlockInit.ALCHECRYSITE.get(),
            'W', PatchouliAPI.get().stateMatcher(BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.WEST)),
            'N', PatchouliAPI.get().stateMatcher(BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH)),
            'S', PatchouliAPI.get().stateMatcher(BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH)),
            'E', PatchouliAPI.get().stateMatcher(BlockInit.ALCHECRYSITE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, Direction.EAST)),
            'P', BlockInit.POLISHED_ALCHECRYSITE.get(),
            'T', PatchouliAPI.get().stateMatcher(BlockInit.POLISHED_ALCHECRYSITE_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
            'B', BlockInit.ALCHECRYSITE_BRICKS.get(),
            'L', BlockInit.ALCHECRYSITE_BRICK_WALL.get(),
            'A', PatchouliAPI.get().stateMatcher(BlockInit.ALCHECRYSITE_BRICK_SLAB.get().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)),
            'F', BlockInit.FLUORITE_BLOCK.get(),
            'O', BlockInit.FLUORITE_BRICKS.get(),
            'M', BlockInit.ALCHEMETRIC_PYLON.get(),
            'V', BlockInit.MANA_VESSEL.get(),
            'U', PatchouliAPI.get().stateMatcher(BlockInit.MANA_NODE.get().defaultBlockState().setValue(ManaNodeBlock.FACING, Direction.UP)),
            'D', PatchouliAPI.get().stateMatcher(BlockInit.MANA_NODE.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.DOWN)),
            'I', BlockInit.ATHANOR_PILLAR.get());


//    private static Map<BlockPos, BlockState> getStructure() {
//        return StructureHolder.STRUCTURE;
//    }

    public static boolean hasCorrectStructure(Level level, BlockPos pos) {
        return StructureHelper.checkMultiblock(level, STRUCTURE, pos.subtract(new Vec3i(0, 1, 0)));
    }

    public static BlockPos detectIncorrectStructurePos(Level level, BlockPos pos) {
        return StructureHelper.detectMultiblockError(level, STRUCTURE, pos.subtract(new Vec3i(0, 1, 0)), Rotation.NONE, false);
    }

}
