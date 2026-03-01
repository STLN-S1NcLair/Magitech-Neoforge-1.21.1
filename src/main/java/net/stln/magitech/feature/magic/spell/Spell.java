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
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.network.SpellEndPayload;
import net.stln.magitech.content.network.SpellCastPayload;
import net.stln.magitech.data.DataAttachmentInit;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.mana.UsedHandData;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.charge.ChargeData;
import net.stln.magitech.feature.magic.spell.property.SpellProperties;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyKey;
import net.stln.magitech.helper.MathHelper;
import net.stln.magitech.vfx.animation.AnimationHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class Spell implements ISpell {


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
        } else {
            if (isLongSpell()) {
                caster.startUsingItem(hand);
                if (this.getConfig().hasCharge()) {
                    caster.setData(DataAttachmentInit.SPELL_CHARGE, new ChargeData(Optional.of(this), this.getConfig().chargeTime().get()));
                }
            } else {
                this.end(level, caster, wand, hand, false);
            }
        }
        castSpell(level, caster, wand, hand);
        if (isHost) {
            PacketDistributor.sendToServer(new SpellCastPayload(this, Optional.ofNullable(wand), caster.getId()));
        }
    }

    // ここをそれぞれオーバーライドすること
    public void castSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {

    }

    @Override
    public boolean canCast(Level level, LivingEntity caster) {
        return true;
    }

    @Override
    public void tick(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, boolean isHost) {
        if (level.isClientSide) {
            setUsedHand(caster, hand);
            if (isLongSpell()) {
                if (caster instanceof Player player) {
                    Optional<ResourceLocation> tickAnim = this.getConfig().tickAnim();
                    tickAnim.ifPresent(anim -> AnimationHelper.playAnim(player, anim));
                }
            }
        } else {
            if (isLongSpell()) {
                ChargeData data = caster.getData(DataAttachmentInit.SPELL_CHARGE);
                if (data.chargeTicks() <= 0) {
                    if (this.getConfig().continuous()) {
                        // TODO: ここで継続的な効果を発動
                    } else {
                        caster.stopUsingItem();
                    }
                }
            }
        }
        boolean charging = this.getConfig().hasCharge() && caster.getData(DataAttachmentInit.SPELL_CHARGE).chargeTicks() > 0;
        this.tickSpell(level, caster, wand, hand, charging);
    }

    // ここをそれぞれオーバーライドすること
    public void tickSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, boolean charging) {

    }

    @Override
    public void end(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, boolean isHost) {
        if (level.isClientSide) {
            if (caster instanceof Player player) {
                Optional<ResourceLocation> endAnim = this.getConfig().endAnim();
                endAnim.ifPresent(anim -> AnimationHelper.playAnim(player, anim));
            }
        } else {
            caster.stopUsingItem();
        }
        endSpell(level, caster, wand, hand);
        if (isHost) {
            PacketDistributor.sendToServer(new SpellCastPayload(this, Optional.ofNullable(wand), caster.getId()));
        }
    }

    // ここをそれぞれオーバーライドすること
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {

    }

    private boolean isLongSpell() {
        return this.getConfig().continuous() || this.getConfig().hasCharge();
    }

    private static void setUsedHand(LivingEntity caster, @Nullable InteractionHand hand) {
        if (caster instanceof Player user) {
            UsedHandData.setUsedHand(user, (hand == InteractionHand.OFF_HAND) ^ (user.getMainArm() == HumanoidArm.LEFT));
        }
    }

    public List<Component> getTooltip(Level level, LivingEntity caster, @Nullable ItemStack wand) {
        List<Component> list = new ArrayList<>();
        SpellConfig config = this.getConfig();
        Element element = config.element();
        SpellShape shape = config.shape();
        long cost = config.cost();
        SpellProperties properties = config.properties();
        list.add(element.getSpellElementName().withColor(element.getColor().getRGB()).append(Component.literal(" ").append(Component.translatable("spell_shape.magitech." + shape.get()).withColor(shape.getDark()))));
        for (SpellPropertyKey<?> key : properties.map().keySet()) {
            MutableComponent component = key.getDisplayText(this, caster, wand);
            list.add(component);
        }
        if (config.hasCharge()) {
            list.add(Component.translatable("tooltip.magitech.spell.charge_time").append(": " + MathHelper.round((double) MagicPerformanceHelper.getEffectiveSpellPropertyWithDivide(caster, wand, cost, config.chargeTime().get(), AttributeInit.CASTING_SPEED) / 20, 2) + "s"));
        }
        list.add(Component.translatable("tooltip.magitech.spell.cooldown_time").append(": " + MathHelper.round(MagicPerformanceHelper.getEffectiveSpellPropertyWithDivide(caster, wand, cost, config.cooldown(), AttributeInit.COOLDOWN_SPEED), 2) + "kJ"));
        list.add(Component.empty());
        list.add(Component.translatable("tooltip.magitech.spell.mana_cost").append(": " + MathHelper.round(MagicPerformanceHelper.getEffectiveSpellPropertyWithDivide(caster, wand, cost, cost, AttributeInit.MANA_EFFICIENCY), 2)).withColor(0x40FFF0));
        if (config.continuous()) {
            list.add(Component.translatable("tooltip.magitech.spell.continuous_mana_cost").append(": " + MathHelper.round(MagicPerformanceHelper.getEffectiveSpellPropertyWithDivide(caster, wand, cost, config.costPerTick().get(), AttributeInit.MANA_EFFICIENCY), 2) + "kJ/tick").withColor(0x40FFF0));
        }
        return list;
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
