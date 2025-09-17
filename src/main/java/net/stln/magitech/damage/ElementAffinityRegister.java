package net.stln.magitech.damage;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.stln.magitech.item.tool.element.Element;
import net.stln.magitech.util.TableHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ElementAffinityRegister {
    private static final Table<Element, Boolean, List<Element>> dict = HashBasedTable.create();
    public static boolean EFFICIENT = true;
    public static boolean INEFFICIENT = false;

    public static @NotNull ElementAffinity getElementAffinity(@NotNull Element element, @NotNull Element targetElement) {
        if (dict.containsRow(element)) {
            if (getElements(element, EFFICIENT).contains(targetElement)) {
                return ElementAffinity.EFFICIENT;
            }
            if (getElements(element, INEFFICIENT).contains(targetElement)) {
                return ElementAffinity.INEFFICIENT;
            }
        }
        return ElementAffinity.NORMAL;
    }

    public static void registerAffinity(@NotNull Element element, boolean efficiency, @NotNull Element target) {
        List<Element> list = getElements(element, efficiency);
        list.add(target);
        dict.put(element, efficiency, list);
    }

    private static @NotNull List<Element> getElements(@NotNull Element element, boolean bool) {
        return TableHelper.computeIfAbsent(dict, element, bool, (t, u) -> new ArrayList<>());
    }
}
