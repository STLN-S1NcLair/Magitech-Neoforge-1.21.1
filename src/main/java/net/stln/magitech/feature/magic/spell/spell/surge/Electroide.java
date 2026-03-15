package net.stln.magitech.feature.magic.spell.spell.surge;

import net.stln.magitech.content.entity.magicentity.electroide.ElectroideEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

public class Electroide extends ShotSpell {

    public Electroide() {
        super(new SpellConfig.Builder(Element.SURGE, SpellShape.BOMB, 300, 65)
                        .charge(12)
                        .property(SpellPropertyInit.DAMAGE, 8.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 2.0F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 3.5F)
                        .endSound(SoundInit.ELECTROIDE_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                ElectroideEntity::new);
    }
}
