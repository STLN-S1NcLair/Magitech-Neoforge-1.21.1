package net.stln.magitech.gui;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.ArrayList;
import java.util.List;

public class ToolStatsPanel {
    private static final ResourceLocation TEXTURE = Magitech.id("textures/gui/tool_stats_panel.png");

    public static void addPanel(FlowLayout root, Positioning positioning, ItemStack stack, net.minecraft.network.chat.Component title, List<Component> whenEmpty) {
        List<net.minecraft.network.chat.Component> components = new ArrayList<>();
        if (stack != null && stack.getItem() instanceof PartToolItem toolItem) {

            toolItem.addStatsHoverText(stack, components);
        } else {
            components.addAll(whenEmpty);
        }
        addPanel(root, positioning, stack, components, title);
    }

    public static void addPartPanel(FlowLayout root, Positioning positioning, ItemStack stack, net.minecraft.network.chat.Component title, List<Component> whenEmpty) {
        List<net.minecraft.network.chat.Component> components = new ArrayList<>();
        if (stack != null && stack.getItem() instanceof PartItem partItem) {

            partItem.addStatsHoverText(stack, components, true);
        } else {
            components.addAll(whenEmpty);
        }
        addPanel(root, positioning, stack, components, title);
    }

    public static void addPanel(FlowLayout root, Positioning positioning, ItemStack stack, List<Component> components, net.minecraft.network.chat.Component title) {

        // アイテムスロット背景
        var slotBg = Components.texture(TEXTURE, 0, 0, 160, 199);
        slotBg.sizing(Sizing.fixed(160), Sizing.fixed(199));

        // 実際のアイテム表示
        var item = Components.item(stack);
        item.positioning(Positioning.absolute(137, 31));

        FlowLayout layout = Containers.verticalFlow(Sizing.content(), Sizing.content()).gap(4); // ラベル間の隙間

        for (net.minecraft.network.chat.Component component : components) {
            LabelComponent label = Components.label(component).shadow(true);
            layout.child(label);
        }

        // ラベル
        LabelComponent label = Components.label(title);
        label.positioning(Positioning.absolute(8, 4));
        label.color(Color.ofRgb(0x333333));

        ScrollContainer<io.wispforest.owo.ui.core.Component> component = Containers.verticalScroll(Sizing.fixed(148), Sizing.fixed(159), layout);
        component.positioning(Positioning.absolute(6, 30));

        StackLayout panel = Containers.stack(Sizing.fixed(160), Sizing.fixed(199));
        panel.positioning(positioning);



        // コンテナに追加
        panel.child(slotBg);
        panel.child(item);
        panel.child(label);
        panel.child(component);

        root.child(panel);
    }
}
