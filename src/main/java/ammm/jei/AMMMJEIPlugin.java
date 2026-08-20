package ammm.jei;

import ammm.registries.AMMMachines;
import astral_mekanism.AMEConstants;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.api.providers.IItemProvider;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@JeiPlugin

public class AMMMJEIPlugin implements IModPlugin {
    private static IJeiRuntime runtime;
    private static IRecipesGui recipesGui;

    @Override
    public ResourceLocation getPluginUid() {
        return AMEConstants.rl("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ASTRAL_CRAFTING,AMMMachines.ASTRAL_CRAFTER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ASTRAL_CRAFTING,AMMMachines.ENCHANTED_CRAFTER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SMELTING,AMMMachines.ENCHANTED_ENERGIZED_SMELTING_FACTORIES.values().toArray(IItemProvider[]::new));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        recipesGui = runtime.getRecipesGui();
    }

    public static @Nullable IJeiRuntime getRuntime() {
        return runtime;
    }

    public static @Nullable IRecipesGui getRecipesGui() {
        return recipesGui;
    }
}
