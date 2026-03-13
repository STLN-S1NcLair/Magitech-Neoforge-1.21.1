package net.stln.magitech.feature.magic.spell.spell.surge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.BeamParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BeamSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellHelper;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Fulgenza extends BeamSpell {

    public Fulgenza() {
        super(new SpellConfig.Builder(Element.SURGE, SpellShape.BEAM, 200, 85)
                .charge(80)
                .property(SpellPropertyInit.DAMAGE, 18.0F)
                .property(SpellPropertyInit.MAX_RANGE, 64F)
                .property(SpellPropertyInit.BEAM_RADIUS, 1.0F)
                .chargeSound(SoundInit.FULGENZA_CHARGE)
                .endSound(SoundInit.FULGENZA)
                .castAnim("wand_charge_beam")
                .endAnim("wand_beam")
        );
    }

    @Override
    protected void additionalBeamProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable Entity target, Vec3 hitPos) {
        if (target != null) {
            Set<Entity> attackList = new HashSet<>();
            Vec3 targetBody = CombatHelper.getBodyPos(target);
            attackList.addAll(CombatHelper.getEntitiesInBox(level, caster, hitPos, new Vec3(12.0, 12.0, 12.0)));
            attackList.remove(target);
            for (Entity chainTarget : attackList) {
                Vec3 chainBody = CombatHelper.getBodyPos(chainTarget);
                if (!SpellHelper.canSee(level, caster, targetBody, chainBody)) {
                    continue;
                }

                if (!level.isClientSide) {
                    hitTarget(level, caster, wand, chainTarget, this.getConfig().properties().get(SpellPropertyInit.DAMAGE), 0.5F);
                } else {
                    addChainVFX(level, caster, hitPos, chainBody);
                }
            }
        }
    }

    protected void addChainVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        TrailVFX.zapTrail(level, start, end, 0.5F, 1.0F, 0.5F, 10, element);
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 3, 0.0F, 0.1F);
    }

    @Override
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = this.getConfig().element();
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 10, 0.2F, 0.3F);
        BeamParticles.beamParticle(level, start, end, element, this.getConfig().properties().get(SpellPropertyInit.BEAM_RADIUS));
        PointVFX.burst(level, end, element, ((lev, pos, elm) -> SquareParticles.squareGravityParticle(level, pos, elm, 0.2F)), 50, 0.5F);
        PointVFX.zap(level, end, element, 5, 0.25F, 4.0F, 1.0F, 1.0F, 15);

        TrailVFX.zapTrail(level, start, end, 1.0F, 0.25F, 1.0F, 5, element);
        TrailVFX.zapTrail(level, start, end, 1.0F, 0.25F, 1.5F, 20, element);
    }
}
