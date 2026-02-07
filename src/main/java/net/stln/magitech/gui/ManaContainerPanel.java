package net.stln.magitech.gui;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.network.chat.Component;
import net.stln.magitech.util.ManaContainerUtil;

import java.util.ArrayList;
import java.util.List;

public class ManaContainerPanel {

    public static InitializableOffsetScrollContainer<io.wispforest.owo.ui.core.Component> addPanel(FlowLayout root, Positioning positioning, ManaContainerMenu menu,
                                                                                                   double scrollOffset, double scrollPosition, int panelWidth, int panelHeight) {
        return addPanel(root, positioning, menu.getMana(), menu.getMaxMana(), menu.getFlowRate(), menu.getMaxManaFlow(), menu.getProductionRate(), menu.getConsumptionRate(), scrollOffset, scrollPosition, panelWidth, panelHeight, menu.hasProduction, menu.hasConsumption);
    }

    public static InitializableOffsetScrollContainer<io.wispforest.owo.ui.core.Component> addPanel(FlowLayout root, Positioning positioning, long mana, long maxMana, long maxFlow,
                                                                                                   double scrollOffset, double scrollPosition, int panelWidth, int panelHeight) {
        return addPanel(root, positioning, mana, maxMana, 0, maxFlow, 0, 0, scrollOffset, scrollPosition, panelWidth, panelHeight, false, false);
    }

    public static InitializableOffsetScrollContainer<io.wispforest.owo.ui.core.Component> addPanel(FlowLayout root, Positioning positioning, long mana, long maxMana, long flow, long maxFlow, long production, long consumption,
                                                                                                   double scrollOffset, double scrollPosition, int panelWidth, int panelHeight, boolean hasProduction, boolean hasConsumption) {

        FlowLayout layout = Containers.verticalFlow(Sizing.content(), Sizing.content()).gap(1); // ラベル間の隙間
        List<Component> components = new ArrayList<>();
        int titleColor = 0x5a9e91;
        int color = 0xcdffde;
        ManaContainerUtil.addManaContainerBlockInfo(mana, maxMana, flow, maxFlow, production, consumption, components, color, titleColor, hasProduction, hasConsumption);

        for (net.minecraft.network.chat.Component component : components) {
            LabelComponent label = Components.label(component).shadow(false);
            layout.child(label);
        }

        InitializableOffsetScrollContainer<io.wispforest.owo.ui.core.Component> component = InitializableOffsetScrollContainer.verticalScroll(Sizing.fixed(panelWidth - 2), Sizing.fixed(panelHeight - 2), layout);
        component.positioning(Positioning.absolute(1, 1));
        component.setScrollOffset(scrollOffset);
        component.setScrollPosition(scrollPosition);

        StackLayout panel = Containers.stack(Sizing.fixed(panelWidth), Sizing.fixed(panelHeight));
        panel.positioning(positioning);


        // コンテナに追加
        panel.child(component);

        root.child(panel);
        return component;
    }
}
