package ammm.recipes.cached;

import fixdol.mekanismelements.api.recipes.ChemicalDemolitionRecipe;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BooleanSupplier;

public class FormulizedChemicalDemolitionCachedRecipe extends CachedRecipe<ChemicalDemolitionRecipe> {
    private final IOutputHandler firstOutputHandler;
    private final IOutputHandler secondOutputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final ILongInputHandler<@NotNull GasStack> gasInputHandler;
    private int multiply;

    private ItemStack recipeItem = ItemStack.EMPTY;
    private GasStack recipeGas = GasStack.EMPTY;
    private ItemStack firstOutput = ItemStack.EMPTY;
    private ItemStack secondOutput = ItemStack.EMPTY;

    public FormulizedChemicalDemolitionCachedRecipe(ChemicalDemolitionRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler,
                                            ILongInputHandler<@NotNull GasStack> gasInputHandler, IOutputHandler firstOutputHandler, IOutputHandler secondOutputHandler, int multiply) {
        super(recipe, recheckAllErrors);
        this.itemInputHandler = Objects.requireNonNull(itemInputHandler, "Item input handler cannot be null.");
        this.gasInputHandler = Objects.requireNonNull(gasInputHandler, "Chemical input handler cannot be null.");
        this.multiply = multiply;
        this.firstOutputHandler = Objects.requireNonNull(firstOutputHandler, "First Input handler cannot be null.");
        this.secondOutputHandler = Objects.requireNonNull(secondOutputHandler, "Second Input handler cannot be null.");
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            recipeItem = itemInputHandler.getRecipeInput(recipe.getItemInput());
            if (recipeItem.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                recipeGas = gasInputHandler.getRecipeInput(IngredientCreatorAccess.gas()
                            .createMulti(recipe.getGasInput().getRepresentations().stream()
                                    .map(stack -> IngredientCreatorAccess.gas()
                                            .from(new GasStack(stack, stack.getAmount() * multiply)))
                                    .toArray(ChemicalStackIngredient.GasStackIngredient[]::new))
                );
                if (recipeGas.isEmpty()) {
                    tracker.updateOperations(0);
                    if (!tracker.shouldContinueChecking()) {
                        return;
                    }
                }
                itemInputHandler.calculateOperationsCanSupport(tracker, recipeItem);
                if (!recipeGas.isEmpty() && tracker.shouldContinueChecking()) {
                    gasInputHandler.calculateOperationsCanSupport(tracker, recipeGas);
                    if (tracker.shouldContinueChecking()) {
                        firstOutput = recipe.getFirstOutput(recipeItem, recipeGas);
                        secondOutput = recipe.getSecondOutput(recipeItem, recipeGas);
                        firstOutputHandler.calculateOperationsCanSupport(tracker, firstOutput);
                        secondOutputHandler.calculateOperationsCanSupport(tracker, secondOutput);
                    }
                }
            }
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack itemInput = itemInputHandler.getInput();
        if (!itemInput.isEmpty()) {
            GasStack gasStack = gasInputHandler.getInput();
            if (!gasStack.isEmpty() && recipe.test(itemInput, gasStack)) {
                GasStack recipeGas = gasInputHandler.getRecipeInput(recipe.getGasInput());
                return !recipeGas.isEmpty() && gasStack.getAmount() >= recipeGas.getAmount();
            }
        }
        return false;
    }

    @Override
    protected void useResources(int operations) {
        super.useResources(operations);
    }

    @Override
    protected void finishProcessing(int operations) {
        if (!firstOutput.isEmpty() && !secondOutput.isEmpty()) {
            itemInputHandler.use(recipeItem, operations);
            gasInputHandler.use(recipeGas, recipeGas.getAmount() * operations);
            firstOutputHandler.handleOutput(firstOutput, operations);
            secondOutputHandler.handleOutput(secondOutput, operations);
        }
    }
}

