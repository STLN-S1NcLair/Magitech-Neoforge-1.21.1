package net.stln.magitech.feature.magic.spell.spell.mana;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.magicentity.aetherix.AetherixEntity;
import net.stln.magitech.content.entity.magicentity.mirazien.MirazienEntity;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Aetherix extends ShotSpell {

    public Aetherix() {
        super(new SpellConfig.Builder(Element.MANA, SpellShape.SHOT, 15, 25)
                        .property(SpellPropertyInit.DAMAGE, 3.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.5F)
                        .endSound(SoundInit.AETHERIX)
                        .endAnim("swing_wand"),
                AetherixEntity::new);
    }
}
