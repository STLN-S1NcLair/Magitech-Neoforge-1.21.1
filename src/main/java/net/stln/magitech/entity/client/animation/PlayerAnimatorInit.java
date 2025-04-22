package net.stln.magitech.entity.client.animation;


import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.EventBusSubscriber;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.mana.UsedHandData;

import java.util.Optional;

/**
 * This is an example implementation of PlayerAnimator resourceLoading and playerMapping
 */
@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PlayerAnimatorInit {


    @net.neoforged.bus.api.SubscribeEvent
    public static void onClientSetup(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event)
    {
        //Set the player construct callback. It can be a lambda function.
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"),
                42,
                (abstractClientPlayer -> {
                        boolean mirror = UsedHandData.getUsedHand(abstractClientPlayer) == InteractionHand.OFF_HAND;
                        //animationStack.addAnimLayer(42, testAnimation); //Add and save the animation container for later use.
                        ModifierLayer<IAnimation> testAnimation =  new ModifierLayer<>();

                        testAnimation.addModifierBefore(new AbstractFadeModifier(1) {
                            @Override
                            protected float getAlpha(String s, TransformType transformType, float v) {
                                return 0;
                            }
                        });
                        testAnimation.addModifierBefore(new MirrorModifier() {
                            @Override
                            public boolean isEnabled() {
                                return mirror;
                            }
                        });
                        testAnimation.addModifierBefore(new AdjustmentModifier((part) -> {
                            if (part.equals("head")) {
                                return Optional.of(new AdjustmentModifier.PartModifier(
                                        new Vec3f(0, (float) Math.toRadians(abstractClientPlayer.yBodyRot - abstractClientPlayer.yHeadRot) * (mirror ? -1 : 1), 0),
                                        new Vec3f(0, 0, 0)));
                            }
                            if (part.equals("body")) {
                                return Optional.of(new AdjustmentModifier.PartModifier(
                                        new Vec3f(0, (float) Math.toRadians(abstractClientPlayer.yBodyRot - abstractClientPlayer.yHeadRot) * (mirror ? -1 : 1), 0),
                                        new Vec3f(0, 0, 0)));
                            }
                            return Optional.empty();
                        }));
                        return testAnimation;
                }));
    }

    //This method will set your mods animation into the library.
    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        //This will be invoked for every new player
        return new ModifierLayer<>();
    }

}