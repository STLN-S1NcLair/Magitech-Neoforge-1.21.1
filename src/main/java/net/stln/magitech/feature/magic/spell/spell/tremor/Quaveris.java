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
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.WaveParticleEffect;
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
        Vec3 back = Vec3.directionFromRotation(caster.getRotationVector()).scale(-1);
        Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(back.scale(1));

        EffectHelper.lineEffect(level, new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 10), 0.9F), start, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.0F, 1.0F, 1.0F), new Vector3f(0.0F, 1.0F, 1.0F), end.toVector3f(), 0.7F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 10), 0.9F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
        for (int j = 0; j < 10; j++) {
            level.addParticle(new WaveParticleEffect(new Vector3f(1), new Vector3f(1),
                            5F, 1, 0.3F, level.random.nextInt(5, 10), 0.9F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    back.x * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.y * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.z * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2);
        }
    }
}
