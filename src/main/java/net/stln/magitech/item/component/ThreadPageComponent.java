package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;

public record ThreadPageComponent(Spell spell) {

    public static final Codec<ThreadPageComponent> CODEC = RecordCodecBuilder.create(spellComponentInstance ->
            spellComponentInstance.group(
                    Codec.STRING.fieldOf("spell").forGetter(ThreadPageComponent::getStringSpellId)
            ).apply(spellComponentInstance, ThreadPageComponentUtil::generateFromId)
    );
    public static final StreamCodec<ByteBuf, ThreadPageComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(SpellRegister::getSpellFromString, SpellRegister::getStringId),
            ThreadPageComponent::spell,
            ThreadPageComponent::new
    );


    public ResourceLocation getSpellId() {
        if (spell != null) {
            return SpellRegister.getId(spell);
        }
        return null;
    }

    public String getStringSpellId() {
        if (spell != null) {
            return SpellRegister.getId(spell).toString();
        }
        return null;
    }

    public Spell getSpell() {
        return spell;
    }
}

