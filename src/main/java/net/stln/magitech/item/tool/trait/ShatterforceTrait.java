package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ShatterforceTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        ToolStats aDefault = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(aDefault.getStats());
        float mul = traitLevel * 0.35F * stack.getDamageValue() / stack.getMaxDamage();
        Float swp = stats.getStats().get(ToolStats.SWP_STAT);
        Float min = stats.getStats().get(ToolStats.MIN_STAT);
        modified.put(ToolStats.SWP_STAT, swp * mul);
        modified.put(ToolStats.MIN_STAT, min * mul);
        return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        ToolStats aDefault = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(aDefault.getStats());
        float mul = traitLevel * 0.35F * stack.getDamageValue() / stack.getMaxDamage();
        Float swp = stats.getStats().get(ToolStats.SWP_STAT);
        modified.put(ToolStats.SWP_STAT, swp * mul);
        return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (player.getRandom().nextFloat() < (float) stack.getDamageValue() / stack.getMaxDamage()) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.9F, 0.6F, 1.0F), new Vector3f(0.9F, 0.6F, 1.0F), 1F, 1, 0, 15, 1.0F), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0xF0A0FF;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.shatterforce");
    }
}
