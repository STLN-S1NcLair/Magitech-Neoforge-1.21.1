package net.stln.magitech.effect.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class SoundHelper {

    // サーバー側で呼び出すこと
    public static void broadcastSound(Level level, Vec3 pos, SoundEvent event, SoundSource soundSource, float volume, float pitch) {
        level.playSound(null, pos.x, pos.y, pos.z, event, soundSource, volume, pitch);
    }

    public static void broadcastSound(Level level, Vec3 pos, SoundEvent event, SoundSource soundSource, float volume) {
        broadcastSound(level, pos, event, soundSource, volume, Mth.randomBetween(level.random, 0.7F, 1.3F));
    }

    public static void broadcastSound(Level level, Vec3 pos, SoundEvent event, SoundSource soundSource) {
        broadcastSound(level, pos, event, soundSource, 1.0F);
    }

    public static void broadcastSound(Level level, Vec3 pos, Optional<Supplier<SoundEvent>> event, SoundSource soundSource) {
        event.ifPresent(soundEvent -> broadcastSound(level, pos, soundEvent.get(), soundSource));
    }

    public static void broadcastSound(Level level, Entity caster, Optional<Supplier<SoundEvent>> event) {
        event.ifPresent(soundEvent -> broadcastSound(level, caster.position(), soundEvent.get(), getSoundSource(caster)));
    }

    public static void broadcastSound(Level level, Entity caster, Vec3 pos, Optional<Supplier<SoundEvent>> event) {
        event.ifPresent(soundEvent -> broadcastSound(level, pos, soundEvent.get(), getSoundSource(caster)));
    }

    public static void broadcastPlayerSound(Level level, Vec3 pos, Optional<Supplier<SoundEvent>> event) {
        event.ifPresent(soundEvent -> broadcastSound(level, pos, soundEvent.get(), SoundSource.PLAYERS));
    }

    public static void broadcastDelayedSound(Level level, LivingEntity caster, Vec3 pos, Optional<Supplier<SoundEvent>> event, int endTick) {
        if (event.isPresent()) {
            double castingSpeed = caster.getAttributeValue(AttributeInit.CASTING_SPEED);
            int delay = (int) Math.round(endTick / castingSpeed) - endTick;
            if (delay > 0) {
                TickScheduler.schedule(delay, () -> {
                    broadcastSound(level, pos, event.get().get(), getSoundSource(caster));
                }, level.isClientSide);
            } else {
                broadcastSound(level, pos, event.get().get(), getSoundSource(caster), 1.0F, (float) castingSpeed);
            }
        }
    }

    public static void broadcastDelayedSound(Level level, LivingEntity caster, Optional<Supplier<SoundEvent>> event, int endTick) {
        broadcastDelayedSound(level, caster, caster.position(), event, endTick);
    }

    public static @NotNull SoundSource getSoundSource(Entity caster) {
        return caster instanceof Player ? (caster instanceof Enemy ? SoundSource.HOSTILE : SoundSource.NEUTRAL) : SoundSource.PLAYERS;
    }
}
