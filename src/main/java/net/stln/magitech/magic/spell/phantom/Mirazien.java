package net.stln.magitech.magic.spell.phantom;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethEntity;
import net.stln.magitech.entity.magicentity.mirazien.MirazienEntity;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.particle.particle_option.FrostParticleEffect;
import net.stln.magitech.particle.particle_option.MembraneParticleEffect;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Mirazien extends Spell {
    @Override
    public Map<ManaUtil.ManaType, Double> getCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 35.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 3.0);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getTickCost() {
        return new HashMap<>();
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        super.use(level, user, hand, isHost);
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS);

        if (!level.isClientSide && !isHost) {
            MirazienEntity bullet = new MirazienEntity(level, user, user.getItemInHand(hand), 4);
            Vec3 velocity = Vec3.directionFromRotation(user.getRotationVector());
            velocity = velocity.normalize().scale(3);
            bullet.setDeltaMovement(velocity);
            bullet.setPos(user.getX(), user.getEyeY() - 0.3, user.getZ());
            level.addFreshEntity(bullet);
        }
    }

    @Override
    protected void playAnimation(Player user) {
        var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) user).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
        if (playerAnimationData != null) {

            user.yBodyRot = user.yHeadRot;
            playerAnimationData.setAnimation(new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "swing_wand")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true)));
        }
    }
}
