package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.stln.magitech.item.tool.ToolStats;
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
            float mul = traitLevel * 0.35F;
            Float atk = stats.getStats().get(ToolStats.ATK_STAT);
            Float min = stats.getStats().get(ToolStats.MIN_STAT);
            modified.put(ToolStats.ATK_STAT, atk * mul);
            modified.put(ToolStats.MIN_STAT, min * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (player.position().y < 0 || player.level().dimension().equals(LevelStem.NETHER)) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.35F;
            Float atk = stats.getStats().get(ToolStats.ATK_STAT);
            modified.put(ToolStats.ATK_STAT, atk * mul);
            Float chg = stats.getStats().get(ToolStats.CHG_STAT);
            modified.put(ToolStats.CHG_STAT, chg * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifySpellCasterStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (player.position().y < 0 || player.level().dimension().equals(LevelStem.NETHER)) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 0.25F, 0F), new Vector3f(1.0F, 0.25F, 0F), 1F, 1, 0, 15, 1.0F), player, 1);
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
