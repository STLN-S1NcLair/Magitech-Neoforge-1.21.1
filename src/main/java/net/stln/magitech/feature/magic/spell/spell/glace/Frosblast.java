package net.stln.magitech.feature.magic.spell.spell.glace;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.magicentity.frosblast.FrosblastEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Frosblast extends ShotSpell {

    public Frosblast() {
        super(new SpellConfig.Builder(Element.GLACE, SpellShape.BOMB, 220, 70)
                        .charge(10)
                        .property(SpellPropertyInit.DAMAGE, 9.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.0F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 3.0F)
                        .endSound(SoundInit.FROSBLAST_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                FrosblastEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.setTicksFrozen(Math.min(livingEntity.getTicksFrozen() + 200, 300));
        }
    }
}
