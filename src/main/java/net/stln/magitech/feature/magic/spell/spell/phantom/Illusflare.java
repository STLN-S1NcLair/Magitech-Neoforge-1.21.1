package net.stln.magitech.feature.magic.spell.spell.phantom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.magicentity.illusflare.IllusflareEntity;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Illusflare extends ShotSpell {

    public Illusflare() {
        super(new SpellConfig.Builder(Element.PHANTOM, SpellShape.BOMB, 200, 70)
                        .charge(20)
                        .property(SpellPropertyInit.DAMAGE, 4.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.2F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 3.5F)
                        .endSound(SoundInit.ILLUSFLARE_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                IllusflareEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffectInit.SEIZE, 50, 0));
        }
    }
}
