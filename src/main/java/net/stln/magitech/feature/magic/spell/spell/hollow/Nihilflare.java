package net.stln.magitech.feature.magic.spell.spell.hollow;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.entity.magicentity.nihilflare.NihilflareEntity;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public class Nihilflare extends ShotSpell {

    public Nihilflare() {
        super(new SpellConfig.Builder(Element.HOLLOW, SpellShape.BOMB, 200, 85)
                        .charge(30)
                        .property(SpellPropertyInit.DAMAGE, 13.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 1.5F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 4.0F)
                        .endSound(SoundInit.NIHILFLARE_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                NihilflareEntity::new);
    }

    @Override
    public void applyEffectToTarget(Level level, Entity summoned, @Nullable Entity owner, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            RandomSource random = level.getRandom();
            livingEntity.teleportTo(livingEntity.getX() + Mth.randomBetween(random, -1.0F, 1.0F), livingEntity.getY() + Mth.randomBetween(random, -1.0F, 1.0F), livingEntity.getZ() + Mth.randomBetween(random, -1.0F, 1.0F));
            livingEntity.addEffect(new MobEffectInstance(MobEffectInit.PHASELOCK, 100, 0));
        }
    }
}
