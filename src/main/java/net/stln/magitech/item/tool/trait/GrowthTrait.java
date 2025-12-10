package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

public class GrowthTrait extends Trait {

    @Override
    public void inventoryTick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.inventoryTick(player, level, stack, traitLevel, stats, isHost);
        if (level.canSeeSky(player.blockPosition()) && stack.isDamaged()) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.8F, 1.0F, 0.5F), new Vector3f(0.4F, 0.5F, 0.25F), 1F, 1, 0, 15, 1.0F), player, 1);
            if (level.getGameTime() % (600 / traitLevel) == 0 && !level.isClientSide && isHost) {
                stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
            }
        }
    }

    @Override
    public int getColor() {
        return 0x607040;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.growth");
    }

}
