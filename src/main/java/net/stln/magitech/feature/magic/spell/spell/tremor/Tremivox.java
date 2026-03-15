package net.stln.magitech.feature.magic.spell.spell.tremor;

import net.stln.magitech.content.entity.magicentity.tremivox.TremivoxEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

public class Tremivox extends ShotSpell {

    public Tremivox() {
        super(new SpellConfig.Builder(Element.TREMOR, SpellShape.SHOT, 60, 30)
                        .charge(10)
                        .property(SpellPropertyInit.DAMAGE, 5.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 0.75F)
                        .endSound(SoundInit.TREMIVOX)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                TremivoxEntity::new);
    }
}
