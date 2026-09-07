package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.material.MaterialInit;
import net.stln.magitech.feature.tool.property.ToolProperties;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ScorchedTrait extends Trait {

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.setRemainingFireTicks(livingEntity.getRemainingFireTicks() + 60 * traitLevel);
            if (level.isClientSide) {
                PointVFX.burst(level, livingEntity.position().add(0, livingEntity.getBbHeight() / 2, 0), Element.EMBER, ElementParticles::smokeParticle, 3, 0.1F);
                PointVFX.burstSquare(level, livingEntity.position().add(0, livingEntity.getBbHeight() / 2, 0), Element.EMBER, 5, 0.1F);
            }
        }
    }
    @Override
    public void onBlockLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, List<ItemEntity> loots) {
        addBlockBreakVFX(player, level, stack, blockState, pos, MaterialInit.NETHER_BRICK);
        smeltLoot(level, loots);
    }

    @Override
    public void onEntityLooting(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, List<ItemEntity> loots) {
        if (!loots.isEmpty()) {
            addEntityKillVFX(player, level, stack, loots.getFirst().position(), MaterialInit.NETHER_BRICK);
        }
        smeltLoot(level, loots);
    }

    private static void smeltLoot(Level level, List<ItemEntity> loots) {
        for (ItemEntity itemEntity : loots) {
            ItemStack stack = itemEntity.getItem();
            SingleRecipeInput input = new SingleRecipeInput(stack);
            Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, level);
            if (recipe.isEmpty()) {
                continue;
            }

            ItemStack smeltedStack = recipe.get().value().assemble(input, level.registryAccess());
            if (!smeltedStack.isEmpty()) {
                ItemEntity newItem = new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), smeltedStack.copyWithCount(smeltedStack.getCount() * stack.getCount()));
                itemEntity.discard();
                level.addFreshEntity(newItem);
            }
        }
    }

    @Override
    public Color getColor() {
        return new Color(0x8C3738);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xFF9457);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x8B4C5D);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("scorched");
    }
}
