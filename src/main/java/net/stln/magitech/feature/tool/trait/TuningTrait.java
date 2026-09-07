package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.cooldown.CooldownData;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.tool.property.ElementalAttributeToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.helper.BlockHelper;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class TuningTrait extends Trait {

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, Entity target) {
        shortenCooldown(player, traitLevel, properties);
        super.onDamageEntity(player, level, stack, traitLevel, properties, target);
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        shortenCooldown(player, traitLevel, properties);
        super.onBreakBlock(player, level, stack, traitLevel, properties, blockState, pos, damageAmount, isInitial);
    }

    private void shortenCooldown(Player player, int traitLevel, ToolProperties properties) {
        ElementalAttributeToolProperty prop = (ElementalAttributeToolProperty) ToolPropertyInit.ELEMENTAL_DAMAGE.asToolProperty();
        Element element = prop.getElement(properties.getOrId(prop));
        for (Map.Entry<ISpell, CooldownData.Cooldown> entry : player.getData(DataAttachmentInit.SPELL_COOLDOWNS).cooldowns().entrySet()) {
            ISpell spell = entry.getKey();
            CooldownData.Cooldown cooldown = entry.getValue();
            if (spell.getConfig().element() == element) {
                cooldown = cooldown.reduceCooldown(traitLevel * 10);
                entry.setValue(cooldown);
            }
        }
    }

    @Override
    public Color getColor() {
        return new Color(0xA6DFCA);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xB1FFCE);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x9697EC);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("tuning");
    }

}
