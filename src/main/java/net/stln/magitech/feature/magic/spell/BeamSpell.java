package net.stln.magitech.feature.magic.spell;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.charge.ChargeData;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EntityHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class BeamSpell extends DamageSpell {

    public BeamSpell(SpellConfig.Builder builder) {
        super(builder);
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
        float radius = MagicPerformanceHelper.getEffectiveBeamRadius(caster, wand, this);
        float maxRange = MagicPerformanceHelper.getEffectiveMaxRange(caster, wand, this);
        Vec3 hitPos = EntityHelper.raycastBeam(caster, maxRange, radius);
        Entity target = EntityHelper.raycastBeamEntity(caster, maxRange, radius);
        Vec3 start = EntityHelper.getBodyPos(caster).add(forward.scale(0.5));

        if (!level.isClientSide) {
            if (target != null) {
                hitTarget(level, caster, wand, target);
            }
            List<Entity> entities = EntityHelper.getEntitiesInBox(level, caster, hitPos, new Vec3(1, 1, 1));
            for (Entity entity : entities) {
                if (entity instanceof ItemEntity item) {
                    SpellHelper.applyEffectToItem(level, this, caster, item);
                }
            }
            SoundHelper.broadcastSound(level, caster, hitPos, this.getConfig().endSound());
        } else {
            addBeamVFX(level, caster, start, hitPos);
        }
        additionalBeamProcess(level, caster, wand, target, hitPos);
    }

    // 追加の処理
    protected void additionalBeamProcess(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable Entity target, Vec3 hitPos) {

    }

    // ビームのエフェクト
    protected void addBeamVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {

    }
}
