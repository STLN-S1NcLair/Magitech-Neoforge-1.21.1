package net.stln.magitech.data;

import com.mojang.serialization.Codec;
import net.stln.magitech.element.Element;

public record EntityElementData(Element element) {
    public static final Codec<EntityElementData> CODEC = Element.CODEC.xmap(EntityElementData::new, EntityElementData::element);
}
