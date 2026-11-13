package ammm.jei;

import ammm.AMEMMConstants;
import ammm.registries.AMEMMFactories;
import ammm.registries.AMEMMMachines;
import appeng.integration.modules.jei.ChargerCategory;
import appeng.integration.modules.jei.TransformCategory;
import astral_mekanism.jei.AMEJEIRecipeType;
import fixdol.mekanismelements.client.MSJEIRecipeType;
import mekanism.api.providers.IItemProvider;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

@JeiPlugin

public class AMEMMJEIPlugin implements IModPlugin {
    private static IJeiRuntime runtime;
    private static IRecipesGui recipesGui;

    @Override
    public ResourceLocation getPluginUid() {
        return AMEMMConstants.rl("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (!ModList.get().isLoaded("emi")) {
            CatalystRegistryHelper.register(registry, MSJEIRecipeType.ADSORPTION_SEPARATOR, AMEMMMachines.ASTRAL_ADSORPTION_SEPARATOR);
        }
        CatalystRegistryHelper.register(registry,MekanismJEIRecipeType.PAINTING, AMEMMMachines.ASTRAL_PAINTING_MACHINE);
        CatalystRegistryHelper.register(registry,AMEJEIRecipeType.MEKANICAL_COMPOSTER, AMEMMMachines.ENCHANTED_COMPOSTER);
        registry.addRecipeCatalysts(RecipeTypes.COMPOSTING, AMEMMMachines.ENCHANTED_COMPOSTER);
        CatalystRegistryHelper.register(registry,MekanismJEIRecipeType.ACTIVATING, AMEMMMachines.ENCHANTED_GNA);
        CatalystRegistryHelper.register(registry,AMEJEIRecipeType.FLUID_INFUSER_RECIPE, AMEMMMachines.ENCHANTED_FLUID_INFUSER);
        registry.addRecipeCatalysts(ChargerCategory.RECIPE_TYPE, AMEMMMachines.ENCHANTED_MEKANICAL_CHARGER);
        CatalystRegistryHelper.register(registry,AMEJEIRecipeType.AAE_REACTION, AMEMMMachines.ENCHANTED_REACTION_CHAMBER);
        registry.addRecipeCatalysts(TransformCategory.RECIPE_TYPE, AMEMMMachines.ENCHANTED_TRANSFORMER);
        CatalystRegistryHelper.register(registry,AMEJEIRecipeType.MEKANICAL_TRANSFORM, AMEMMMachines.ENCHANTED_TRANSFORMER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.TRANSFORM, AMEMMMachines.ENCHANTED_TRANSFORMER);

        registry.addRecipeCatalysts(TransformCategory.RECIPE_TYPE, AMEMMMachines.ENCHANTED_TRANSFORMER);
        CatalystRegistryHelper.register(registry,AMEJEIRecipeType.ASTRAL_CRAFTING, AMEMMMachines.ASTRAL_ESSENTIAL_CRAFTER, AMEMMMachines.ENCHANTED_ESSENTIAL_CRAFTER);

        registry.addRecipeCatalysts(RecipeTypes.SMELTING, AMEMMMachines.ENCHANTED_ENERGIZED_SMELTING_FACTORIES.values().toArray(ItemLike[]::new));
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING, AMEMMMachines.ENCHANTED_ENERGIZED_SMELTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING, AMEMMFactories.ASTRAL_CRUSHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING, AMEMMFactories.ENCHANTED_CRUSHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING, AMEMMFactories.CRUSHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING, AMEMMFactories.ASTRAL_ENRICHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING, AMEMMFactories.ENCHANTED_ENRICHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING, AMEMMFactories.ENRICHING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING, AMEMMFactories.ASTRAL_COMBINING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING, AMEMMFactories.ENCHANTED_COMBINING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING, AMEMMFactories.COMBINING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING, AMEMMFactories.ASTRAL_COMPRESSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING, AMEMMFactories.ENCHANTED_COMPRESSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING, AMEMMFactories.COMPRESSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING, AMEMMFactories.ASTRAL_INJECTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING, AMEMMFactories.ENCHANTED_INJECTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING, AMEMMFactories.INJECTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING, AMEMMFactories.ASTRAL_PURIFYING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING, AMEMMFactories.ENCHANTED_PURIFYING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING, AMEMMFactories.PURIFYING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING, AMEMMFactories.ASTRAL_INFUSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING, AMEMMFactories.ENCHANTED_INFUSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING, AMEMMFactories.INFUSING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING, AMEMMFactories.ASTRAL_SAWING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING, AMEMMFactories.ENCHANTED_SAWING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING, AMEMMFactories.SAWING_FACTORIES.values().toArray(IItemProvider[]::new));
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
