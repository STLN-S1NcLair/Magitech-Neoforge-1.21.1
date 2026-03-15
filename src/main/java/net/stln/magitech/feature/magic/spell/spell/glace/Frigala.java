package net.stln.magitech.feature.magic.spell.spell.glace;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.magicentity.frigala.FrigalaEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Frigala extends ShotSpell {

    public Frigala() {
        super(new SpellConfig.Builder(Element.GLACE, SpellShape.SHOT, 60, 55)
                        .charge(25)
                        .property(SpellPropertyInit.DAMAGE, 8.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.5F)
                        .endSound(SoundInit.GLACE_LAUNCH)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                FrigalaEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.setTicksFrozen(Math.min(livingEntity.getTicksFrozen() + 200, 300));
        }
    }
}
