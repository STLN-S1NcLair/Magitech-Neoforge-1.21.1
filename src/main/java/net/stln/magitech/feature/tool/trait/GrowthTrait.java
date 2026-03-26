package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.EffectHelper;
import org.joml.Vector3f;

import java.awt.*;

public class GrowthTrait extends Trait {

    @Override
    public void inventoryTick(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, boolean isHost) {
        if (effectEnabled(player, level, stack, traitLevel, properties)) {
            EffectHelper.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.8F, 1.0F, 0.5F), new Vector3f(0.4F, 0.5F, 0.25F), 1F, 1, 0, 15, 1.0F), player, 1);
            if (level.getGameTime() % (300 / traitLevel) == 0 && !level.isClientSide && isHost) {
                stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
            }
        }
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return level.canSeeSky(player.blockPosition()) && stack.isDamaged();
    }

    @Override
    public Color getColor() {
        return new Color(0x607040);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xDCE686);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x88D856);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.growth");
    }

}
