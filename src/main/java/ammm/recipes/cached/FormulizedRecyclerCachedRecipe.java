package ammm.recipes.cached;

import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipeHelper;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

public class FormulizedRecyclerCachedRecipe extends CachedRecipe<RecyclerRecipe> {

    private final IInputHandler<ItemStack> inputHandler;
    private final IOutputHandler<ItemStack> outputHandler;
    private final int multiply;
    private final ItemStackIngredient inputIngredient;
    @Nullable
    private ItemStack recipeInput;
    @Nullable
    private ItemStack recipeOutput;

    public FormulizedRecyclerCachedRecipe(RecyclerRecipe recipe, BooleanSupplier recheckAllErrors,
                                          IInputHandler<ItemStack> inputHandler, IOutputHandler<ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        double chance = this.recipe.getOutputChance();
        this.multiply = chance > 0 ? (int) Math.ceil(1 / chance) : 1;
        this.inputIngredient = IngredientCreatorAccess.item()
                .createMulti(this.recipe.getInput().getRepresentations().stream()
                        .map(stack -> IngredientCreatorAccess.item()
                                .from(stack.copyWithCount(stack.getCount() * multiply)))
                        .toArray(ItemStackIngredient[]::new));
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ItemStack inputStack = inputHandler.getInput();
            if (inputStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                CachedRecipeHelper.oneInputCalculateOperationsThisTick(tracker,
                        inputHandler, () -> inputIngredient, i -> recipeInput = i,
                        outputHandler, stack -> recipe.getOutput(stack).getMaxChanceOutput(), o -> recipeOutput = o, ItemStack::isEmpty);
            }
        }
    }

    @Override
    public boolean isInputValid() {
        return !inputHandler.getInput().isEmpty() && recipe.test(inputHandler.getInput());
    }

    @Override
    protected void finishProcessing(int arg0) {
        if (recipeInput != null && !recipeInput.isEmpty() && recipeOutput != null) {
            inputHandler.use(recipeInput, arg0);
            outputHandler.handleOutput(recipeOutput, arg0);
        }
    }

}
