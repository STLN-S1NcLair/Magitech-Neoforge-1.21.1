package net.stln.magitech.magic.cooldown;

import net.stln.magitech.item.tool.element.Element;

public class Cooldown {
    private double progress;
    private double cooltime;
    private Element element;

    public Cooldown(double maxCharge, Element element) {
        this.progress = 0;
        this.cooltime = maxCharge;
        this.element = element;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getCooltime() {
        return cooltime;
    }

    public void setCooltime(double cooltime) {
        this.cooltime = cooltime;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
