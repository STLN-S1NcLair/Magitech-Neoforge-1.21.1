package net.stln.magitech.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RingItem extends TooltipTextItem implements ICurioItem {

    Map<Holder<Attribute>, AttributeModifier> attributeModifiers = new HashMap<>();

    public RingItem(Properties settings) {
        super(settings);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> modifierMultimap = ImmutableMultimap.builder();
        for (Map.Entry<Holder<Attribute>, AttributeModifier> modifier : attributeModifiers.entrySet()) {
            ResourceLocation location = ResourceLocation.tryParse(modifier.getValue().id().toString());
            AttributeModifier attributeModifier = new AttributeModifier(location.withSuffix(generateUUID(slotContext, stack).toString()), modifier.getValue().amount(), modifier.getValue().operation());
            modifierMultimap.put(modifier.getKey(), attributeModifier);
        }
        return modifierMultimap.build();
    }

    private UUID generateUUID(SlotContext slotContext, ItemStack stack) {
        String base = slotContext.identifier() + "|" + slotContext.index() + "|" + stack.getItem().getDescriptionId() + "|" + stack.getCount();
        return UUID.nameUUIDFromBytes(base.getBytes(StandardCharsets.UTF_8));
    }

    public RingItem attributeModifier(Map<Holder<Attribute>, AttributeModifier> map) {
        attributeModifiers = map;
        return this;
    }
}
