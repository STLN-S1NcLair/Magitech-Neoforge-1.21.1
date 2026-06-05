package net.stln.magitech.feature.tool.trait;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.tool.property.ToolProperties;

import java.awt.*;
import java.util.List;

public class EnderDrawTrait extends Trait {

    @Override
    public void handTick(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, boolean isHost) {
        if (player.isCrouching()) {
            float range = traitLevel * 8;
            Vec3 rangeVec = new Vec3(range, range, range);
            List<Entity> list = level.getEntities(player, new AABB(player.getPosition(0F).subtract(rangeVec), player.getPosition(0F).add(rangeVec)), entity -> entity instanceof ItemEntity);
            Entity item = null;
            for (Entity entity : list.reversed()) {
                if (level.getNearestPlayer(entity, 0.5) == null) {
                    item = entity;
                    break;
                }
            }
            if (item != null) {
                Vec3 playerPos = new Vec3(player.getX(), player.getY(0.5F), player.getZ());
                Vec3 itemPos = new Vec3(item.getX(), item.getY(0.5F), item.getZ());
                LineVFX.destinationLinedColor(level, itemPos, playerPos, getPrimary(), getSecondary(), SquareParticles::squareParticleColored, Section.cover(), 10, 0.2F, 0.0F);
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
                item.setPos(player.getPosition(0F));
                item.setDeltaMovement(0, 0, 0);
            }
        }
    }

    @Override
    public Color getColor() {
        return new Color(0x006050);
    }

    @Override
    public Color getPrimary() {
        return new Color(0x30CC90);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x006970);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("ender_draw");
    }
}
