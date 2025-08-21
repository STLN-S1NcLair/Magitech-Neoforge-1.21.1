package net.stln.magitech.magic.spell.tremor;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.util.Element;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.BeamSpell;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.WaveParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.SpellShape;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Oscilbeam extends BeamSpell {

    public Oscilbeam() {
        baseDamage = 10.0F;
        baseMaxRange = 31;
        beamradius = 0.7;
    }

    public Element getElement() {
        return Element.TREMOR;
    }

    public SpellShape getSpellShape() {
        return SpellShape.BEAM;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 55.0);
        cost.put(ManaUtil.ManaType.NOCTIS, 7.5);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 180;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        addCharge(user, 15, this.getElement());
        super.use(level, user, hand, isHost);
    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, boolean isHost) {
        super.finishUsing(stack, level, livingEntity, timeCharged, isHost);
    }

    @Override
    protected void applyEffectToLivingTarget(Level level, Player user, LivingEntity target) {
        super.applyEffectToLivingTarget(level, user, target);
    }

    @Override
    protected void playBeamSound(Level level, Player user) {
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.SONICBOOM.get(), SoundSource.PLAYERS, 1.0F, 0.5F + (user.getRandom().nextFloat() * 0.5F));
    }

    @Override
    protected void addVisualEffect(Level level, Player user, Vec3 start, Vec3 hitPos) {
        EffectUtil.lineEffect(level, new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0), start, hitPos, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.0F, 0.7F, 0.7F), new Vector3f(0.0F, 1.0F, 1.0F), hitPos.toVector3f(), 1.8F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new WaveParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0),
                    hitPos.x + (user.getRandom().nextFloat() - 0.5) / 3, hitPos.y + (user.getRandom().nextFloat() - 0.5) / 3, hitPos.z + (user.getRandom().nextFloat() - 0.5) / 3, 0, 0, 0);
        }
    }
}
