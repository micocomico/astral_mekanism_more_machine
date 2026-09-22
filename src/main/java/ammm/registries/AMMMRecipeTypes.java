package ammm.registries;

import ammm.AMMMConstants;
import astral_mekanism.AMEConstants;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.FluidFluid;
import astral_mekanism.recipes.inputRecipeCache.AMInputRecipeCache.GasInfusion;
import astral_mekanism.recipes.inputRecipeCache.AstralCraftingRecipeCache;
import astral_mekanism.recipes.inputRecipeCache.MekanicalTransformRecipeCache;
import astral_mekanism.recipes.recipe.*;
import astral_mekanism.util.RecipeTypeUtils;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.ItemFluidChemical;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.registration.impl.RecipeTypeDeferredRegister;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

import java.util.function.Function;

public class AMMMRecipeTypes {

    public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(
            AMMMConstants.MODID);

    private static <MR extends MekanismRecipe, IIRC extends IInputRecipeCache> RecipeTypeRegistryObject<MR, IIRC> register(
            String name, Function<MekanismRecipeType<MR, IIRC>, IIRC> inputCacheCreator) {
        return RecipeTypeUtils.registerRecipeType(RECIPE_TYPES, name, AMMMConstants::rl, inputCacheCreator);
    }

    public static final RecipeTypeRegistryObject<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> TEST = register(
            "test", rt -> new SingleItem<>(rt, ItemStackToItemStackRecipe::getInput));
}