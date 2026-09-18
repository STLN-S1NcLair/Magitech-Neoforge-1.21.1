package net.stln.magitech.content.item.debug;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import net.stln.magitech.core.api.field_effect.data.FieldEffectManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FieldInfluenceDebugItem extends Item {
    private static final Map<UUID, Boolean> COLD_MODE_BY_PLAYER = new HashMap<>();

    public FieldInfluenceDebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (context.getPlayer() == null) {
            return InteractionResult.PASS;
        }

        ItemStack stack = context.getItemInHand();
        if (context.getPlayer().isSecondaryUseActive()) {
            if (!level.isClientSide()) {
                toggleMode(context.getPlayer());
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (level.isClientSide()) {
            return InteractionResult.sidedSuccess(true);
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        BlockPos source = context.getClickedPos();
        FieldEffectManager manager = FieldEffectManager.get(serverLevel);
        if (!manager.getRangesBySource(source).isEmpty()) {
            manager.removeRangesBySource(serverLevel, source);
            return InteractionResult.SUCCESS;
        }

        FieldInfluenceType type = getCurrentType(context.getPlayer());
        FieldInfluenceInstance instance = FieldInfluenceInstance.of(new FieldInfluence(type, 1));
        manager.addRange(serverLevel, source.offset(-1, -1, -1), source.offset(1, 1, 1), source, instance);
        return InteractionResult.SUCCESS;
    }

    private static void toggleMode(Player player) {
        UUID uuid = player.getUUID();
        COLD_MODE_BY_PLAYER.put(uuid, !COLD_MODE_BY_PLAYER.getOrDefault(uuid, false));
    }

    private static FieldInfluenceType getCurrentType(Player player) {
        return COLD_MODE_BY_PLAYER.getOrDefault(player.getUUID(), false)
                ? FieldInfluenceInit.COLD.get()
                : FieldInfluenceInit.HEAT.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag flag) {
        tooltipComponents.add(Component.literal("Shift-right click: toggle HEATED / COLD"));
        tooltipComponents.add(Component.literal("Right click block: place/remove 3x3x3 field influence"));
        super.appendHoverText(stack, context, tooltipComponents, flag);
    }
}


