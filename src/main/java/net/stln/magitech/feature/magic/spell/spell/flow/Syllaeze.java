package net.stln.magitech.feature.magic.spell.spell.flow;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.function.Function;

public class Syllaeze extends Spell {

    public Syllaeze() {
        super(new SpellConfig.Builder(Element.FLOW, SpellShape.SPRAY, 80, 5)
                .continuous(2)
                .property(SpellPropertyInit.EFFECT_STRENGTH, 1.0F)
                .tickSound(SoundInit.SYLLAEZE, 5)
                .castAnim("wand_flight")
        );
    }

    @Override
    public void tickSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, int ticks, boolean charging) {
        if (!level.isClientSide) {
            caster.fallDistance = 0;
            caster.addDeltaMovement(Vec3.directionFromRotation(caster.getRotationVector()).scale(MagicPerformanceHelper.getEffectiveEffectStrength(caster, wand, this) / 5));
            caster.hurtMarked = true;
        }
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        Element element = getConfig().element();
        Vec3 old = CombatHelper.getOldBodyPos(caster);
        Vec3 current = CombatHelper.getBodyPos(caster);

        PointVFX.burst(level, current, element, ((lev, pos, elm) -> SquareParticles.squareGravityParticle(level, pos, elm, 0.1F)), 20, 0.3F);
        PointVFX.burst(level, current, element, ElementParticles::leafParticle, 20, 0.4F);

        TrailPointBuilder trail = TrailPointBuilder.create(5);
        TrailPointBuilder longTrail = TrailPointBuilder.create(20);
        trail.addTrailPoint(current);
        longTrail.addTrailPoint(current);
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailData trailData = new TrailData(level, builderFunc, trail, element.getPrimary(), element.getSecondary(), 2.0F, 0.9F);
        TrailData longTrailData = new TrailData(level, builderFunc, longTrail, element.getSecondary(), element.getDark(), 1.0F, 0.5F);

        TrailRenderer.updateTrail(caster, trailData, new TrailPoint(old), TrailRenderer.TRAIL);
        TrailRenderer.updateTrail(caster, longTrailData, new TrailPoint(old), TrailRenderer.LONG_TRAIL);
    }
}
