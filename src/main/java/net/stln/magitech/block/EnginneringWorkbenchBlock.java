package net.stln.magitech.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.stln.magitech.gui.PartCuttingMenu;

public class EnginneringWorkbenchBlock extends Block {
    public static final MapCodec<EnginneringWorkbenchBlock> CODEC = simpleCodec(EnginneringWorkbenchBlock::new);
    private static final Component CONTAINER_TITLE = Component.translatable("recipe.magitech.part_cutting");

    public EnginneringWorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (p_52229_, p_52230_, p_52231_) -> new PartCuttingMenu(p_52229_, p_52230_, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE
        );
    }
}
