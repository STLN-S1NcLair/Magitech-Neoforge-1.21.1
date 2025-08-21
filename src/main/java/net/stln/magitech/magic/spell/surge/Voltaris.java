package net.stln.magitech.magic.spell.surge;

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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisEntity;
import net.stln.magitech.util.Element;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.SpellShape;

import java.util.HashMap;
import java.util.Map;

public class Voltaris extends Spell {

    public Voltaris() {
        baseDamage = 6.0F;
        baseSpeed = 1.0F;
    }

    public Element getElement() {
        return Element.SURGE;
    }

    public SpellShape getSpellShape() {
        return SpellShape.SHOT;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 50.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 4.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 3.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 30;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        super.use(level, user, hand, isHost);
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.SPARK.get(), SoundSource.PLAYERS);
        if (!level.isClientSide && !isHost) {
            VoltarisEntity bullet = new VoltarisEntity(level, user, user.getItemInHand(hand), getDamage(user, this.getCost(level, user, user.getItemInHand(hand)), baseDamage, this.getElement()));
            Vec3 velocity = Vec3.directionFromRotation(user.getRotationVector());
            velocity = velocity.normalize().scale(getProjectileSpeed(user, baseSpeed));
            bullet.setDeltaMovement(velocity);
            bullet.setPos(user.getX(), user.getEyeY() - 0.3, user.getZ());
            level.addFreshEntity(bullet);
        }
        addCooldown(level, user, user.getItemInHand(hand));
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
