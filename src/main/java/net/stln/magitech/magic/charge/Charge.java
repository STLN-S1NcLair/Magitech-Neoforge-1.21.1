package net.stln.magitech.magic.charge;

import net.stln.magitech.util.Element;
import net.stln.magitech.magic.spell.Spell;

public class Charge {
    private double charge;
    private double maxCharge;
    private Spell spell;
    private Element element;

    public Charge(double maxCharge, Spell spell, Element element) {
        this.charge = 0;
        this.maxCharge = maxCharge;
        this.spell = spell;
        this.element = element;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getMaxCharge() {
        return maxCharge;
    }

    public void setMaxCharge(double maxCharge) {
        this.maxCharge = maxCharge;
    }

    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
