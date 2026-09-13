package net.stln.magitech.core.api.mana.flow;

/**
 * マナの入出力可否と充填率バイアスを表します。
 * Describes mana insertion/extraction permissions and the fill-ratio bias.
 */
public record ManaFlowRule(float flowBias, boolean canExtract, boolean canInsert) {

    /**
     * 入出力の両方を許可するルールを生成します。
     * Creates a rule that allows both insertion and extraction.
     */
    public static ManaFlowRule bothWays(float flowBias) {
        return new ManaFlowRule(flowBias, true, true);
    }

    /**
     * 挿入のみを許可するルールを生成します。
     * Creates a rule that allows insertion only.
     */
    public static ManaFlowRule insertOnly(float flowBias) {
        return new ManaFlowRule(flowBias, false, true);
    }

    /**
     * 排出のみを許可するルールを生成します。
     * Creates a rule that allows extraction only.
     */
    public static ManaFlowRule extractOnly(float flowBias) {
        return new ManaFlowRule(flowBias, true, false);
    }

    /**
     * 入出力を禁止するルールを生成します。
     * Creates a rule that forbids both insertion and extraction.
     */
    public static ManaFlowRule none() {
        return new ManaFlowRule(0.0F, false, false);
    }

    /**
     * 入出力がどちらも禁止されているか判定します。
     * Determines whether both insertion and extraction are disabled.
     */
    public boolean isNone() {
        return !canExtract && !canInsert;
    }
}
