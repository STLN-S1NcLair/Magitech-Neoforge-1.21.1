package net.stln.magitech.item.tool;

import net.stln.magitech.item.tool.material.MiningLevel;
import oshi.annotation.concurrent.Immutable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolStats {
    @Immutable
    public static final ToolStats DEFAULT = new ToolStats(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, Element.NONE, MiningLevel.NONE);

    public static String ATK_STAT = "attack";
    public static String ELM_ATK_STAT = "element_attack";
    public static String SPD_STAT = "attack_speed";
    public static String MIN_STAT = "mining_speed";
    public static String DEF_STAT = "defense";
    public static String RNG_STAT = "attack_range";
    public static String SWP_STAT = "sweep_range";
    public static String DUR_STAT = "durability";

    public static String PWR_STAT = "attack";
    public static String ELM_PWR_STAT = "element_attack";
    public static String CHG_STAT = "attack_speed";
    public static String CLD_STAT = "mining_speed";

    public static String PRJ_STAT = "attack_range";
    public static String MNA_STAT = "sweep_range";
    private final Map<String, Float> stats = new HashMap<>();
    private final Element element;
    private final MiningLevel miningLevel;

    public ToolStats(float atk, float elmAtk, float spd, float min, float def, float rng, float swp, float dur, Element elm, MiningLevel miningLevel) {
        this.miningLevel = miningLevel;
        this.stats.put(ATK_STAT, atk);
        this.stats.put(ELM_ATK_STAT, elmAtk);
        this.stats.put(SPD_STAT, spd);
        this.stats.put(MIN_STAT, min);
        this.stats.put(DEF_STAT, def);
        this.stats.put(RNG_STAT, rng);
        this.stats.put(SWP_STAT, swp);
        this.stats.put(DUR_STAT, dur);
        element = elm;
    }

    public ToolStats(Map<String, Float> map, Element elm, MiningLevel miningLevel) {
        this.miningLevel = miningLevel;
        stats.putAll(map);
        element = elm;
    }

    public static ToolStats add(List<ToolStats> statsList) {
        float atk = 0;
        float elmAtk = 0;
        float spd = 0;
        float min = 0;
        float def = 0;
        float rng = 0;
        float swp = 0;
        float dur = 0;
        Map<Element, Float> elementMap = new HashMap<>();
        Element elm = Element.NONE;
        MiningLevel minLv = MiningLevel.NONE;

        for (ToolStats stats : statsList) {
            if (stats != null) {
                Map<String, Float> map = stats.getStats();

                if (map.get(ATK_STAT) != null) {
                    atk += map.get(ATK_STAT);
                }
                if (map.get(SPD_STAT) != null) {
                    spd += map.get(SPD_STAT);
                }
                if (map.get(MIN_STAT) != null) {
                    min += map.get(MIN_STAT);
                }
                if (map.get(DEF_STAT) != null) {
                    def += map.get(DEF_STAT);
                }
                if (map.get(RNG_STAT) != null) {
                    rng += map.get(RNG_STAT);
                }
                if (map.get(SWP_STAT) != null) {
                    swp += map.get(SWP_STAT);
                }
                if (map.get(DUR_STAT) != null) {
                    dur += map.get(DUR_STAT);
                }

                Element currentElm = stats.getElement();
                if (map.get(ELM_ATK_STAT) != null) {
                    elementMap.put(currentElm, elementMap.getOrDefault(currentElm, 0.0F) + map.get(ELM_ATK_STAT));
                }
                if (stats.getMiningLevel().getTier() > minLv.getTier()) {
                    minLv = stats.getMiningLevel();
                }
            }

        }
        List<Element> elementList = new java.util.ArrayList<>(List.of(Element.EMBER, Element.GLACE, Element.SURGE, Element.PHANTOM, Element.TREMOR, Element.MAGIC, Element.FLOW, Element.HOLLOW));
        for (Element element1 : elementList) {
            if (elementMap.getOrDefault(element1, 0.0F) > elmAtk) {
                elm = element1;
                elmAtk = elementMap.getOrDefault(element1, 0.0F);
            } else if (elementMap.getOrDefault(element1, 0.0F) == elmAtk) {
                elm = Element.NONE;
                elmAtk = 0;
            }
        }
        for (Element element2 : elementList) {
            if (elm != element2) {
                elmAtk += elementMap.getOrDefault(element2, 0.0F) * 0.75;
            }
        }
        elementList.add(Element.NONE);
        for (Element element1 : elementList) {
            if (element1 != elm) {
                elmAtk += elementMap.getOrDefault(element1, 0.0F) * (element1 == Element.NONE ? 1F : 0.5F);
            }
        }

        return new ToolStats(atk, elmAtk, spd, min, def, rng, swp, dur, elm, minLv);
    }

    public static ToolStats mulWithoutElementCode(ToolStats stats, float value) {
        float atk = 0;
        float elmAtk = 0;
        float spd = 0;
        float min = 0;
        float def = 0;
        float rng = 0;
        float swp = 0;
        float dur = 0;
        Map<Element, Float> elementMap = new HashMap<>();
        Element elm = Element.NONE;
        MiningLevel minLv = MiningLevel.NONE;

        if (stats != null) {
            Map<String, Float> map = stats.getStats();

            if (map.get(ATK_STAT) != null) {
                atk = map.get(ATK_STAT) * value;
            }
            if (map.get(ELM_ATK_STAT) != null) {
                elmAtk = map.get(ELM_ATK_STAT) * value;
            }
            if (map.get(SPD_STAT) != null) {
                spd = map.get(SPD_STAT) * value;
            }
            if (map.get(MIN_STAT) != null) {
                min = map.get(MIN_STAT) * value;
            }
            if (map.get(DEF_STAT) != null) {
                def = map.get(DEF_STAT) * value;
            }
            if (map.get(RNG_STAT) != null) {
                rng = map.get(RNG_STAT) * value;
            }
            if (map.get(SWP_STAT) != null) {
                swp = map.get(SWP_STAT) * value;
            }
            if (map.get(DUR_STAT) != null) {
                dur = map.get(DUR_STAT) * value;
            }
            if (stats.getElement() != null) {
                elm = stats.getElement();
            }
            if (stats.getMiningLevel() != null) {
                minLv = stats.getMiningLevel();
            }
        }
        return new ToolStats(atk, elmAtk, spd, min, def, rng, swp, dur, elm, minLv);
    }

    public static ToolStats mulWithoutElementCode(ToolStats stats, ToolStats value) {
        float atk = 0;
        float elmAtk = 0;
        float spd = 0;
        float min = 0;
        float def = 0;
        float rng = 0;
        float swp = 0;
        float dur = 0;
        Map<Element, Float> elementMap = new HashMap<>();
        Element elm = Element.NONE;
        MiningLevel minLv = MiningLevel.NONE;

        if (stats != null && value != null) {
            Map<String, Float> map = stats.getStats();
            Map<String, Float> map2 = value.getStats();

            if (map.get(ATK_STAT) != null && map2.get(ATK_STAT) != null) {
                atk = map.get(ATK_STAT) * map2.get(ATK_STAT);
            }
            if (map.get(ELM_ATK_STAT) != null && map2.get(ELM_ATK_STAT) != null) {
                elmAtk = map.get(ELM_ATK_STAT) * map2.get(ELM_ATK_STAT);
            }
            if (map.get(SPD_STAT) != null && map2.get(SPD_STAT) != null) {
                spd = map.get(SPD_STAT) * map2.get(SPD_STAT);
            }
            if (map.get(MIN_STAT) != null && map2.get(MIN_STAT) != null) {
                min = map.get(MIN_STAT) * map2.get(MIN_STAT);
            }
            if (map.get(DEF_STAT) != null && map2.get(DEF_STAT) != null) {
                def = map.get(DEF_STAT) * map2.get(DEF_STAT);
            }
            if (map.get(RNG_STAT) != null && map2.get(RNG_STAT) != null) {
                rng = map.get(RNG_STAT) * map2.get(RNG_STAT);
            }
            if (map.get(SWP_STAT) != null && map2.get(SWP_STAT) != null) {
                swp = map.get(SWP_STAT) * map2.get(SWP_STAT);
            }
            if (map.get(DUR_STAT) != null && map2.get(DUR_STAT) != null) {
                dur = map.get(DUR_STAT) * map2.get(DUR_STAT);
            }
            if (stats.getElement() != null) {
                elm = stats.getElement();
            }
            if (stats.getMiningLevel() != null) {
                minLv = stats.getMiningLevel();
            }
        }
        return new ToolStats(atk, elmAtk, spd, min, def, rng, swp, dur, elm, minLv);
    }

    public Map<String, Float> getStats() {
        return this.stats;
    }

    public Element getElement() {
        return element;
    }

    public MiningLevel getMiningLevel() {
        return miningLevel;
    }
}
