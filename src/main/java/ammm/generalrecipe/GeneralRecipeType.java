package ammm.generalrecipe;

import appeng.recipes.AERecipeTypes;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.AstralMekanism;
import astral_mekanism.generalrecipe.AMEFakeRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.lookup.cache.recipe.AAEReactionRecipeCache;
import astral_mekanism.generalrecipe.lookup.cache.recipe.CropSoilInputRecipeCache;
import astral_mekanism.generalrecipe.lookup.cache.recipe.InscriberRecipeInputRecipeCache;
import ammm.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.cache.recipe.TransformRecipeInputRecipeCache;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import mekanism.api.inventory.IgnoredIInventory;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.client.MekanismClient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.impl.CrushingIRecipe;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GeneralRecipeType<C extends Container, RECIPE extends Recipe<C>, INPUT_CACHE extends IInputRecipeCache>
        implements IUnifiedRecipeType<RECIPE, INPUT_CACHE>, IUnifiedRecipeTypeProvider<RECIPE, INPUT_CACHE> {

    private List<RECIPE> cachedRecipes = Collections.emptyList();
    private final RecipeType<RECIPE> recipeType;
    private final ResourceLocation registryName;
    private final INPUT_CACHE inputCache;

    public GeneralRecipeType(RecipeType<RECIPE> recipeType,
                             Function<GeneralRecipeType<C, RECIPE, INPUT_CACHE>, INPUT_CACHE> function) {
        this.recipeType = recipeType;
        this.registryName = new ResourceLocation(recipeType.toString());
        this.inputCache = function.apply(this);
    }

    public GeneralRecipeType(RecipeType<RECIPE> recipeType, ResourceLocation registryName,
                             Function<GeneralRecipeType<C, RECIPE, INPUT_CACHE>, INPUT_CACHE> function) {
        this.recipeType = recipeType;
        this.registryName = registryName;
        this.inputCache = function.apply(this);
    }

    @Override
    public String toString() {
        return registryName.toString();
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public GeneralRecipeType<C, RECIPE, INPUT_CACHE> getUnifiedRecipeType() {
        return this;
    }

    private void clearCaches() {
        cachedRecipes = Collections.emptyList();
        inputCache.clear();
    }

    @Override
    public INPUT_CACHE getInputCache() {
        return inputCache;
    }

    @NotNull
    @Override
    public List<RECIPE> getRecipes(@Nullable Level world) {
        if (world == null) {
            if (FMLEnvironment.dist.isClient()) {
                world = MekanismClient.tryGetClientWorld();
            } else {
                world = ServerLifecycleHooks.getCurrentServer().overworld();
            }
            if (world == null) {
                return Collections.emptyList();
            }
        }
        if (cachedRecipes.isEmpty()) {
            List<RECIPE> recipes = getRecipesUncached(world.getRecipeManager(), world.registryAccess());
            cachedRecipes = recipes.stream()
                    .filter(this::isNotRecipeIncomplete)
                    .toList();
        }
        return cachedRecipes;
    }

    protected @NotNull List<RECIPE> getRecipesUncached(RecipeManager recipeManager, RegistryAccess registryAccess) {
        List<RECIPE> recipes = recipeManager.getAllRecipesFor(recipeType);
        return recipes;
    }

    private boolean checkMyIncompleteRecipes(RecipeManager recipeManager, RegistryAccess registryAccess) {
        boolean incomplete = false;
        for (RECIPE recipe : getRecipesUncached(recipeManager, registryAccess)) {
            if (!recipe.isIncomplete()) {
                continue;
            }
            AstralMekanism.LOGGER.error("Incomplete recipe detected: {}", recipe.getId());
            incomplete = true;
        }
        return incomplete;
    }

    protected boolean isNotRecipeIncomplete(RECIPE recipe) {
        return !recipe.isIncomplete();
    }

    private static class BaseIsFake<C extends Container, RECIPE extends Recipe<C>, INPUT_CACHE extends IInputRecipeCache>
            extends GeneralRecipeType<C, RECIPE, INPUT_CACHE> {

        private final BiFunction<RecipeManager, RegistryAccess, List<RECIPE>> uncachedRecipeGetter;

        public BaseIsFake(RecipeType<RECIPE> recipeType,
                Function<GeneralRecipeType<C, RECIPE, INPUT_CACHE>, INPUT_CACHE> function,
                BiFunction<RecipeManager, RegistryAccess, List<RECIPE>> uncachedRecipeGetter) {
            super(recipeType, function);
            this.uncachedRecipeGetter = uncachedRecipeGetter;
        }

        @Override
        protected @NotNull List<RECIPE> getRecipesUncached(RecipeManager manager, RegistryAccess access) {
            return uncachedRecipeGetter.apply(manager, access);
        }

    }

    public static final GeneralRecipeType<IgnoredIInventory, CrushingIRecipe, GeneralSingleItem<IgnoredIInventory, CrushingIRecipe>> CRUSHING = new GeneralRecipeType<>(
            MekanismRecipeType.CRUSHING,
            type -> new GeneralSingleItem<>(type,
                    recipe -> IngredientCreatorAccess.item().createMulti(recipe.getIngredients().stream()
                            .map(IngredientCreatorAccess.item()::from)
                            .toArray(ItemStackIngredient[]::new)),
                    (stack, recipe) -> recipe.getIngredients().stream()
                            .anyMatch(ingredient -> ingredient.test(stack))));
}
