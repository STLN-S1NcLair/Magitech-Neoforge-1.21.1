package net.stln.magitech.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.entity.AdjustableAttackStrengthEntity;
import net.stln.magitech.entity.MagicBulletEntity;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.surge.Stormhaze;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;

public class ThreadboundItem extends Item implements ICurioItem {

    Map<Holder<Attribute>, AttributeModifier> attributeModifiers = new HashMap<>();

    public ThreadboundItem(Properties settings) {
        super(settings);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> modifierMultimap = ImmutableMultimap.builder();
        for (Map.Entry<Holder<Attribute>, AttributeModifier> modifier : attributeModifiers.entrySet()) {
            modifierMultimap.put(modifier.getKey(), modifier.getValue());
        }
        return modifierMultimap.build();
    }

    public ThreadboundItem attributeModifier(Map<Holder<Attribute>, AttributeModifier> map) {
        attributeModifiers = map;
        return this;
    }
}
