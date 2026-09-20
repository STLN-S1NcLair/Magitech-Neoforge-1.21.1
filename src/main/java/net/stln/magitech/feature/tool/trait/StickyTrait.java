package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.material.MaterialInit;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.BlockHelper;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class StickyTrait extends Trait {

    @Override
    public void onBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, List<ItemEntity> loots) {
        addBlockBreakVFX(player, level, stack, blockState, pos);
        teleportToPlayer(player, level, loots);
    }

    @Override
    public void onEntityLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, List<ItemEntity> loots) {
        if (!loots.isEmpty()) {
            addEntityKillVFX(player, level, stack, loots.getFirst().position());
        }
        teleportToPlayer(player, level, loots);
    }

    private static void teleportToPlayer(Player player, Level level, List<ItemEntity> loots) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        for (ItemEntity itemEntity : loots) {
            itemEntity.teleportTo(
                    serverLevel,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    Set.of(),
                    player.getYRot(),
                    player.getXRot()
            );
            itemEntity.setPickUpDelay(0);
        }
    }

    @Override
    public Color getColor() {
        return new Color(0x96FF88);
    }

    @Override
    public Color getPrimary() {
        return new Color(0x83FFB4);
    }

    @Override
    public Color getSecondary() {
        return new Color(0xC5FF52);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("sticky");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
