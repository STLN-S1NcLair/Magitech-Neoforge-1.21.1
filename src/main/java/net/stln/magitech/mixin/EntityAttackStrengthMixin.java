package net.stln.magitech.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.entity.AdjustableAttackStrengthEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class EntityAttackStrengthMixin implements AdjustableAttackStrengthEntity {

    @Unique
    LivingEntity entity = (LivingEntity) (Object) this;

    @Shadow
    protected int attackStrengthTicker;

    @Override
    @Unique
    public int getLastAttackedTicks() {
        return this.attackStrengthTicker;
    }
    @Unique
    public void setLastAttackedTicks(int value) {
        this.attackStrengthTicker = value;
    }
}
