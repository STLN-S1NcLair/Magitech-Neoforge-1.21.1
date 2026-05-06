package net.stln.magitech.content.item.armor;

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
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.TickScheduler;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class AetherLifterItem extends TooltipArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AetherLifterItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    public static void doubleJump(Player player, int jumpCount, ItemStack stack) {
        if (jumpCount == 0) {
            Level level = player.level();
            Vec3 movement = player.getDeltaMovement();
            player.setDeltaMovement(new Vec3(movement.x, 0.8, movement.z));
            player.fallDistance = -5;
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.AETHER_LIFTER_JUMP.get(), SoundSource.PLAYERS, 1, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));

            if (level.isClientSide) {

                var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(Magitech.id("animation"));
                if (playerAnimationData != null) {

                    player.yBodyRot = player.yHeadRot;
                    playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(4, Ease.INOUTSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(Magitech.id("double_jump")))
                            .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(false, false, true, true)));
                }
                Vec3 position = player.position();
                Vec3 surface = CombatHelper.findSurface(level, position);
                for (int i = 0; i < 10; i++) {
                    if (player.getRandom().nextBoolean()) {
                        PointVFX.burst(level, position, Element.HOLLOW, SquareParticles::squareParticle, 1, 0.0F);
                    } else {
                        PointVFX.burst(level, position, Element.PHANTOM, SquareParticles::squareParticle, 1, 0.0F);
                    }
                }
                for (int i = 0; i < position.distanceTo(surface) * 5; i++) {
                    Vec3 lerped = surface.lerp(position, i / position.distanceTo(surface) / 5);
                    if (player.getRandom().nextBoolean()) {
                        PointVFX.burst(level, lerped, Element.HOLLOW, SquareParticles::squareParticle, 1, 0.0F);
                    } else {
                        PointVFX.burst(level, lerped, Element.PHANTOM, SquareParticles::squareParticle, 1, 0.0F);
                    }
                }
                Vec3 up = new Vec3(0, 1, 0);
                if (player.getRandom().nextBoolean()) {
                    PointVFX.burst(level, position.add(0, 0.1F, 0), Element.HOLLOW, (lvl, p, elm) -> PresetHelper.bigger(PresetHelper.longer(RingParticles.ringReversedParticle(lvl, p, up, elm))), 1, 0.0F);
                } else {
                    PointVFX.burst(level, position.add(0, 0.1F, 0), Element.PHANTOM, (lvl, p, elm) -> PresetHelper.bigger(PresetHelper.longer(RingParticles.ringReversedParticle(lvl, p, up, elm))), 1, 0.0F);
                }
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 3; j++) {
                        TickScheduler.schedule(i + 1, () -> {
                            if (player.getRandom().nextBoolean()) {
                                PointVFX.burst(level, position, Element.HOLLOW, SquareParticles::squareParticle, 1, 0.0F);
                            } else {
                                PointVFX.burst(level, position, Element.PHANTOM, SquareParticles::squareParticle, 1, 0.0F);
                            }
                        }, level.isClientSide);
                    }
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
//            state.getController().setAnimation(DefaultAnimations.ITEM_ON_USE);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null) // Important that we do this. If we just instantiate  it directly in the field it can cause incompatibilities with some mods.
                    this.renderer = new AetherLifterRenderer();

                return this.renderer;
            }
        });
    }
}
