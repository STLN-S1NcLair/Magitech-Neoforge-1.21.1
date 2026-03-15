package net.stln.magitech.feature.magic.spell.spell.tremor;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.WaveParticleEffect;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public class Quaveris extends BlinkSpell {

    public Quaveris() {
        super(new SpellConfig.Builder(Element.TREMOR, SpellShape.DASH, 100, 65)
                .charge(30)
                .property(SpellPropertyInit.DAMAGE, 10.0F)
                .property(SpellPropertyInit.MAX_RANGE, 20F)
                .endSound(SoundInit.QUAVERIS)
                .castAnim("charge_wand")
                .endAnim("wand_blink")
        );
    }

    @Override
    protected void additionalBlinkProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, List<Entity> targets, Vec3 start, Vec3 end) {
        for (int i = -2; i < 2; i++) {
            for (int j = -2; j < 2; j++) {
                for (int k = -2; k < 2; k++) {
                    breakBlockWithoutTool(level, BlockPos.containing(end).offset(i, j, k), caster);
                }
            }
        }
    }

    private static void breakBlockWithoutTool(Level level, BlockPos pos, LivingEntity caster) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;

        FluidState fluid = state.getFluidState();

        // 石ツール以上が必要なら破壊しない
        if (state.getTags().anyMatch(Predicate.isEqual(BlockTags.INCORRECT_FOR_WOODEN_TOOL)) || state.getBlock().defaultDestroyTime() < 0) {
            return;
        }

        ItemStack fakeWoodTool = new ItemStack(Items.WOODEN_PICKAXE);
        Player player = caster instanceof Player p ? p : null;
        // playerDestroy を呼んで副作用処理
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (player != null) {
            state.getBlock().playerDestroy(level, player, pos, state, blockEntity, fakeWoodTool);
        } else {
            Block.dropResources(state, level, pos, blockEntity, caster, fakeWoodTool);
        }
        // ブロックの破壊音を再生
        level.levelEvent(player, 2001, pos, Block.getId(state));

        // ブロック破壊
        state.getBlock().onDestroyedByPlayer(state, level, pos, player, true, fluid);
    }

    @Override
    protected void addBlinkVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Element element = getConfig().element();
        Vec3 bodyAdjustment = new Vec3(0, caster.getBbHeight() * 0.7F, 0);
        Vec3 bodyStart = start.add(bodyAdjustment);
        Vec3 bodyEnd = end.add(bodyAdjustment);
        TrailVFX.directionalTrail(level, bodyStart, bodyEnd, 2.0F, 20, element);
        LineVFX.destinationLinedSquare(level, bodyStart, bodyEnd, element, new Section(0F, 1F), 5, 0.7F, 0.2F);
        LineVFX.destinationLined(level, bodyStart, bodyEnd, element, (lvl, pos, elm) -> RingParticles.ringParticle(lvl, pos, end.subtract(start).normalize(), elm), new Section(0F, 1F), 0.25F, 0.0F, 0.0F);
        PointVFX.burst(level, bodyEnd, element, SquareParticles::squareGravityParticle, 100, 0.7F);
        PointVFX.burst(level, bodyEnd, element, (lvl, position, elm) -> PresetHelper.bigger(RingParticles.ringReversedParticle(lvl, position, elm), 9.0F), 1, 0.0F);
    }
}
