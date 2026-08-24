package ammm.block.blockentity.interfacee;

import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IEssentialElectricMachine<BE extends TileEntityRecipeMachine<ItemStackToItemStackRecipe> & IEssentialElectricMachine<BE>>
        extends
        ISingleRecipeLookupHandler<ItemStack, ItemStackToItemStackRecipe, InputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> {

    public static final RecipeError NOT_ENOUGH_ITEM_OUTPUT_SPACE = RecipeError.create();

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            NOT_ENOUGH_ITEM_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    public MachineEnergyContainer<BE> getEnergyContainer();

    public abstract double getProgressScaled();

    public FloatingLong getEnergyUsage();
}
