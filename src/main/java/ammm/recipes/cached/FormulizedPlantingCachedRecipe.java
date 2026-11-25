package ammm.recipes.cached;

import astral_mekanism.recipes.output.DoubleItemOutput;
import com.jerry.mekmm.api.recipes.PlantingRecipe;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipeHelper;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BooleanSupplier;

public class FormulizedPlantingCachedRecipe extends CachedRecipe<PlantingRecipe> {

    private final IInputHandler<ItemStack> itemInputHandler;
    private final ILongInputHandler<GasStack> gasInputHandler;
    private final IOutputHandler<DoubleItemOutput> outputHandler;
    private final int multiply;
    private final ItemStackIngredient itemInputIngredient;
    private final ChemicalStackIngredient.GasStackIngredient gasInputIngredient;
    private ItemStack recipeItem;
    private GasStack recipeGas;
    private DoubleItemOutput recipeOutput;

    public FormulizedPlantingCachedRecipe(PlantingRecipe recipe, BooleanSupplier recheckAllErrors,
                                          IInputHandler<ItemStack> itemInputHandler, ILongInputHandler<GasStack> gasInputHandler, IOutputHandler<DoubleItemOutput> outputHandler) {
        super(recipe, recheckAllErrors);
        this.itemInputHandler = itemInputHandler;
        this.gasInputHandler = gasInputHandler;
        this.outputHandler = outputHandler;
        double chance = this.recipe.getSecondaryChance();
        this.multiply = chance > 0 ? (int) Math.ceil(1 / this.recipe.getSecondaryChance()) : 1;
        this.itemInputIngredient = IngredientCreatorAccess.item()
                .createMulti(this.recipe.getItemInput().getRepresentations().stream()
                        .map(itemStack -> IngredientCreatorAccess.item()
                                .from(itemStack.copyWithCount(itemStack.getCount() * multiply)))
                        .toArray(ItemStackIngredient[]::new));
        this.gasInputIngredient = IngredientCreatorAccess.gas()
                .createMulti(this.recipe.getGasInput().getRepresentations().stream()
                        .map(gasStack -> IngredientCreatorAccess.gas()
                                .from(new GasStack(gasStack,gasStack.getAmount() * multiply)))
                        .toArray(ChemicalStackIngredient.GasStackIngredient[]::new));
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ItemStack itemInputStack = itemInputHandler.getInput();
            if (itemInputStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                GasStack gasInputStack = gasInputHandler.getInput();
                if (gasInputStack.isEmpty()) {
                    tracker.mismatchedRecipe();
                } else {
                    List<ItemStack> extraOutputDef = recipe.getSecondaryOutputDefinition();
                    CachedRecipeHelper.twoInputCalculateOperationsThisTick(tracker,
                            itemInputHandler, () -> itemInputIngredient, gasInputHandler, () -> gasInputIngredient, (item, gas) -> {
                                this.recipeItem = item;
                                this.recipeGas = gas;
                            },
                            outputHandler, (item,gas) -> {
                                ItemStack main = recipe.getOutput(item,gas).getMainOutput();
                                return new DoubleItemOutput(main.copyWithCount(main.getCount() * multiply),
                                        extraOutputDef.isEmpty() ? ItemStack.EMPTY : extraOutputDef.get(0));
                            }, o -> recipeOutput = o, ItemStack::isEmpty, GasStack::isEmpty);
                }
            }
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack itemInput = this.itemInputHandler.getInput();
        if (!itemInput.isEmpty()) {
            GasStack gasStack = this.gasInputHandler.getInput();
            if (!gasStack.isEmpty() && this.recipe.test(itemInput, gasStack)) {
                GasStack recipeGas = this.gasInputHandler.getRecipeInput(this.recipe.getGasInput());
                return !recipeGas.isEmpty() && gasStack.getAmount() >= recipeGas.getAmount();
            }
        }

        return false;
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (recipeItem != null && recipeGas != null && !recipeItem.isEmpty() && !recipeGas.isEmpty() && recipeOutput != null) {
            gasInputHandler.use(recipeGas, recipeGas.getAmount() * arg0);
            outputHandler.handleOutput(recipeOutput, arg0);
        }
    }

}
