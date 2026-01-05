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

    public static InitializableOffsetScrollContainer<io.wispforest.owo.ui.core.Component> addPanel(FlowLayout root, Positioning positioning, long mana, long maxMana, long flow, long maxFlow, double scrollOffset, double scrollPosition) {

        FlowLayout layout = Containers.verticalFlow(Sizing.content(), Sizing.content()).gap(1); // ラベル間の隙間
        List<Component> components = new ArrayList<>();
        int titleColor = 0x5a9e91;
        int color = 0xcdffde;
        ManaContainerUtil.addManaContainerBlockInfo(mana, maxMana, flow, maxFlow, components, color, titleColor);

        for (net.minecraft.network.chat.Component component : components) {
            LabelComponent label = Components.label(component).shadow(false);
            layout.child(label);
        }

        InitializableOffsetScrollContainer<io.wispforest.owo.ui.core.Component> component = InitializableOffsetScrollContainer.verticalScroll(Sizing.fixed(126), Sizing.fixed(32), layout);
        component.positioning(Positioning.absolute(1, 1));
        component.setScrollOffset(scrollOffset);
        component.setScrollPosition(scrollPosition);

        StackLayout panel = Containers.stack(Sizing.fixed(128), Sizing.fixed(34));
        panel.positioning(positioning);


        // コンテナに追加
        panel.child(component);

        root.child(panel);
        return component;
    }
}
