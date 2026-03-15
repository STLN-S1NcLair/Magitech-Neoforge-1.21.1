package net.stln.magitech.feature.magic.spell.spell.tremor;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.magicentity.shockvane.ShockvaneEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Shockvane extends ShotSpell {

    public Shockvane() {
        super(new SpellConfig.Builder(Element.TREMOR, SpellShape.BOMB, 360, 90)
                        .charge(30)
                        .property(SpellPropertyInit.DAMAGE, 9.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.5F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 5.0F)
                        .endSound(SoundInit.SHOCKVANE_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                ShockvaneEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
        }
    }
}
