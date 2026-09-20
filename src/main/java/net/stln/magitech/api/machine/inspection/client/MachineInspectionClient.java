package net.stln.magitech.api.machine.inspection.client;

import net.minecraft.core.BlockPos;
import net.stln.magitech.api.machine.inspection.MachineInspectionData;
import org.jetbrains.annotations.Nullable;

public final class MachineInspectionClient {
    private static final int MAX_DATA_AGE = 12;
    private static final int FADE_IN_TICKS = 5;
    private static final int FADE_OUT_TICKS = 2;
    private static final float FADE_IN_STEP = 1.0F / FADE_IN_TICKS;
    private static final float FADE_OUT_STEP = 1.0F / FADE_OUT_TICKS;
    private static @Nullable MachineInspectionData data;
    private static @Nullable BlockPos currentDisplayPosition;
    private static int dataAge = MAX_DATA_AGE + 1;
    private static float fadeAlpha;
    private static boolean targetActive;

    private MachineInspectionClient() {
    }

    public static void accept(MachineInspectionData nextData) {
        if (targetActive && currentDisplayPosition != null
                && !currentDisplayPosition.equals(nextData.displayPosition())) {
            return;
        }

        boolean displayChanged = data == null
                || !data.displayPosition().equals(nextData.displayPosition());
        data = nextData;
        dataAge = 0;
        if (displayChanged) {
            fadeAlpha = 0.0F;
        }
    }

    public static void updateTarget(BlockPos displayPosition) {
        targetActive = true;
        currentDisplayPosition = displayPosition.immutable();
    }

    public static void tick() {
        if (dataAge < Integer.MAX_VALUE) {
            dataAge++;
        }

        if (hasCurrentData()) {
            fadeAlpha = Math.min(1.0F, fadeAlpha + FADE_IN_STEP);
        } else {
            fadeAlpha = Math.max(0.0F, fadeAlpha - FADE_OUT_STEP);
        }
        if (fadeAlpha <= 0.0F && !hasCurrentData()) {
            data = null;
        }
    }

    public static void clear() {
        targetActive = false;
        currentDisplayPosition = null;
    }

    public static @Nullable MachineInspectionData getDataForRender() {
        return data == null || fadeAlpha <= 0.0F ? null : data;
    }

    public static float getFadeAlpha(float partialTick) {
        if (data == null || fadeAlpha <= 0.0F) {
            return 0.0F;
        }
        float fadeStep = hasCurrentData() ? FADE_IN_STEP : FADE_OUT_STEP;
        float interpolatedAlpha = hasCurrentData()
                ? fadeAlpha + partialTick * fadeStep
                : fadeAlpha - partialTick * fadeStep;
        return Math.clamp(interpolatedAlpha, 0.0F, 1.0F);
    }

    private static boolean hasCurrentData() {
        return targetActive
                && data != null
                && dataAge <= MAX_DATA_AGE
                && currentDisplayPosition != null
                && currentDisplayPosition.equals(data.displayPosition());
    }
}
