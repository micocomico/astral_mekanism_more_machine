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
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING,AMMMachines.ENCHANTED_COMBINER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING,AMMMachines.ENCHANTED_ENERGIZED_SMELTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING,AMMMachines.ASTRAL_CRUSHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING,AMMMachines.ENCHANTED_CRUSHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING,AMMMachines.CRUSHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING,AMMMachines.ASTRAL_ENRICHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING,AMMMachines.ENCHANTED_ENRICHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING,AMMMachines.ENRICHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING,AMMMachines.ASTRAL_COMBINING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING,AMMMachines.ENCHANTED_COMBINING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING,AMMMachines.COMBINING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING,AMMMachines.ASTRAL_COMPRESSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING,AMMMachines.ENCHANTED_COMPRESSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING,AMMMachines.COMPRESSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING,AMMMachines.ASTRAL_INJECTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING,AMMMachines.ENCHANTED_INJECTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING,AMMMachines.INJECTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING,AMMMachines.ASTRAL_PURIFYING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING,AMMMachines.ENCHANTED_PURIFYING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING,AMMMachines.PURIFYING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING,AMMMachines.ASTRAL_INFUSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING,AMMMachines.ENCHANTED_INFUSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING,AMMMachines.INFUSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING,AMMMachines.ASTRAL_SAWING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING,AMMMachines.ENCHANTED_SAWING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING,AMMMachines.SAWING_FACTORIES.values().toArray(IItemProvider[]::new));
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
