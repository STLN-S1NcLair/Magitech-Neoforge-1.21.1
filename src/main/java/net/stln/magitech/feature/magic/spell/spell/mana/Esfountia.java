package net.stln.magitech.feature.magic.spell.spell.mana;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellHelper;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.SpraySpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.VectorHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Esfountia extends SpraySpell {

    public Esfountia() {
        super(new SpellConfig.Builder(Element.MANA, SpellShape.SPRAY, 20, 5)
                .continuous(0.5F)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 1.0F)
                .tickSound(SoundInit.ESFOUNTIA, 10)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        if (!charging) {
            Element element = this.getConfig().element();
            Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
            Vec3 bodyPos = CombatHelper.getBodyPos(caster);
            Vec3 offset = bodyPos.add(forward.scale(1));
            PointVFX.spray(level, offset, element,
                    (lvl, pos, elm) -> PresetHelper.bigger(SquareParticles.squareParticle(lvl, pos, elm)),
                    forward, 10, 0.5F, 0.4F);
            if (level.getRandom().nextBoolean()) {
                Vec3 center = caster.getEyePosition().add(forward);
                Vec3 center2 = center.add(forward.scale(2));
                Vec3 random = center2.add(VectorHelper.randScaledRandom(level.random).scale(2.0F));
                TrailVFX.directionalZapTrail(level, offset, random, 0.25F, 2.0F, 0.25F, 20, element);
            }
        }
    }
}
