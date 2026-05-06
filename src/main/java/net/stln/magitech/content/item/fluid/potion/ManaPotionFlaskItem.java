package net.stln.magitech.content.item.fluid.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.item.fluid.DrinkableFlaskItem;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.handler.EntityManaHandler;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.EffectHelper;
import org.joml.Vector3f;

public class ManaPotionFlaskItem extends DrinkableFlaskItem {

    public ManaPotionFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Level level, LivingEntity entity, ItemStack stack) {
        EntityManaHandler handler = entity.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            handler.addMana(90000);
        }

        EntityVFX.powerupAura(level, Element.MANA, entity, Section.cover(), 40);
        entity.addEffect(new MobEffectInstance(MobEffectInit.MANA_REGENERATION, 300, 1));
    }
}
