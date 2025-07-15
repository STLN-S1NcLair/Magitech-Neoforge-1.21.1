package net.stln.magitech.item.armor;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.particle.particle_option.SquareFieldParticleEffect;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.BlockUtil;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import net.stln.magitech.util.TickScheduler;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class AetherLifterItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AetherLifterItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
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
                if(this.renderer == null) // Important that we do this. If we just instantiate  it directly in the field it can cause incompatibilities with some mods.
                    this.renderer = new AetherLifterRenderer();

                return this.renderer;
            }
        });
    }

    public static void doubleJump(Player player, int jumpCount, ItemStack stack) {
        if (jumpCount == 0) {
            Level level = player.level();
            Vec3 movement = player.getDeltaMovement();
            player.setDeltaMovement(new Vec3(movement.x, 0.8, movement.z));
            player.fallDistance = -5;
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.AETHER_LIFTER_JUMP.get(), SoundSource.PLAYERS, 1, 1);

            if (level.isClientSide) {

                var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animation"));
                if (playerAnimationData != null) {

                    player.yBodyRot = player.yHeadRot;
                    playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(4, Ease.INOUTSINE), new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "double_jump")))
                            .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration(false, false, true, true)));
                }

                Vector3f phantomCol = new Vector3f(1.0F, 1.0F, 0.5F);
                Vector3f hollowCol = new Vector3f(0.3F, 0.0F, 1.0F);
                Vec3 position = player.position();
                Vec3 surface = EntityUtil.findSurface(level, position);
                for (int i = 0; i < 10; i++) {
                    double x = player.getX() + Mth.nextDouble(player.getRandom(), -player.getBbWidth(), player.getBbWidth());
                    double y = player.getY() + Mth.nextDouble(player.getRandom(), -0.25, 0.25);
                    double z = player.getZ() + Mth.nextDouble(player.getRandom(), -player.getBbWidth(), player.getBbWidth());
                    if (player.getRandom().nextBoolean()) {
                        level.addParticle(new SquareParticleEffect(hollowCol, phantomCol, 1.0F, player.getRandom().nextInt(3, 6), 0), x, y, z, 0, 0.05, 0);
                    } else {
                        level.addParticle(new SquareParticleEffect(phantomCol, hollowCol, 1.0F, player.getRandom().nextInt(3, 6), 0), x, y, z, 0, 0.05, 0);
                    }
                }
                for (int i = 0; i < position.distanceTo(surface) * 5; i++) {
                    Vec3 lerped = surface.lerp(position, i / position.distanceTo(surface) / 5);
                    double x = lerped.x + Mth.nextDouble(player.getRandom(), -player.getBbWidth(), player.getBbWidth()) / 2;
                    double y = lerped.y + Mth.nextDouble(player.getRandom(), -0.25, 0.25);
                    double z = lerped.z + Mth.nextDouble(player.getRandom(), -player.getBbWidth(), player.getBbWidth()) / 2;
                    if (player.getRandom().nextBoolean()) {
                        level.addParticle(new SquareParticleEffect(hollowCol, phantomCol, 1.0F, player.getRandom().nextInt(3, 6), 0), x, y, z, 0, 0.05, 0);
                    } else {
                        level.addParticle(new SquareParticleEffect(phantomCol, hollowCol, 1.0F, player.getRandom().nextInt(3, 6), 0), x, y, z, 0, 0.05, 0);
                    }
                }
                if (player.getRandom().nextBoolean()) {
                    level.addParticle(new SquareFieldParticleEffect(hollowCol, phantomCol, 1.0F, 1, 0),
                            player.getX() + Mth.nextDouble(player.getRandom(), -0.1, 0.1), player.getY() + Mth.nextDouble(player.getRandom(), -0.1, 0.1), player.getZ() + Mth.nextDouble(player.getRandom(), -0.1, 0.1), 0, 0, 0);
                } else {
                    level.addParticle(new SquareFieldParticleEffect(phantomCol, hollowCol, 1.0F, 1, 0),
                            player.getX() + Mth.nextDouble(player.getRandom(), -0.1, 0.1), player.getY() + Mth.nextDouble(player.getRandom(), -0.1, 0.1), player.getZ() + Mth.nextDouble(player.getRandom(), -0.1, 0.1), 0, 0, 0);
                }
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 3; j++) {
                        TickScheduler.schedule(i + 1, () -> {
                            double x = player.getX() + Mth.nextDouble(player.getRandom(), -0.1, 0.1);
                            double y = player.getY() + Mth.nextDouble(player.getRandom(), -0.1, 0.1);
                            double z = player.getZ() + Mth.nextDouble(player.getRandom(), -0.1, 0.1);
                            if (player.getRandom().nextBoolean()) {
                                level.addParticle(new SquareParticleEffect(hollowCol, phantomCol, 1.0F, player.getRandom().nextInt(3, 6), 0), x, y, z, 0, 0.05, 0);
                            } else {
                                level.addParticle(new SquareParticleEffect(phantomCol, hollowCol, 1.0F, player.getRandom().nextInt(3, 6), 0), x, y, z, 0, 0.05, 0);
                            }
                        }, level.isClientSide);
                    }
                }
            }
        }
    }
}
