package net.stln.magitech.feature.magic.spell.spell.magic;

import net.stln.magitech.content.entity.magicentity.hexflare.HexflareEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

public class Hexflare extends ShotSpell {

    public Hexflare() {
        super(new SpellConfig.Builder(Element.MAGIC, SpellShape.BOMB, 240, 80)
                        .charge(5)
                        .property(SpellPropertyInit.DAMAGE, 15.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.5F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 4.0F)
                        .endSound(SoundInit.HEXFLARE_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                HexflareEntity::new);
    }
}
