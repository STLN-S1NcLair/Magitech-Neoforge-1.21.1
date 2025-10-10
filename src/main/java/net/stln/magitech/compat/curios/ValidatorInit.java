package net.stln.magitech.compat.curios;

import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemTagKeys;
import top.theillusivec4.curios.api.CuriosApi;

public class ValidatorInit {

    public static void registerValidators() {
        CuriosApi.registerCurioPredicate(Magitech.id("threadbound"), (slotResult -> slotResult.stack().getTags().anyMatch(itemTagKey -> itemTagKey.equals(ItemTagKeys.THREAD_BOUND))));
    }
}
