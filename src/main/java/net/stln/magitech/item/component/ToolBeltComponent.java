package net.stln.magitech.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.event.ItemSwitchDetector;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellLike;

import java.util.List;

public record ToolBeltComponent(List<ItemStack> stacks) {
    public static final Codec<ToolBeltComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.OPTIONAL_CODEC.listOf(8, 8).fieldOf("stacks").forGetter(ToolBeltComponent::stacks)
    ).apply(instance, ToolBeltComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolBeltComponent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(8)),
            ToolBeltComponent::stacks,
            ToolBeltComponent::new
    );

    public static final ToolBeltComponent EMPTY = new ToolBeltComponent(List.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY));

    public ToolBeltComponent changeStack(ItemStack newStack, int index) {
        List<ItemStack> newStacks = new java.util.ArrayList<>(List.copyOf(this.stacks));
        if (index < 0 || index >= newStacks.size()) {
            return this;
        }
        newStacks.set(index, newStack);
        return new ToolBeltComponent(newStacks);
    }
}
