package net.stln.magitech.api.mana.flow;

public record ManaFlowRule(float flowBias, boolean canExtract, boolean canInsert) {

    public static ManaFlowRule BothWays(float flowBias) {
        return new ManaFlowRule(flowBias, true, true);
    }

    public static ManaFlowRule InsertOnly(float flowBias) {
        return new ManaFlowRule(flowBias, false, true);
    }

    public static ManaFlowRule ExtractOnly(float flowBias) {
        return new ManaFlowRule(flowBias, true, false);
    }

    public static ManaFlowRule None() {
        return new ManaFlowRule(0.0F, false, false);
    }

    public boolean isNone() {
        return !canExtract && !canInsert;
    }
}
