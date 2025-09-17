package net.stln.magitech.compat.modonomicon;

import com.klikli_dev.modonomicon.client.render.page.PageRendererRegistry;
import com.klikli_dev.modonomicon.data.BookPageJsonLoader;
import com.klikli_dev.modonomicon.data.LoaderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public class PageInit {

    public static final ResourceLocation TOOL_ASSEMBLY_RECIPE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "tool_assembly_recipe");

    public static void registerPages() {
        LoaderRegistry.registerPageLoader(TOOL_ASSEMBLY_RECIPE, (BookPageJsonLoader<?>) BookToolAssemblyRecipePage::fromJson, BookToolAssemblyRecipePage::fromNetwork);
    }

    public static void registerRenderers() {
        PageRendererRegistry.registerPageRenderer(TOOL_ASSEMBLY_RECIPE, p -> new BookToolAssemblyRecipePageRenderer((BookToolAssemblyRecipePage) p));
    }

}
