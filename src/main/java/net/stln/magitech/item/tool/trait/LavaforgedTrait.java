package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.LevelStem;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class LavaforgedTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (player.position().y < 0 || player.level().dimension().equals(LevelStem.NETHER)) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.08F;
            Float atk = PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.ATK_STAT);
            modified.put(ToolStats.ATK_STAT, atk * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel());
        }
        return super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        if (player.position().y < 0 || player.level().dimension().equals(LevelStem.NETHER)) {
            float mul = traitLevel * 0.1F;
            Float min = PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.MIN_STAT);
            return min * mul;
        }
        return super.modifyMiningSpeed(player, level, stack, traitLevel, stats, blockState, pos);
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        super.tick(player, level, stack, traitLevel, stats);
        if (player.position().y < 0 || player.level().dimension().equals(LevelStem.NETHER)) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 0.25F, 0F), new Vector3f(1.0F, 0.25F, 0F), 1F, 1, 0), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0x802000;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.lavaforged");
    }
}
