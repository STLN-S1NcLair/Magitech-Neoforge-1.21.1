package net.stln.magitech.core.api.field_effect;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.MagitechRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * フィールド効果の処理と表示を定義する基底クラスです。
 * Base class defining field-effect processing and visualization.
 */
public abstract class FieldEffectType implements FieldEffectTypeLike {
    public static final Codec<FieldEffectType> CODEC = MagitechRegistries.FIELD_EFFECT_TYPE.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, FieldEffectType> STREAM_CODEC = ByteBufCodecs.registry(MagitechRegistries.Keys.FIELD_EFFECT_TYPE);

    /**
     * アイテム入力に対して処理可能か判定します。
     * Determines whether the effect can process item inputs.
     */
    public abstract boolean canProcess(Level level, List<ItemStack> inputs);

    /**
     * ブロック位置に対して処理可能か判定します。
     * Determines whether the effect can process a block position.
     */
    public abstract boolean canProcess(Level level, BlockPos pos);

    /**
     * アイテム処理の間隔を tick 単位で返します。
     * Returns the item-processing interval in ticks.
     */
    public int getProcessTime(Level level, List<ItemStack> inputs) {
        int total = 0;
        for (ItemStack stack : inputs) {
            total += stack.getCount();
        }
        return (int) (40 + 200 * Math.log(total));
    }

    /**
     * ブロック処理の間隔を tick 単位で返します。
     * Returns the block-processing interval in ticks.
     */
    public int getProcessTime(Level level, BlockPos pos) {
        return 100;
    }

    /**
     * アイテム入力に対する処理を実行します。
     * Processes item inputs.
     */
    public List<ItemStack> processItem(Level level, List<ItemStack> inputs) {
        return inputs;
    }

    /**
     * 単一のアイテムを処理し、処理後のスタックを返します。
     * Processes a single item and returns the stack after processing.
     *
     * <p>既存の複数入力向け処理を単一対象へ接続するための標準アダプターです。
     * This is a standard adapter that connects the existing multi-input operation to a single target.</p>
     *
     * @param level 処理対象のワールド / target level
     * @param input 処理前のアイテム / item before processing
     * @return 処理後のアイテム、空になった場合は空スタック / processed item, or an empty stack when consumed
     */
    public List<ItemStack> processItem(Level level, ItemStack input) {
        List<ItemStack> inputs = new ArrayList<>(1);
        inputs.add(input.copy());
        return processItem(level, inputs);
    }

    /**
     * ブロック位置に対する処理を実行します。
     * Processes a block position.
     */
    public List<ItemStack> processBlock(Level level, BlockPos pos) {
        return List.of(level.getBlockState(pos).getBlock().asItem().getDefaultInstance());
    }

    /**
     * 範囲内のエンティティに効果を適用します。
     * Applies the effect to an entity inside the field.
     */
    public void affectEntity(Entity entity) {

    }

    /**
     * この効果が要求する影響条件を返します。
     * Returns the influence condition required by this effect.
     */
    public abstract FieldInfluenceInstance getCondition();

    /**
     * この効果のGUIアイコンテクスチャを返します。
     * Returns the GUI icon texture for this effect.
     *
     * @return アイコンテクスチャ、未登録時はnull / icon texture, or null while unregistered
     */
    public @Nullable ResourceLocation getIconTexture() {
        ResourceLocation key = MagitechRegistries.FIELD_EFFECT_TYPE.getKey(this);
        return key == null
                ? null
                : ResourceLocation.fromNamespaceAndPath(
                        key.getNamespace(),
                        "textures/field_effect/" + key.getPath() + ".png"
                );
    }

    /**
     * この効果の表示名を返します。
     * Returns the display name of this effect.
     */
    public Component getDisplayName() {
        ResourceLocation key = MagitechRegistries.FIELD_EFFECT_TYPE.getKey(this);
        return key == null
                ? Component.empty()
                : Component.translatable("field_effect." + key.getNamespace() + "." + key.getPath());
    }

    /**
     * この効果の主色を返します。
     * Returns the primary color of this effect.
     *
     * @return 主色 / primary color
     */
    public Color getPrimary() {
        return Color.WHITE;
    }

    /**
     * この効果の副色を返します。
     * Returns the secondary color of this effect.
     *
     * @return 副色 / secondary color
     */
    public Color getSecondary() {
        return Color.WHITE;
    }

    /**
     * 指定位置にこの効果の視覚効果を描画します。
     * Renders this effect's visual effect at the specified position.
     */
    public abstract void renderVFX(Level level, BlockPos pos);

    /**
     * このオブジェクトをフィールド効果タイプとして返します。
     * Returns this object as a field-effect type.
     */
    @Override
    public @NotNull FieldEffectType asFieldEffectType() {
        return this;
    }
}
