package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.content.network.TraitActionPayload;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Trait {

    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return new ArrayList<>();
    }

    public void modifyAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, List<ItemAttributeModifiers.Entry> entries) {
    }

    public Boolean modifyCorrectTool(ItemStack stack, int traitLevel, BlockState blockState) {
        return null;
    }

    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos) {
        return 1.0F;
    }

    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return false;
    }

    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        if (!isInitial) {
            SoundType soundType = level.getBlockState(pos).getSoundType(level, pos, player);
            level.playSound(player, pos, soundType.getBreakSound(), SoundSource.PLAYERS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            level.addDestroyBlockEffect(pos, blockState);
        }
    }

    public void emitBlockBreakParticle(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {

    }

    public Set<BlockPos> additionalBlockBreak(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        Set<BlockPos> posSet = new HashSet<>();
        posSet.add(pos);
        return posSet;
    }

    public void modifyEnchantmentOnBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, List<ItemStack> lootStack) {
    }

    // EXP倍率で指定すること
    public double modifyExpOnBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, List<ItemStack> lootStack, int exp) {
        return 1.0;
    }

    public List<ItemStack> setItemOnBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, List<ItemStack> lootStack) {
        return List.of();
    }

    public List<ItemStack> addItemOnBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, List<ItemStack> lootStack) {
        return List.of();
    }

    public int addEnchantments(ItemStack stack, int traitLevel, ToolProperties properties, Holder<Enchantment> enchantmentHolder, int enchantmentLevel) {
        return 0;
    }

    public int enhanceEnchantments(ItemStack stack, int traitLevel, ToolProperties properties, Holder<Enchantment> enchantmentHolder, int enchantmentLevel) {
        return 0;
    }

    public void onCastSpell(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
    }

    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, Entity target) {
    }

    public void traitAction(Player player, Level level, Entity target, Vec3 lookingPos, ItemStack stack, int traitLevel, ToolProperties properties, InteractionHand hand, boolean isHost) {
        int id = target != null ? target.getId() : -1;
        if (isHost) {
            if (level.isClientSide) {
                PacketDistributor.sendToServer(new TraitActionPayload(hand == InteractionHand.MAIN_HAND, id, lookingPos, player.getUUID()));
            } else {
                PacketDistributor.sendToAllPlayers(new TraitActionPayload(hand == InteractionHand.MAIN_HAND, id, lookingPos, player.getUUID()));
            }
        }
    }

    public void handTick(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, boolean isHost) {
        if (level.isClientSide && effectEnabled(player, level, stack, traitLevel, properties)) {
            EntityVFX.powerupAura(level, this, player, 0.5F);
        }
    }

    public void inventoryTick(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, boolean isHost) {
    }

    public void onRepair(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, int repairAmount) {
    }

    public void testRepair(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, int repairAmount) {
    }

    public abstract Color getColor();

    public Color getPrimary() {
        return new Color(0xFFFFFF);
    }

    public Color getSecondary() {
        return new Color(0xFFFFFF);
    }

    public abstract ResourceLocation getKey();

    public Component getName() {
        ResourceLocation key = getKey();
        return Component.translatable("trait." + key.getNamespace() + "." + key.getPath());
    }

    public MutableComponent getComponent() {
        return getName().copy().withColor(getColor().getRGB());
    }

    public void addDescription(List<Component> components) {
        ResourceLocation key = this.getKey();
        MutableComponent component = Component.translatable("trait." + key.getNamespace() + ".description." + key.getPath());
        String[] lines = component.getString().split("\n");

        for (String line : lines) {
            components.add(Component.literal(line).withColor(0x808080));
        }
    }

    public int getMaxLevel() {
        return -1;
    }
}
