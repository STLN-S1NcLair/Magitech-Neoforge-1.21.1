package net.stln.magitech.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadboundItem extends Item {

    public ThreadboundItem(Properties settings) {
        super(settings);
    }

}
