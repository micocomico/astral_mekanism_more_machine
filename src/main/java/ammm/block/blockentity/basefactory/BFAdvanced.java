package ammm.block.blockentity.basefactory;

import ammm.block.blockentity.astralfactory.AFCompressing;
import ammm.block.blockentity.base.MekanismProgressFactory;
import ammm.block.blockentity.base.MekanismRecipeFactory;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.interfaces.IHasDumpButton;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;

public abstract class BFAdvanced<RECIPE extends MekanismRecipe, BE extends MekanismRecipeFactory<RECIPE, BE, INPUT_CACHE>,INPUT_CACHE extends IInputRecipeCache> extends MekanismRecipeFactory<RECIPE, BE, INPUT_CACHE>
        implements IHasDumpButton {

    protected BFAdvanced(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, Set<CachedRecipe.OperationTracker.RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
    }

    IGasTank gasTank;
    public IGasTank getGasTank() {return gasTank;}
}
