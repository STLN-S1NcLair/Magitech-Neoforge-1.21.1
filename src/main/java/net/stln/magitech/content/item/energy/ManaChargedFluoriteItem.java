package net.stln.magitech.content.item.energy;

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
import net.stln.magitech.content.item.tooltip_item.TooltipTextItem;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.core.api.mana.handler.EntityManaHandler;
import net.stln.magitech.core.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.helper.EffectHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;

public class ManaChargedFluoriteItem extends TooltipTextItem {
    public ManaChargedFluoriteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        EntityManaHandler handler = player.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            long currentMana = handler.getMana();
            long maxMana = handler.getMaxMana();
            if (currentMana < maxMana) {
                if (!player.isCreative()) {
                    stack.setCount(stack.getCount() - 1);
                }
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.CRYSTAL_BREAK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                EffectHelper.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.9F, 1.0F, 0.7F), new Vector3f(0.3F, 1.0F, 0.9F), 1F, 1, 0, 15, 1.0F), player, 20);
                handler.addMana(45000);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockEntity entity = level.getBlockEntity(pos);
        if (ManaTransferHelper.getManaContainer(level, pos, null) instanceof IBasicManaHandler handler) {
            if (!player.isCreative()) {
                stack.setCount(stack.getCount() - 1);
            }
            handler.addMana(45000);
            level.playSound(player, pos, SoundInit.CRYSTAL_BREAK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            for (int i = 0; i < 40; i++) {
                double x = pos.getCenter().x + Mth.nextDouble(player.getRandom(), -0.75, 0.75);
                double y = pos.getCenter().y + Mth.nextDouble(player.getRandom(), -0.75, 0.75);
                double z = pos.getCenter().z + Mth.nextDouble(player.getRandom(), -0.75, 0.75);
                level.addParticle(new PowerupParticleEffect(new Vector3f(0.9F, 1.0F, 0.7F), new Vector3f(0.3F, 1.0F, 0.9F), 1F, 1, 0, 15, 1.0F), x, y, z, 0, 0, 0);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.hint.item.magitech.mana_charged_fluorite").withColor(0x60A090));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
