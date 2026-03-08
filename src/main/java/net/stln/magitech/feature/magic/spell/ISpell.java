package net.stln.magitech.feature.magic.spell;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.element.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface ISpell extends SpellLike {

    public static final Codec<ISpell> CODEC = MagitechRegistries.SPELL.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ISpell> STREAM_CODEC = ByteBufCodecs.registry(MagitechRegistries.Keys.SPELL);

    // スペルの基本情報: 属性、形状、マナコストなどを定義するためのメソッド
    SpellConfig getConfig();

    // 発射元のアイテムがない場合にはwandはnull
    // プレイヤーでない場合にはhandはnullになる可能性がある
    // スペルを開始する: 即時スペルの場合は即endに飛び、持続スペルの場合はtickに飛ぶ(ここでは効果は発動させないこと: アニメーションなどの開始のために使用する)
    void cast(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, boolean isHost);

    // 持続スペル用に、スペルが発動している間に毎ティック呼び出されるメソッド
    // ticks: 発動中の時間
    void tick(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, int ticks, boolean isHost);

    // スペルが終了したときに呼び出されるメソッド: 持続スペルの効果を解除するためなどに使用
    void end(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand, boolean isHost);

    // キャスト可能かどうかの判定: スペルの条件を満たしているかなどをチェックするために使用(マナコストのチェックなど)
    boolean canCast(Level level, LivingEntity caster);

    boolean canContinuousCast(Level level, LivingEntity caster);

    // スペルの文字情報の取得
    List<Component> getTooltip(Level level, LivingEntity caster, ItemStack stack);

    @NotNull ResourceLocation getId();

    @NotNull String getDescriptionId();

    @NotNull MutableComponent getDescription();

    @NotNull ResourceLocation getIconId();
}
