package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SmoothTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (!level.canSeeSkyFromBelowWater(player.blockPosition())) {
            List<ToolMaterial> materials = ComponentHelper.getPartMaterials(stack);
            Set<ToolMaterial> materialSet = PartToolItem.getMaterialSet(materials);
            ToolStats defaultStats = ToolStats.DEFAULT;
            Map<String, Float> statsMap = stats.getStats();
            Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
            modified.put(ToolStats.RNG_STAT, statsMap.get(ToolStats.RNG_STAT) * traitLevel * 0.1F);
            return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
        }
        return super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (!level.canSeeSkyFromBelowWater(player.blockPosition())) {
            List<ToolMaterial> materials = ComponentHelper.getPartMaterials(stack);
            Set<ToolMaterial> materialSet = PartToolItem.getMaterialSet(materials);
            ToolStats defaultStats = ToolStats.DEFAULT;
            Map<String, Float> statsMap = stats.getStats();
            Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
            modified.put(ToolStats.PRJ_STAT, statsMap.get(ToolStats.PRJ_STAT) * traitLevel * 0.1F);
            return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
        }
        return super.modifySpellCasterStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public void modifyEnchantmentOnBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, List<ItemStack> lootStack) {
        if (!level.canSeeSkyFromBelowWater(player.blockPosition())) {
            Holder<Enchantment> silkTouch = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH);
            stack.enchant(silkTouch, 1);
        }
        super.modifyEnchantmentOnBlockLooting(player, level, stack, traitLevel, stats, blockState, pos, lootStack);
    }

    @Override
    public double modifyExpOnBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, List<ItemStack> lootStack, int exp) {
        if (!level.canSeeSkyFromBelowWater(player.blockPosition())) {
            return 0.0; // No experience gained when the trait is active
        }
        return super.modifyExpOnBlockLooting(player, level, stack, traitLevel, stats, blockState, pos, lootStack, exp);
    }

    @Override
    public boolean emitEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        return !level.canSeeSkyFromBelowWater(player.blockPosition());
    }

    @Override
    public void addEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.addEffect(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new PowerupParticleEffect(new Vector3f(1.0F, 1.0F, 0.9F), new Vector3f(1.0F, 0.9F, 0.7F), 1F, 1, 0, 15, 1.0F),
                    pos.getX() + player.getRandom().nextFloat(), pos.getY() + player.getRandom().nextFloat(), pos.getZ() + player.getRandom().nextFloat(), 0, 0, 0);
        }
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (!level.canSeeSkyFromBelowWater(player.blockPosition())) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 1.0F, 0.9F), new Vector3f(1.0F, 0.9F, 0.7F), 1F, 1, 0, 15, 1.0F), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0xFFF8E0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.smooth");
    }

}
