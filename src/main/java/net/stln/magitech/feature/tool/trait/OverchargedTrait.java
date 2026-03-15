package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.core.api.mana.handler.EntityManaHelper;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.helper.EffectHelper;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class OverchargedTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (EntityManaHelper.getMagicManaFillRatio(player) >= 1) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.25F;
            Float elmAtk = stats.getStats().get(ToolStats.ELM_ATK_STAT);
            float mul2 = traitLevel * 0.35F;
            Float min = stats.getStats().get(ToolStats.MIN_STAT);
            modified.put(ToolStats.ELM_ATK_STAT, elmAtk * mul);
            modified.put(ToolStats.MIN_STAT, min * mul2);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (EntityManaHelper.getMagicManaFillRatio(player) >= 0.5) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.35F;
            Float pwr = stats.getStats().get(ToolStats.PWR_STAT);
            Float chg = stats.getStats().get(ToolStats.CHG_STAT);
            modified.put(ToolStats.PWR_STAT, pwr * mul);
            modified.put(ToolStats.CHG_STAT, chg * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifySpellCasterStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (EntityManaHelper.getMagicManaFillRatio(player) >= (stack.getItem() instanceof SpellCasterItem ? 0.5 : 1)) {
            EffectHelper.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.9F, 1.0F, 0.7F), new Vector3f(0.3F, 1.0F, 0.9F), 1F, 1, 0, 15, 1.0F), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0x80FFC0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.overcharged");
    }
}
