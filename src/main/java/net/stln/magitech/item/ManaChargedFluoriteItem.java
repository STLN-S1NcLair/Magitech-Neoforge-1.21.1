package net.stln.magitech.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.List;

public class ManaChargedFluoriteItem extends TooltipTextItem {
    public ManaChargedFluoriteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        double currentMana = ManaData.getCurrentMana(player, ManaUtil.ManaType.MANA);
        double maxMana = ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);
        if (currentMana < maxMana) {
            if (!player.isCreative()) {
                stack.setCount(stack.getCount() - 1);
            }
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.CRYSTAL_BREAK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.9F, 1.0F, 0.7F), new Vector3f(0.3F, 1.0F, 0.9F), 1F, 1, 0), player, 20);
            ManaUtil.setMana(player, ManaUtil.ManaType.MANA, Math.min(currentMana + 45, maxMana));
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.hint.item.magitech.mana_charged_fluorite").withColor(0x60A090));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
