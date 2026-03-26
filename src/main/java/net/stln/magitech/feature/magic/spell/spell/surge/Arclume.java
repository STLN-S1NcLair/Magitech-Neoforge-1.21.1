package net.stln.magitech.feature.magic.spell.spell.surge;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.helper.VectorHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Arclume extends BlinkSpell {

    public Arclume() {
        super(new SpellConfig.Builder(Element.SURGE, SpellShape.DASH, 60, 55)
                .property(SpellPropertyInit.DAMAGE, 7.0F)
                .property(SpellPropertyInit.MAX_RANGE, 10F)
                .endSound(SoundInit.ARCLUME)
                .endAnim("wand_blink")
        );
    }

    @Override
    protected void additionalBlinkProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, List<Entity> targets, Vec3 start, Vec3 hitPos) {
        for (int i = 0; i < hitPos.subtract(start).length() / 2; i++) {
            int finalI = i;
            TickScheduler.schedule(i * 2, () -> {
                addLightning(level, caster, wand, start.lerp(hitPos, finalI / hitPos.subtract(start).length() * 2).add(VectorHelper.randScaledRandom(level.random)));
            }, level.isClientSide);
        }
    }

    private void addLightning(Level level, LivingEntity caster, ItemStack wand, Vec3 pos) {
        Vec3 surface = CombatHelper.findSurface(level, pos);
        Vec3 lightningTop = surface.add(0, Mth.randomBetween(caster.getRandom(), 5, 20), 0);
        List<Entity> entities = CombatHelper.getEntitiesInBox(level, caster, surface, new Vec3(2, 2, 2));

        SoundHelper.broadcastSound(level, caster, surface, this.getConfig().endSound());

        if (!level.isClientSide) {
            for (Entity target : entities) {
                hitTarget(level, caster, wand, target);
            }
        } else {
            addLightningVFX(level, caster, lightningTop, surface);
        }
    }

    private void addLightningVFX(Level level, LivingEntity caster, Vec3 lightningTop, Vec3 surface) {
        Element element = getConfig().element();
        TrailVFX.zapTrail(level, surface, lightningTop, 0.5F, 1.0F, 0.5F, 20, element);
        PointVFX.burst(level, lightningTop, element, SquareParticles::squareParticle, 10, 0.05F);
        PointVFX.burst(level, surface, element, SquareParticles::squareGravityParticle, 20, 0.5F);
        PointVFX.zap(level, surface, element, 4, 0.25F, 2F, 2F, 0.5F, 10);
    }

    @Override
    protected void addBlinkVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = getConfig().element();
        Vec3 bodyAdjustment = new Vec3(0, caster.getBbHeight() * 0.7F, 0);
        Vec3 bodyStart = start.add(bodyAdjustment);
        Vec3 bodyEnd = end.add(bodyAdjustment);
        TrailVFX.directionalTrail(level, bodyStart, bodyEnd, 2.0F, 20, element);
        TrailVFX.directionalZapTrail(level, bodyEnd, bodyStart, 0.5F, 1.0F, 0.5F, 20, element);
        LineVFX.destinationLinedSquare(level, bodyStart, bodyEnd, element, new Section(0F, 1F), 5, 0.0F, 0.1F);

        PointVFX.zap(level, bodyEnd, element, 4, 0.25F, 2F, 2F, 0.5F, 10);
    }
}
