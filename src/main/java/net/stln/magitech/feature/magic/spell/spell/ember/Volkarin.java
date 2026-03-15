package net.stln.magitech.feature.magic.spell.spell.ember;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.magicentity.volkarin.VolkarinEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Volkarin extends ShotSpell {

    public Volkarin() {
        super(new SpellConfig.Builder(Element.EMBER, SpellShape.BOMB, 240, 75)
                        .charge(10)
                        .property(SpellPropertyInit.DAMAGE, 12.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.5F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 4.0F)
                        .endSound(SoundInit.VOLKARIN_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                VolkarinEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.setRemainingFireTicks(Math.min(livingEntity.getRemainingFireTicks() + 80, 200));
        }
    }
}
