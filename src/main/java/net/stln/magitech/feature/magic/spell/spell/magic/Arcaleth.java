package net.stln.magitech.feature.magic.spell.spell.magic;
import net.stln.magitech.content.entity.magicentity.arcaleth.ArcalethEntity;
import net.stln.magitech.content.entity.magicentity.ignisca.IgniscaEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.Map;

public class Arcaleth extends ShotSpell {

    public Arcaleth() {
        super(new SpellConfig.Builder(Element.MAGIC, SpellShape.SHOT, 40, 45)
                        .property(SpellPropertyInit.DAMAGE, 6.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 2.0F)
                        .endSound(SoundInit.ARCALETH)
                        .endAnim("swing_wand"),
                ArcalethEntity::new);
    }
}
