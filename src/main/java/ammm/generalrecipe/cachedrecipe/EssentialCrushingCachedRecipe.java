package ammm.generalrecipe.cachedrecipe;

import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.recipes.output.ItemInfuseOutput;
import astral_mekanism.registries.AMEInfuseTypes;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.recipe.impl.CrushingIRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

public class EssentialCrushingCachedRecipe extends CachedRecipe<ItemStackToItemStackRecipe> {

    private final IInputHandler<ItemStack> inputHandler;
    private final IOutputHandler<ItemStack> outputHandler;
    private final ItemStackIngredient inputIngredient;
    @Nullable
    private ItemStack recipeInput;
    @Nullable
    private ItemStack recipeOutput;

    public EssentialCrushingCachedRecipe(ItemStackToItemStackRecipe recipe, BooleanSupplier recheckAllErrors,
                                         IInputHandler<ItemStack> inputHandler, IOutputHandler<ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        this.inputIngredient = creator.createMulti(
                recipe.getIngredients().stream()
                        .map(creator::from)
                        .toArray(ItemStackIngredient[]::new));
    }

    @Override
    public void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            ItemStack inputStack = inputHandler.getInput();
            if (inputStack.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                recipeInput = inputHandler.getRecipeInput(inputIngredient);
                recipeOutput = recipe.getResultItem(null);
                if (recipeInput.isEmpty() || recipeOutput.isEmpty()) {
                    tracker.mismatchedRecipe();
                    return;
                }
                inputHandler.calculateOperationsCanSupport(tracker, recipeInput);
                outputHandler.calculateOperationsCanSupport(tracker, recipeOutput);
            }

        }
    }

    @Override
    public boolean isInputValid() {
        return !inputHandler.getInput().isEmpty() && inputIngredient.test(inputHandler.getInput());
    }

    @Override
    public void finishProcessing(int operations) {
        if (recipeInput != null && !recipeInput.isEmpty() && recipeOutput != null) {
            inputHandler.use(recipeInput, operations);
            outputHandler.handleOutput(recipeOutput, operations);
        }
    }

    public static IOutputHandler<ItemStack> merge(IOutputHandler<ItemStack> itemOutputHandler,
            IOutputHandler<InfusionStack> infusionOutputHandler) {
        return new IOutputHandler<ItemStack>() {

            @Override
            public void handleOutput(ItemStack toOutput, int operations) {
                itemOutputHandler.handleOutput(toOutput, operations);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, ItemStack toOutput) {
                itemOutputHandler.calculateOperationsCanSupport(tracker, toOutput);
            }

        };
    }

}
