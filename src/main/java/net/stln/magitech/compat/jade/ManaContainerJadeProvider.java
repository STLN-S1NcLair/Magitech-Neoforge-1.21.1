package net.stln.magitech.compat.jade;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.block_entity.ManaContainerBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

public class ManaContainerJadeProvider implements IBlockComponentProvider {

    public static final ResourceLocation UID = Magitech.id("mana_container");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockPos pos = blockAccessor.getPosition();
        BlockState state = blockAccessor.getBlockState();
        Level level = blockAccessor.getLevel();
        if (level.getBlockEntity(pos) instanceof ManaContainerBlockEntity container) {
            int mana = container.getMana();
            int maxMana = container.getMaxMana();
            float progress = (float) mana / maxMana;

            iTooltip.add(
                    IElementHelper.get().progress(
                            progress,
                            Component.literal(mana + " / " + maxMana + " kJ"),
                            IElementHelper.get().progressStyle().color(0xA0FFD0).textColor(0xFFFFFF), // ゲージ色
                            BoxStyle.getNestedBox(),
                            true
                    )
            );
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
