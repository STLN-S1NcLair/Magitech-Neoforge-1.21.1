package net.stln.magitech.compat.curios;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemTagKeys;
import top.theillusivec4.curios.api.CuriosApi;

public class ValidatorInit {

    public static void registerValidators() {
        CuriosApi.registerCurioPredicate(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), (slotResult -> {
            return slotResult.stack().getTags().anyMatch(itemTagKey -> itemTagKey.equals(ItemTagKeys.THREAD_BOUND));
        }));
    }
}
