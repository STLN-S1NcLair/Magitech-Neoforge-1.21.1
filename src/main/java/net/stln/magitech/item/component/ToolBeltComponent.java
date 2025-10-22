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
            ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(ToolBeltComponent::stacks)
    ).apply(instance, ToolBeltComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolBeltComponent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ToolBeltComponent::stacks,
            ToolBeltComponent::new
    );

    public static final ToolBeltComponent EMPTY = new ToolBeltComponent(List.of());
}
