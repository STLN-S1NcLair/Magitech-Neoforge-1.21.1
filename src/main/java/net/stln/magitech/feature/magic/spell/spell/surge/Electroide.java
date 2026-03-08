package net.stln.magitech.feature.magic.spell.spell.surge;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.magicentity.electroide.ElectroideEntity;
import net.stln.magitech.content.entity.magicentity.volkarin.VolkarinEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.charge.ChargeData;
import net.stln.magitech.feature.magic.spell.ShotSpell;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

import java.util.HashMap;
import java.util.Map;

public class Electroide extends ShotSpell {

    public Electroide() {
        super(new SpellConfig.Builder(Element.SURGE, SpellShape.BOMB, 300, 65)
                        .charge(12)
                        .property(SpellPropertyInit.DAMAGE, 8.0F)
                        .property(SpellPropertyInit.PROJECTILE_SPEED, 2.0F)
                        .property(SpellPropertyInit.EXPLOSION_RADIUS, 7.0F)
                        .endSound(SoundInit.ELECTROIDE_SHOOT)
                        .castAnim("charge_wand")
                        .endAnim("swing_wand"),
                ElectroideEntity::new);
    }
}
