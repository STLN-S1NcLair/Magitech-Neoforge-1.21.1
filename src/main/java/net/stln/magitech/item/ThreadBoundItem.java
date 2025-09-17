package net.stln.magitech.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.Element;
import net.stln.magitech.util.RegistryHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.PatchouliDataComponents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadBoundItem extends TooltipTextItem implements ICurioItem {

    Map<Holder<Attribute>, AttributeModifier> attributeModifiers = new HashMap<>();

    public ThreadBoundItem(Properties settings) {
        super(settings);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> modifierMultimap = ImmutableMultimap.builder();
        for (Map.Entry<Holder<Attribute>, AttributeModifier> modifier : attributeModifiers.entrySet()) {
            modifierMultimap.put(modifier.getKey(), modifier.getValue());
        }
        return modifierMultimap.build();
    }

    public ThreadBoundItem attributeModifier(Map<Holder<Attribute>, AttributeModifier> map) {
        attributeModifiers = map;
        return this;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if (level.isClientSide && ModList.get().isLoaded("patchouli")) {
            Book book = BookRegistry.INSTANCE.books.get(player.getItemInHand(usedHand).get(PatchouliDataComponents.BOOK));
            if (book != null) {
                PatchouliAPI.get().openBookGUI(book.id);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int i = 0;
        @NotNull SpellComponent spells = ComponentHelper.getSpells(stack);
        for (Holder<Spell> holder : spells.spells()) {
            ResourceLocation location = RegistryHelper.getIdOrNull(holder);
            if (location != null) {
                int abs = Math.abs(spells.selected() - i);
                if (abs <= 2 || Screen.hasShiftDown()) {
                    Element element = holder.value().getElement();
                    if (spells.selected() == i) {
                        tooltipComponents.add(Component.literal("> ").append(Component.translatable("spell." + location.getNamespace() + "." + location.getPath())).withColor(element.getSpellColor()));
                    } else {
                        tooltipComponents.add(Component.translatable("spell." + location.getNamespace() + "." + location.getPath()).withColor(element.getSpellDark()));
                    }
                } else if (abs == 3) {
                    tooltipComponents.add(Component.literal("...").withColor(0x405060));
                }
            }
            i++;
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
