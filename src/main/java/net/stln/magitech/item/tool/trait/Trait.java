package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.network.TraitActionPayload;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Trait {

    public ToolStats modifyStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifyStats2(ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifyStats3(ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifyStatsConditional2(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifyStatsConditional3(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public void modifyAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, List<ItemAttributeModifiers.Entry> entries) {
    }

    public ToolStats modifySpellCasterStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifySpellCasterStats2(ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifySpellCasterStats3(ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifySpellCasterStatsConditional2(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public ToolStats modifySpellCasterStatsConditional3(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        return ToolStats.DEFAULT;
    }

    public void modifySpellCasterAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, List<ItemAttributeModifiers.Entry> entries) {
    }

    public Boolean isCorrectTool(ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState) {
        return null;
    }

    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        return 0;
    }

    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        if (!isInitial) {
            SoundType soundType = level.getBlockState(pos).getSoundType(level, pos, player);
            level.playSound(player, pos, soundType.getBreakSound(), SoundSource.PLAYERS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            level.addDestroyBlockEffect(pos, blockState);
        }
    }

    public boolean emitEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        return false;
    }

    public void addEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {

    }

    public Set<BlockPos> addAdditionalBlockBreakFirst(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        Set<BlockPos> posSet = new HashSet<>();
        posSet.add(pos);
        return posSet;
    }

    public Set<BlockPos> addAdditionalBlockBreakSecond(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        Set<BlockPos> posSet = new HashSet<>();
        posSet.add(pos);
        return posSet;
    }

    public void onAttackEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
    }

    public void onCastSpell(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
    }

    public void traitAction(Player player, Level level, Entity target, Vec3 lookingPos, ItemStack stack, int traitLevel, ToolStats stats, InteractionHand hand, boolean isHost) {
        int id = target != null ? target.getId() : -1;
        if (isHost) {
            if (level.isClientSide) {
                PacketDistributor.sendToServer(new TraitActionPayload(hand == InteractionHand.MAIN_HAND, id, lookingPos, player.getUUID().toString()));
            } else {
                PacketDistributor.sendToAllPlayers(new TraitActionPayload(hand == InteractionHand.MAIN_HAND, id, lookingPos, player.getUUID().toString()));
            }
        }
    }

    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
    }

    public void onRepair(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, int repairAmount) {
    }

    public int getColor() {
        return 0xFFFFFF;
    }

    public Component getName() {
        return Component.empty();
    }

    public MutableComponent getComponent() {
        return getName().copy().withColor(getColor());
    }

    public int getMaxLevel() {
        return -1;
    }
}
