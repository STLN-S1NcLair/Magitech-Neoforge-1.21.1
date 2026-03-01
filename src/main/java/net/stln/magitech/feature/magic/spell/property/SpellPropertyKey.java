package net.stln.magitech.feature.magic.spell.property;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.feature.magic.spell.ISpell;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public record SpellPropertyKey<T>(String id, Function4<ISpell, LivingEntity, ItemStack, SpellPropertyKey<T>, MutableComponent> textDecoration) {

    public MutableComponent getDisplayName() {
        return Component.translatable("spell.magitech.property." + id);
    }

    public MutableComponent getDisplayText(ISpell spell, LivingEntity caster, @Nullable ItemStack wand) {
        return textDecoration.apply(spell, caster, wand, this);
    }

}
