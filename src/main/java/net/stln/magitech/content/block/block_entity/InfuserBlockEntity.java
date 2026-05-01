package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.InfuserBlock;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ManaNodeBlock;
import net.stln.magitech.content.gui.EnhancedManaVesselMenu;
import net.stln.magitech.content.gui.InfuserMenu;
import net.stln.magitech.content.recipe.AthanorPillarInfusionRecipe;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.recipe.input.GroupedMultiStackRecipeInput;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.particle.particle_option.ManaZapParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.StructureHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfuserBlockEntity extends InfusionAltarBlockEntity {

    public InfuserBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.INFUSER_ENTITY.get(), pos, blockState, mana);
        this.maxProgress = 100;
    }

    public InfuserBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new InfuserMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.magitech.infuser");
    }
}
