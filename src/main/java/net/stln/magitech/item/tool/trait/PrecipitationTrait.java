package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.BlockUtil;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrecipitationTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (player.isInWater() || level.isRainingAt(player.blockPosition())) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.45F;
            Float atk = stats.getStats().get(ToolStats.ATK_STAT);
            modified.put(ToolStats.ATK_STAT, atk * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (player.isInWater() || level.isRainingAt(player.blockPosition())) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.45F;
            Float atk = stats.getStats().get(ToolStats.ATK_STAT);
            modified.put(ToolStats.ATK_STAT, atk * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifySpellCasterStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public Set<BlockPos> addAdditionalBlockBreakSecond(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        boolean flag = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        for (int i = 0; i < 4; i++) {
            flag &= level.getBlockState(pos.above(i)).getBlock().equals(blockState.getBlock());
            flag2 &= level.getBlockState(pos.above(i - 1)).getBlock().equals(blockState.getBlock());
            flag3 &= level.getBlockState(pos.above(i - 2)).getBlock().equals(blockState.getBlock());
            flag4 &= level.getBlockState(pos.above(i - 3)).getBlock().equals(blockState.getBlock());
        }
        if (flag || flag2 || flag3 || flag4) {
            return BlockUtil.getConnectedBlocks(level, pos, blockState.getBlock(), traitLevel * 5);
        }
        return new HashSet<>();
    }

    @Override
    public boolean emitEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        return !isInitial;
    }

    @Override
    public void addEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.addEffect(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new PowerupParticleEffect(new Vector3f(0.8F, 0.7F, 0.5F), new Vector3f(0.8F, 0.7F, 0.5F), 1F, 1, 0, 15, 1.0F),
                    pos.getX() + player.getRandom().nextFloat(), pos.getY() + player.getRandom().nextFloat(), pos.getZ() + player.getRandom().nextFloat(), 0, 0, 0);
        }
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (player.isInWater()) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.8F, 0.7F, 0.5F), new Vector3f(0.8F, 0.7F, 0.5F), 1F, 1, 0, 15, 1.0F), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0xE0B090;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.precipitation");
    }

}
