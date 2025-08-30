package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class OverchargedTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        double currentMana = ManaData.getCurrentMana(player, ManaUtil.ManaType.MANA);
        double maxMana = ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);
        if (currentMana >= maxMana) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.25F;
            Float elmAtk = stats.getStats().get(ToolStats.ELM_ATK_STAT);
            modified.put(ToolStats.ELM_ATK_STAT, elmAtk * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        double currentMana = ManaData.getCurrentMana(player, ManaUtil.ManaType.MANA);
        double maxMana = ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);
        if (currentMana >= maxMana / 2) {
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
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        double currentMana = ManaData.getCurrentMana(player, ManaUtil.ManaType.MANA);
        double maxMana = ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);
        if (currentMana >= maxMana) {
            float mul = traitLevel * 0.35F;
            Float min = stats.getStats().get(ToolStats.MIN_STAT);
            return min * mul;
        }
        return super.modifyMiningSpeed(player, level, stack, traitLevel, stats, blockState, pos);
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        double currentMana = ManaData.getCurrentMana(player, ManaUtil.ManaType.MANA);
        double maxMana = ManaUtil.getMaxMana(player, ManaUtil.ManaType.MANA);
        if (currentMana >= maxMana / (stack.getItem() instanceof SpellCasterItem ? 2 : 1)) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.9F, 1.0F, 0.7F), new Vector3f(0.3F, 1.0F, 0.9F), 1F, 1, 0), player, 1);
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
