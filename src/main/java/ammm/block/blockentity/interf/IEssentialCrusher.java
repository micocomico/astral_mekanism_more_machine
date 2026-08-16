package ammm.block.blockentity.interf;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedSingelRecipeLookupHandler;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.recipe.impl.CrushingIRecipe;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.interfaces.IHasGasMode;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

import java.util.List;

public interface IEssentialCrusher<BE extends TileEntityRecipeMachine<CrushingIRecipe> & IEssentialCrusher<BE>>
        extends
        ISingleRecipeLookupHandler<ItemStack, CrushingIRecipe, InputRecipeCache.SingleItem<CrushingIRecipe>> {

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
