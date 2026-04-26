package net.stln.magitech.core.api.mana.flow;

public record ManaFlowRule(float flowBias, boolean canExtract, boolean canInsert) {

    public static ManaFlowRule bothWays(float flowBias) {
        return new ManaFlowRule(flowBias, true, true);
    }

    public static ManaFlowRule insertOnly(float flowBias) {
        return new ManaFlowRule(flowBias, false, true);
    }

    public static ManaFlowRule extractOnly(float flowBias) {
        return new ManaFlowRule(flowBias, true, false);
    }

    public static ManaFlowRule none() {
        return new ManaFlowRule(0.0F, false, false);
    }

    public boolean isNone() {
        return !canExtract && !canInsert;
    }
}
