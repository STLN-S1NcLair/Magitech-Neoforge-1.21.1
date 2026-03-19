package net.stln.magitech.feature.magic.spell.spell.phantom;

import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.BeamParticles;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import org.jetbrains.annotations.Nullable;

public class Phantastra extends BeamSpell {

    public Phantastra() {
        super(new SpellConfig.Builder(Element.PHANTOM, SpellShape.BEAM, 70, 40)
                .charge(6)
                .property(SpellPropertyInit.DAMAGE, 5.0F)
                .property(SpellPropertyInit.MAX_RANGE, 128F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.3F)
                .endSound(SoundInit.PHANTASTRA)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void additionalBeamProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable Entity target, Vec3 hitPos) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        float radius = MagicPerformanceHelper.getEffectiveBeamRadius(caster, wand, this);
        float maxRange = MagicPerformanceHelper.getEffectiveMaxRange(caster, wand, this);
        BlockHitResult beamBlockHit = CombatHelper.getBeamBlockHit(caster, maxRange, radius, Vec3.directionFromRotation(caster.getRotationVector()));
        if (beamBlockHit != null && beamBlockHit.getType() != HitResult.Type.MISS) {
            Direction hitDirection = beamBlockHit.getDirection();
            Vec3 reflected = switch (hitDirection.getAxis()) {
                case X -> new Vec3(-forward.x, forward.y, forward.z);
                case Y -> new Vec3(forward.x, -forward.y, forward.z);
                case Z -> new Vec3(forward.x, forward.y, -forward.z);
            };
            reflect(level, caster, wand, hitPos, reflected, maxRange);
        }
    }

    public void reflect(Level level, LivingEntity caster, ItemStack wand, Vec3 hitPos, Vec3 direction, float maxRange) {

        float beamradius = MagicPerformanceHelper.getEffectiveBeamRadius(caster, wand, this);
        Vec3 start = hitPos.add(direction.scale(0.1F));
        Vec3 hit = CombatHelper.raycastBeam(caster, maxRange, start, direction, beamradius);
        Entity target = CombatHelper.raycastBeamEntity(caster, maxRange, start, direction, beamradius);

        if (!level.isClientSide) {
            if (target != null) {
                hitTarget(level, caster, wand, target);
            }
            SoundHelper.broadcastSound(level, caster, hit, getConfig().endSound());
        } else {
            addBeamVFX(level, caster, hitPos, hit);
        }
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffectInit.SEIZE, 50, 0));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
        }
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.2F, 0.2F);
        LineVFX.destinationLined(level, start, end, element, ElementParticles::glintParticle, new Section(0F, 1F), 5, 0.1F, 0.05F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burst(level, end, element, SquareParticles::squareParticle, 10, 0.1F);
        PointVFX.burst(level, end, element, ElementParticles::glintParticle, 10, 0.05F);
    }
}
