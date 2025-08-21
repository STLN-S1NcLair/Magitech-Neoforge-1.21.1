package net.stln.magitech.item.tool.trait;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class BrillianceTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light >= 10) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.25F;
            Float swp = stats.getStats().get(ToolStats.SWP_STAT);
            modified.put(ToolStats.SWP_STAT, swp * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light >= 10) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.25F;
            Float swp = stats.getStats().get(ToolStats.SWP_STAT);
            Float min = stats.getStats().get(ToolStats.MIN_STAT);
            modified.put(ToolStats.SWP_STAT, swp * mul);
            modified.put(ToolStats.MIN_STAT, min * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifySpellCasterStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light >= 10) {
            float mul = traitLevel * 0.25F;
            Float min = stats.getStats().get(ToolStats.MIN_STAT);
            return min * mul;
        }
        return super.modifyMiningSpeed(player, level, stack, traitLevel, stats, blockState, pos);
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light <= 3) {
                EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.85F, 0.8F, 1.0F), new Vector3f(0.85F, 0.8F, 1.0F), 1F, 1, 0F), player, 1);
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false));
        } else if (light >= 10) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.85F, 0.8F, 1.0F), new Vector3f(0.85F, 0.8F, 1.0F), 1F, 1, 0F), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0xD8D0FF;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.brilliance");
    }
}
