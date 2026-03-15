package net.stln.magitech.feature.magic.spell.spell.ember;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.magicentity.ignisca.IgniscaEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Ignisca extends ShotSpell {

    public Ignisca() {
        super(new SpellConfig.Builder(Element.EMBER, SpellShape.SHOT, 40, 45)
                        .property(SpellPropertyInit.DAMAGE, 5.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.5F)
                        .endSound(SoundInit.FIREBALL)
                        .endAnim("swing_wand"),
                IgniscaEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.setRemainingFireTicks(Math.min(livingEntity.getRemainingFireTicks() + 80, 200));
        }
    }
}
