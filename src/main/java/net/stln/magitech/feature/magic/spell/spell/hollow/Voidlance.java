package net.stln.magitech.feature.magic.spell.spell.hollow;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Voidlance extends BeamSpell {

    public Voidlance() {
        super(new SpellConfig.Builder(Element.HOLLOW, SpellShape.BEAM, 120, 40)
                .charge(4)
                .property(SpellPropertyInit.DAMAGE, 7.0F)
                .property(SpellPropertyInit.MAX_RANGE, 64F)
                .property(SpellPropertyInit.BEAM_RADIUS, 0.1F)
                .endSound(SoundInit.VOIDLANCE)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void additionalBeamProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable Entity target, Vec3 end) {
        final List<Vec3>[] vec3s = new List[]{new ArrayList<>()};
        TickScheduler.schedule(2, () -> vec3s[0] = fractalBeam(level, caster, wand, end, 1), level.isClientSide);
        TickScheduler.schedule(4, () -> {
            for (Vec3 hit : vec3s[0]) {
                fractalBeam(level, caster, wand, hit, 2);
            }
        }, level.isClientSide);
    }

    public List<Vec3> fractalBeam(Level level, LivingEntity caster, ItemStack wand, Vec3 end, int order) {
        List<Vec3> vec3s = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) != 1) {
                        continue;
                    }
                    Vec3 dir = new Vec3(i, j, k).normalize();
                    Vec3 start = end.add(dir.scale(0.1));
                    float beamradius = MagicPerformanceHelper.getEffectiveBeamRadius(caster, wand, this);
                    Vec3 hit = CombatHelper.raycastBeam(caster, (double) 24 / Math.pow(order, 2), start, dir, beamradius);
                    Entity target = CombatHelper.raycastBeamEntity(caster, (double) 24 / Math.pow(order, 2), start, dir, beamradius);

                    hitTarget(level, caster, wand, target);
                    if (!level.isClientSide) {
                        SoundHelper.broadcastSound(level, caster, hit, getConfig().endSound());
                    } else {
                        addBeamVFX(level, caster, end, hit);
                    }
                    vec3s.add(hit);
                }
            }
        }
        return vec3s;
    }

    @Override
    protected void applyEffectToTarget(Level level, LivingEntity caster, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(new MobEffectInstance(MobEffectInit.VOIDROT, 80, 0), livingTarget);
        }
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 5, 0.1F, 0.2F);
        LineVFX.destinationLined(level, start, end, element, ElementParticles::riftParticle, new Section(0F, 1F), 5, 0.0F, 0.1F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burst(level, end, element, SquareParticles::squareParticle, 20, 0.3F);
        PointVFX.burst(level, end, element, ElementParticles::riftParticle, 10, 0.1F);
    }
}
