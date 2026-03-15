package net.stln.magitech.feature.magic.spell.spell.hollow;

import net.stln.magitech.content.entity.magicentity.nullixis.NullixisEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

public class Nullixis extends ShotSpell {

    public Nullixis() {
        super(new SpellConfig.Builder(Element.HOLLOW, SpellShape.SHOT, 40, 55)
                        .property(SpellPropertyInit.DAMAGE, 6.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.3F)
                        .endSound(SoundInit.NULLIXIS)
                        .endAnim("swing_wand"),
                NullixisEntity::new);
    }
}
