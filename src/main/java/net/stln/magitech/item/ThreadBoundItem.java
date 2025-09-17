package net.stln.magitech.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.klikli_dev.modonomicon.item.ModonomiconItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadBoundItem extends ModonomiconItem implements ICurioItem {

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

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
//        if (level.isClientSide && ModList.get().isLoaded("patchouli")) {
//            Book book = BookRegistry.INSTANCE.books.get(player.getItemInHand(usedHand).get(PatchouliDataComponents.BOOK));
//            if (book != null) {
//                PatchouliAPI.get().openBookGUI(book.id);
//            }
//        }
//        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(usedHand));
//    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(ComponentInit.SPELL_COMPONENT)) {
            int i = 0;
            for (Spell spell : stack.get(ComponentInit.SPELL_COMPONENT).spells()) {
                ResourceLocation location = SpellRegister.getId(spell);
                if (location != null) {
                    if (Math.abs(stack.get(ComponentInit.SPELL_COMPONENT).selected() - i) <= 2 || Screen.hasShiftDown()) {
                        if (stack.get(ComponentInit.SPELL_COMPONENT).selected() == i) {
                            tooltipComponents.add(Component.literal("> ").append(Component.translatable("spell." + location.getNamespace() + "." + location.getPath())).withColor(spell.getElement().getSpellColor()));
                        } else {
                            tooltipComponents.add(Component.translatable("spell." + location.getNamespace() + "." + location.getPath()).withColor(spell.getElement().getSpellDark()));
                        }
                    } else if (Math.abs(stack.get(ComponentInit.SPELL_COMPONENT).selected() - i) == 3) {
                        tooltipComponents.add(Component.literal("...").withColor(0x405060));
                    }
                }
                i++;
            }
        }
        tooltipComponents.add(Component.translatable("tooltip.item." + stack.getItem().toString().replace(":", ".")).withColor(0x808080));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
