package ammm.block.blockentity.interf;

import astral_mekanism.block.blockentity.base.IAMEFactory;
import astral_mekanism.block.blockentity.interf.IEnergizedMachine;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedSingelRecipeLookupHandler;
import mekanism.api.Action;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IHasGasMode;
import mekanism.common.tile.interfaces.ISustainedData;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

import java.util.List;
import java.util.Set;

public interface IEnergizedSmeltingFactory<BE extends TileEntityMekanism & IAMEFactory<BE>>
        extends
        IUnifiedSingelRecipeLookupHandler<ItemStack, SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>>,
        IEnergizedMachine, IHasGasMode, ISustainedData, IAMEFactory<BE> {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter.NOT_ENOUGH_ITEM_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    public static final Set<RecipeError> GLOBAL_ERROR_TYPES = Set.of(RecipeError.NOT_ENOUGH_ENERGY,
            IEssentialEnergizedSmelter.NOT_ENOUGH_INFUSE_OUTPUT_SPACE);

    public IInfusionTank getInfusionTank();

    public default void handleTank() {
        IInfusionTank infusionTank = getInfusionTank();
        GasMode gasMode = getGasMode();
        if (gasMode == GasMode.DUMPING) {
            if (infusionTank.getStored() > infusionTank.getCapacity() / 5 * 4) {
                infusionTank.shrinkStack(infusionTank.getCapacity() / 5 * 4, Action.EXECUTE);
            }
            infusionTank.shrinkStack(infusionTank.getCapacity() / 100, Action.EXECUTE);
        } else if (gasMode == GasMode.DUMPING_EXCESS) {
            if (infusionTank.getStored() > infusionTank.getCapacity() / 5 * 4) {
                infusionTank.shrinkStack(infusionTank.getCapacity() / 5 * 4, Action.EXECUTE);
            }
        }
    }

    public abstract GasMode getGasMode();

    public abstract double getProgressScaled(int index);
}
