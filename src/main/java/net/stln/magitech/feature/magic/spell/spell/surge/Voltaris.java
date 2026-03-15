package net.stln.magitech.feature.magic.spell.spell.surge;

import net.stln.magitech.content.entity.magicentity.voltaris.VoltarisEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

public class Voltaris extends ShotSpell {

    public Voltaris() {
        super(new SpellConfig.Builder(Element.SURGE, SpellShape.SHOT, 30, 50)
                        .property(SpellPropertyInit.DAMAGE, 6.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.0F)
                        .endSound(SoundInit.SPARK)
                        .endAnim("swing_wand"),
                VoltarisEntity::new);
    }
}
