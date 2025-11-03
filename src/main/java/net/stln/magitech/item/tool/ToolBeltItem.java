package net.stln.magitech.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.stln.magitech.block.block_entity.ManaContainerBlockEntity;
import net.stln.magitech.item.TooltipTextItem;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.EffectUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;

public class ToolBeltItem extends TooltipTextItem {
    public ToolBeltItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
//        tooltipComponents.add(Component.translatable("tooltip.hint.item.magitech.aggregated_fluxia").withColor(0xC0F0FF));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
