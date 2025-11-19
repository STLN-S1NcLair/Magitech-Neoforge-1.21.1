package net.stln.magitech.item.fluid.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.fluid.DrinkableFlaskItem;

public class WaterFlaskItem extends DrinkableFlaskItem {

    public WaterFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Level level, LivingEntity entity, ItemStack stack) {
    }
}
