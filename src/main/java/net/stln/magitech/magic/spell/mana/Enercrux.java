package net.stln.magitech.magic.spell.mana;

import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.item.tool.Element;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.BeamSpell;
import net.stln.magitech.magic.spell.SpellRegister;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.ManaZapParticleEffect;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.SpellConversionRecipe;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Enercrux extends BeamSpell {

    public Enercrux() {
        baseDamage = 5.0F;
        baseMaxRange = 8;
        beamradius = 0.3;
    }

    public Element getElement() {
        return Element.NONE;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getBaseRequiredMana() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 20.0);
        return cost;
    }

    @Override
    public int getCooldown(Level level, Player user, ItemStack stack) {
        return 30;
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand, boolean isHost) {
        addCharge(user, 5, this.getElement());
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
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.ENERCRUX.get(), SoundSource.PLAYERS, 1.0F, 0.75F + (user.getRandom().nextFloat() * 0.5F));
    }

    @Override
    protected void addVisualEffect(Level level, Player user, Vec3 start, Vec3 hitPos) {
        EffectUtil.lineEffect(level, new UnstableSquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, user.getRandom().nextInt(2, 5), 0), start, hitPos, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), hitPos.toVector3f(), 0.4F, user.getRandom().nextInt(2, 5), 10), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new UnstableSquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, user.getRandom().nextInt(2, 5), 0),
                    hitPos.x + (user.getRandom().nextFloat() - 0.5) / 3, hitPos.y + (user.getRandom().nextFloat() - 0.5) / 3, hitPos.z + (user.getRandom().nextFloat() - 0.5) / 3,
                    Mth.nextFloat(user.getRandom(), -0.2F, 0.2F), Mth.nextFloat(user.getRandom(), -0.2F, 0.2F), Mth.nextFloat(user.getRandom(), -0.2F, 0.2F));
        }
        level.addParticle(new ManaZapParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F),
                        new Vector3f((float) (hitPos.x), (float) (hitPos.y), (float) (hitPos.z)), 1.0F, user.getRandom().nextInt(2, 5), 0),
                start.x, start.y, start.z, 0, 0, 0);
    }

    @Override
    protected void applyEffectToItem(Level level, Player user, Entity target) {
        super.applyEffectToItem(level, user, target);
        if (target instanceof ItemEntity item) {
            Optional<RecipeHolder<SpellConversionRecipe>> recipeHolder = level.getRecipeManager().getRecipeFor(RecipeInit.SPELL_CONVERSION_TYPE.get(), new SingleRecipeInput(item.getItem()), level);
            if (recipeHolder.isPresent()) {
                SpellConversionRecipe recipe = recipeHolder.get().value();
                if (recipe.getSpell().equals(SpellRegister.getId(this))) {
                    ItemStack stack = recipe.assemble(new SingleRecipeInput(item.getItem()), null);
                    int count = item.getItem().getCount() * stack.getCount();
                    while (count > 0) {
                        int spawnCount = Math.min(stack.getMaxStackSize(), count);
                        ItemStack result = stack.copy();
                        result.setCount(spawnCount);
                        ItemEntity newItem = new ItemEntity(level, item.getX(), item.getY(), item.getZ(), result, Mth.nextFloat(item.getRandom(), -0.3F, 0.3F), 0.3, Mth.nextFloat(item.getRandom(), -0.3F, 0.3F));
                        level.addFreshEntity(newItem);
                        count -= spawnCount;
                    }
                    item.discard();
                }
            }
        }
    }
}
