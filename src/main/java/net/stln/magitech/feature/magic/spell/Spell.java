package net.stln.magitech.feature.magic.spell;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.network.SpellCastPayload;
import net.stln.magitech.content.network.SpellEndPayload;
import net.stln.magitech.core.api.mana.handler.EntityManaHelper;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.effect.animation.AnimationHelper;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.charge.ChargeData;
import net.stln.magitech.feature.magic.cooldown.CooldownData;
import net.stln.magitech.feature.magic.cooldown.CooldownHelper;
import net.stln.magitech.feature.magic.mana.UsedHandData;
import net.stln.magitech.feature.magic.spell.property.SpellProperties;
import net.stln.magitech.feature.magic.spell.property.SpellProperty;
import net.stln.magitech.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class Spell implements ISpell {

    private final SpellConfig config;

    public Spell(SpellConfig.Builder builder) {
        this.config = builder.build();
    }


    @Override
    public void cast(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, boolean isHost) {
        if (!canCast(level, caster)) {
            return;
        }
        if (level.isClientSide) {
            if (caster instanceof Player player) {
                Optional<ResourceLocation> castAnim = this.getConfig().castAnim();
                castAnim.ifPresent(anim -> AnimationHelper.playAnim(player, anim));
                setUsedHand(caster, hand);
            }
            castVFX(level, caster);
            if (!isLongSpell()) {
                this.end(level, caster, wand, hand, isHost);
            }
        } else {
            if (config.continuous() && !isHost) {
                EntityManaHelper.addMagicMana(caster, -this.config.cost());
            }
            if (isLongSpell()) {
                if (this.getConfig().hasCharge()) {
                    caster.setData(DataAttachmentInit.SPELL_CHARGE, ChargeData.of(this, (int) MagicPerformanceHelper.getEffectiveChargeTime(caster, wand, this)));
                }
                if (hand != null) {
                    caster.startUsingItem(hand);
                }
            } else {
                this.end(level, caster, wand, hand, isHost);
            }
            SoundHelper.broadcastSound(level, caster, config.castSound());
            if (config.hasCharge() && !isHost) {
                SoundHelper.broadcastDelayedSound(level, caster, config.chargeSound(), this.config.chargeTime().get());
            }
        }
        castSpell(level, caster, wand, hand);
        if (isHost) {
            if (level.isClientSide) {
                PacketDistributor.sendToServer(new SpellCastPayload(this, Optional.ofNullable(wand), caster.getId()));
            } else {
                PacketDistributor.sendToAllPlayers(new SpellCastPayload(this, Optional.ofNullable(wand), caster.getId()));
            }
        }
    }

    // ここをそれぞれオーバーライドすること
    public void castSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {

    }

    protected void castVFX(Level level, LivingEntity caster) {

    }

    @Override
    public boolean canCast(Level level, LivingEntity caster) {
        if (CooldownHelper.isCooldown(caster, this)) {
            hintCoolingdown(caster);
            return false;
        }
        if (!hasEnoughMana(caster)) {
            hintNotEnoughMana(caster);
            return false;
        }
        return true;
    }

    @Override
    public void tick(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, int ticks, boolean isHost) {
        boolean charging = this.getConfig().hasCharge() && caster.getData(DataAttachmentInit.SPELL_CHARGE).charge().remaining() > 0;
        if (level.isClientSide) {
            setUsedHand(caster, hand);
            if (isLongSpell()) {
                if (caster instanceof Player player) {
                    Optional<ResourceLocation> tickAnim = this.getConfig().tickAnim();
                    tickAnim.ifPresent(anim -> AnimationHelper.playAnim(player, anim));
                }
            }
            tickVFX(level, caster, ticks, charging);
        } else {
            if (isLongSpell()) {
                ChargeData data = caster.getData(DataAttachmentInit.SPELL_CHARGE);
                if (data.charge().remaining() <= 0) {
                    if (this.getConfig().continuous() && canContinuousCast(level, caster)) {
                        EntityManaHelper.addMagicMana(caster, -this.config.costPerTick().get());
                    } else {
                        end(level, caster, wand, hand, isHost);
                    }
                }
            }
            int tickSoundDelay = config.tickSoundDelay();
            if (!charging && (tickSoundDelay == 0 || ticks % tickSoundDelay == 0)) {
                SoundHelper.broadcastSound(level, caster, config.tickSound());
            }
        }
        this.tickSpell(level, caster, wand, hand, ticks, charging);
    }

    // ここをそれぞれオーバーライドすること
    public void tickSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, int ticks, boolean charging) {

    }

    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {

    }

    @Override
    public boolean canContinuousCast(Level level, LivingEntity caster) {
        if (CooldownHelper.isCooldown(caster, this)) {
            hintCoolingdown(caster);
            return false;
        }
        if (!hasEnoughContinuousMana(caster)) {
            hintNotEnoughMana(caster);
            return false;
        }
        return true;
    }

    @Override
    public void end(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, boolean isHost) {
        if (caster.getData(DataAttachmentInit.SPELL_CHARGE).equals(ChargeData.empty())) {
            if (level.isClientSide) {
                if (caster instanceof Player player) {
                    Optional<ResourceLocation> endAnim = this.getConfig().endAnim();
                    endAnim.ifPresentOrElse(anim -> AnimationHelper.playAnim(player, anim), () -> AnimationHelper.stopAnim(player));
                }
                endVFX(level, caster);
            } else {
                if (!config.continuous()) {
                    EntityManaHelper.addMagicMana(caster, -this.config.cost());
                }
                SoundHelper.broadcastSound(level, caster, config.endSound());
            }
            endSpell(level, caster, wand, hand);
            CooldownHelper.addCooldown(caster, this, wand);
        }
        caster.stopUsingItem();
        if (caster.getData(DataAttachmentInit.SPELL_CHARGE).charge().remaining() > 0 && caster instanceof Player player) {
            if (level.isClientSide) {
                AnimationHelper.stopAnim(player);
            } else if (!isHost) {
                caster.setData(DataAttachmentInit.SPELL_CHARGE, ChargeData.empty());
            }
        }
        if (isHost) {
            if (level.isClientSide) {
                PacketDistributor.sendToServer(new SpellEndPayload(this, Optional.ofNullable(wand), caster.getId()));
            } else {
                PacketDistributor.sendToAllPlayers(new SpellEndPayload(this, Optional.ofNullable(wand), caster.getId()));
            }
        }
    }

    // ここをそれぞれオーバーライドすること
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {

    }

    protected void endVFX(Level level, LivingEntity caster) {

    }

    private boolean isLongSpell() {
        return this.getConfig().continuous() || this.getConfig().hasCharge();
    }

    private static void setUsedHand(LivingEntity caster, @Nullable InteractionHand hand) {
        if (caster instanceof Player user) {
            UsedHandData.setUsedHand(user, (hand == InteractionHand.OFF_HAND) ^ (user.getMainArm() == HumanoidArm.LEFT));
        }
    }

    private boolean hasEnoughMana(LivingEntity caster) {
        return caster instanceof Player player && player.isCreative() || EntityManaHelper.getMagicMana(caster) >= this.config.cost();
    }

    private boolean hasEnoughContinuousMana(LivingEntity caster) {
        return caster instanceof Player player && player.isCreative() || this.config.continuous() && EntityManaHelper.getMagicMana(caster) >= this.config.costPerTick().get();
    }

    private void hintCoolingdown(LivingEntity caster) {
        if (caster instanceof Player player) {
            CooldownData data = player.getData(DataAttachmentInit.SPELL_COOLDOWNS);
            CooldownData.Cooldown cooldown = data.get(this);
            // 少し遅延を入れる
            if (cooldown.progress() > 10 || (float) cooldown.progress() / cooldown.length() > 0.5F) {
                MutableComponent component = Component.translatable("spell.magitech.hint.cooling_down").withColor(0xFF8080);
                player.displayClientMessage(component, true);
            }
        }
    }

    private void hintNotEnoughMana(LivingEntity caster) {
        if (caster instanceof Player player) {
            MutableComponent component = Component.translatable("spell.magitech.hint.not_enough_mana").withColor(0xFF8080);
            player.displayClientMessage(component, true);
        }
    }

    public List<Component> getTooltip(Level level, LivingEntity caster, @Nullable ItemStack wand) {
        List<Component> list = new ArrayList<>();
        SpellConfig config = this.getConfig();
        Element element = config.element();
        SpellShape shape = config.shape();
        float cost = config.cost();
        SpellProperties properties = config.properties();
        list.add(element.getSpellElementName().withColor(element.getTextColor().getRGB()).append(Component.literal(" ").append(Component.translatable("spell_shape.magitech." + shape.get()).withColor(shape.getDark()))));
        for (SpellProperty<?> key : properties.map().keySet()) {
            MutableComponent component = key.getDisplayText(this, caster, wand);
            list.add(component);
        }
        if (config.hasCharge()) {
            list.add(Component.translatable("spell.magitech.charge_time").append(": " + MathHelper.round((double) MagicPerformanceHelper.getEffectiveChargeTime(caster, wand, this) / 20, 2) + "s"));
        }
        list.add(Component.translatable("spell.magitech.cooldown_time").append(": " + MathHelper.round((double) MagicPerformanceHelper.getEffectiveCooldown(caster, wand, this) / 20, 2) + "s"));
        list.add(Component.empty());
        list.add(Component.translatable("spell.magitech.mana_cost").append(": " + MathHelper.round(MagicPerformanceHelper.getEffectiveCost(caster, wand, this), 2) + "kJ").withColor(0x40FFF0));
        if (config.continuous()) {
            list.add(Component.translatable("spell.magitech.continuous_mana_cost").append(": " + MathHelper.round(MagicPerformanceHelper.getEffectiveContinuousCost(caster, wand, this), 2) + "kJ/tick").withColor(0x40FFF0));
        }
        return list;
    }

    @Override
    public SpellConfig getConfig() {
        return config;
    }

    @Override
    public @NotNull Spell asSpell() {
        return this;
    }

    public @NotNull ResourceLocation getId() {
        return Objects.requireNonNull(MagitechRegistries.SPELL.getKey(this));
    }

    public @NotNull String getDescriptionId() {
        return getId().toLanguageKey("spell");
    }

    public @NotNull MutableComponent getDescription() {
        return Component.translatable(getDescriptionId());
    }

    public @NotNull ResourceLocation getIconId() {
        return getId().withPrefix("textures/spell/").withSuffix(".png");
    }
}
