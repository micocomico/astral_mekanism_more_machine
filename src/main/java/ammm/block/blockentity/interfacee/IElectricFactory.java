package ammm.block.blockentity.interfacee;

import astral_mekanism.block.blockentity.base.IAMEFactory;
import astral_mekanism.block.blockentity.interf.IEnergizedMachine;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

public interface IElectricFactory<BE extends TileEntityMekanism & IAMEFactory<BE>>
        extends
        ISingleRecipeLookupHandler<ItemStack, ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>>,
        IEnergizedMachine, IAMEFactory<BE> {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            IEssentialElectricMachine.NOT_ENOUGH_ITEM_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    public static final Set<RecipeError> GLOBAL_ERROR_TYPES = Set.of(RecipeError.NOT_ENOUGH_ENERGY);

    public abstract double getProgressScaled(int index);
}
