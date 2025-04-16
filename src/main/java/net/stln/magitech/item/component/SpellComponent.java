package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;

import java.util.ArrayList;
import java.util.List;

public record SpellComponent(List<Spell> spells, int selected) {

    public static final Codec<SpellComponent> CODEC = RecordCodecBuilder.create(spellComponentInstance ->
            spellComponentInstance.group(
                    Codec.STRING.listOf().fieldOf("spells").forGetter(SpellComponent::getStringSpellIds),
                    Codec.INT.fieldOf("selected").forGetter(SpellComponent::selected)
            ).apply(spellComponentInstance, SpellComponentUtil::generateFromId)
    );
    public static final StreamCodec<ByteBuf, SpellComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(SpellRegister::getSpellFromString, SpellRegister::getStringId).apply(ByteBufCodecs.list()),
            SpellComponent::spells,
            ByteBufCodecs.INT,
            SpellComponent::selected,
            SpellComponent::new
    );


    public List<ResourceLocation> getSpellIds() {
        List<ResourceLocation> ids = new ArrayList<>();
        for (Spell spell : spells) {
            if (spell != null) {
                ids.add(SpellRegister.getId(spell));
            }
        }
        return ids;
    }

    public List<String> getStringSpellIds() {
        List<String> ids = new ArrayList<>();
        for (Spell spell : spells) {
            if (spell != null) {
                ids.add(SpellRegister.getId(spell).toString());
            }
        }
        return ids;
    }

    public List<Spell> getMaterial() {
        return spells;
    }
}

