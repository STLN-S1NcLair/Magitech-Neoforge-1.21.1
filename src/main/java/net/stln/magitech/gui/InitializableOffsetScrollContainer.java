package net.stln.magitech.gui;

import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;

public class InitializableOffsetScrollContainer<C extends Component> extends ScrollContainer<C> {

    protected InitializableOffsetScrollContainer(ScrollDirection direction, Sizing horizontalSizing, Sizing verticalSizing, C child) {
        super(direction, horizontalSizing, verticalSizing, child);
    }

    public static <C extends Component> InitializableOffsetScrollContainer<C> verticalScroll(Sizing horizontalSizing, Sizing verticalSizing, C child) {
        return new InitializableOffsetScrollContainer<>(ScrollDirection.VERTICAL, horizontalSizing, verticalSizing, child);
    }

    public double getScrollOffset() {
        return this.scrollOffset;
    }

    public void setScrollOffset(double scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public double getScrollPosition() {
        return this.currentScrollPosition;
    }

    public void setScrollPosition(double scrollPosition) {
        this.currentScrollPosition = scrollPosition;
    }
}
