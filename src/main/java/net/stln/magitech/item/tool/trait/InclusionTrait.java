package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import org.joml.Vector3f;

public class InclusionTrait extends Trait {

    @Override
    public int addEnchantments(ItemStack stack, int traitLevel, ToolStats stats, Holder<Enchantment> enchantmentHolder, int enchantmentLevel) {
        if (enchantmentHolder.is(Enchantments.FORTUNE) || enchantmentHolder.is(Enchantments.LOOTING)) {
            return traitLevel;
        }
        return super.addEnchantments(stack, traitLevel, stats, enchantmentHolder, enchantmentLevel);
    }

    @Override
    public boolean emitEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        return true;
    }

    @Override
    public void addEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.addEffect(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new PowerupParticleEffect(new Vector3f(0.1F, 0.3F, 1.0F), new Vector3f(0.0F, 0.1F, 0.5F), 1F, 1, 0, 15, 1.0F),
                    pos.getX() + player.getRandom().nextFloat(), pos.getY() + player.getRandom().nextFloat(), pos.getZ() + player.getRandom().nextFloat(), 0, 0, 0);
        }
    }

    @Override
    public int getColor() {
        return 0x2040A0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.inclusion");
    }

}
