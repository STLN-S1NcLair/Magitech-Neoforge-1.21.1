package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.damage.DamageTypeInit;
import net.stln.magitech.entity.mobeffect.MobEffectInit;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.particle.particle_option.PowerupNoCullParticleEffect;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.particle.particle_option.WaveNoCullParticleEffect;
import net.stln.magitech.particle.particle_option.WaveParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlindResonanceTrait extends Trait {

    @Override
    public void modifyAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, List<ItemAttributeModifiers.Entry> entries) {
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light < 4) {
            entries.add(new ItemAttributeModifiers.Entry(Attributes.MOVEMENT_SPEED, new AttributeModifier(Magitech.id("blind_touch"), traitLevel * 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
        }
        super.modifyAttribute(player, level, stack, traitLevel, stats, entries);
    }

    @Override
    public void modifySpellCasterAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, List<ItemAttributeModifiers.Entry> entries) {
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light < 4) {
            entries.add(new ItemAttributeModifiers.Entry(Attributes.MOVEMENT_SPEED, new AttributeModifier(Magitech.id("blind_touch"), traitLevel * 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
        }
        super.modifySpellCasterAttribute(player, level, stack, traitLevel, stats, entries);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light < 4) {
            List<ToolMaterial> materials = ComponentHelper.getPartMaterials(stack);
            Set<ToolMaterial> materialSet = PartToolItem.getMaterialSet(materials);
            ToolStats defaultStats = ToolStats.DEFAULT;
            Map<String, Float> statsMap = stats.getStats();
            Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
            modified.put(ToolStats.MNA_STAT, statsMap.get(ToolStats.MNA_STAT) * traitLevel * 0.1F);
            return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
        }
        return super.modifySpellCasterStatsConditional1(player, level, stack, traitLevel, stats);
    }

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, stats, target);
        if (target instanceof LivingEntity livingEntity) {
            ResourceKey<DamageType> damageType = DamageTypeInit.TREMOR_DAMAGE;
            DamageSource damageSource = player.damageSources().source(damageType, player);

            target.invulnerableTime = 0;
            livingEntity.hurt(damageSource, livingEntity.getArmorValue() * 0.25F * traitLevel);

            EffectUtil.entityEffect(level, new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0), livingEntity, 60);
        }
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        level.updateSkyBrightness();
        int light = level.getMaxLocalRawBrightness(player.blockPosition());
        if (light < 4 && !player.hasEffect(MobEffects.NIGHT_VISION)) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.0F, 0.5F, 0.5F), new Vector3f(0.0F, 1.0F, 1.0F), 1F, 1, 0), player, 1);
            List<Entity> entities = EntityUtil.getEntitiesInBox(level, player, player.position(), new Vec3(10, 10, 10));
            for (Entity entity : entities) {
                if (player.getRandom().nextFloat() < 0.2F) {
                    EffectUtil.entityEffect(level, new PowerupNoCullParticleEffect(new Vector3f(0.0F, 0.5F, 0.5F), new Vector3f(0.0F, 1.0F, 1.0F), 1F, 1, 0), entity, 1);
                    EffectUtil.entityEffect(level, new WaveNoCullParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 5F, 1, 0), entity, 1);
                }
            }
        }
    }

    @Override
    public void traitAction(Player player, Level level, Entity target, Vec3 lookingPos, ItemStack stack, int traitLevel, ToolStats stats, InteractionHand hand, boolean isHost) {
        super.traitAction(player, level, target, lookingPos, stack, traitLevel, stats, hand, isHost);
        if (!level.isClientSide() && !isHost) {
            stack.hurtAndBreak(5, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            player.addEffect(new MobEffectInstance(MobEffectInit.ECHOLOCATION, 60));
        }
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.ECHOLOCATION.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
    }

    @Override
    public int getColor() {
        return 0x008090;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.blind_resonance");
    }
}
