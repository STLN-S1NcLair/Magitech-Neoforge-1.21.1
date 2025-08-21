package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ConcentrationTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (player.getHealth() == player.getMaxHealth() && player.getFoodData().getFoodLevel() <= 18 && player.getFoodData().getFoodLevel() >= 10) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.5F;
            Float atk = stats.getStats().get(ToolStats.ATK_STAT);
            modified.put(ToolStats.ATK_STAT, atk * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (player.getHealth() == player.getMaxHealth() && player.getFoodData().getFoodLevel() <= 18 && player.getFoodData().getFoodLevel() >= 10) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.5F;
            Float atk = stats.getStats().get(ToolStats.ATK_STAT);
            Float cld = stats.getStats().get(ToolStats.CLD_STAT);
            modified.put(ToolStats.ATK_STAT, atk * mul);
            modified.put(ToolStats.CLD_STAT, cld * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifySpellCasterStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        if (player.getHealth() == player.getMaxHealth() && player.getFoodData().getFoodLevel() <= 18 && player.getFoodData().getFoodLevel() >= 10) {
            float mul = traitLevel * 0.5F;
            Float min = stats.getStats().get(ToolStats.MIN_STAT);
            return min * mul;
        }
        return super.modifyMiningSpeed(player, level, stack, traitLevel, stats, blockState, pos);
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (player.getHealth() == player.getMaxHealth() && player.getFoodData().getFoodLevel() <= 18 && player.getFoodData().getFoodLevel() >= 10) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.5F, 1.0F, 0.7F), new Vector3f(0.3F, 1.0F, 0.4F), 1F, 1, 0), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0x40FF50;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.concentration");
    }
}
