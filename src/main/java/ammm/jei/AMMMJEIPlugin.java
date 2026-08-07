package ammm.jei;

import ammm.AMMMConstants;
import ammm.registries.AMMMachines;
import appeng.integration.modules.jei.ChargerCategory;
import appeng.integration.modules.jei.TransformCategory;
import appeng.recipes.AERecipeTypes;
import astral_mekanism.AMEConstants;
import astral_mekanism.block.container.prefab.ContainerAMEFormulaicAssemblicator.ContainerAstralFormulaicAssemblicator;
import astral_mekanism.block.container.prefab.ContainerAMEFormulaicAssemblicator.ContainerEnchantedFormulaicAssemblicator;
import astral_mekanism.block.container.prefab.ContainerAMEFormulaicAssemblicator.ContainerEssentialFormulaicAseemblicator;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import astral_mekanism.jei.AMEJEIRecipeType;
import astral_mekanism.jei.jeirecipe.GasBurningJEIRecipe;
import astral_mekanism.jei.jeirecipe.MekanicalComposterJEIRecipe;
import astral_mekanism.jei.jeirecipe.MixingReactorJEIrecipe;
import astral_mekanism.jei.recipeCategory.*;
import astral_mekanism.jei.transferHandler.AMEFormulaicAssemblicatorTransferHandler;
import astral_mekanism.registries.AMEMachines;
import astral_mekanism.registries.AMERecipeTypes;
import com.fxd927.mekanismelements.client.MSJEIRecipeType;
import com.jerry.generator_extras.common.ExtraGenLang;
import com.jerry.generator_extras.common.genregistries.ExtraGenBlocks;
import com.jerry.generator_extras.common.genregistries.ExtraGenItem;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import mekanism.api.providers.IItemProvider;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mekanism.client.jei.machine.GasToGasRecipeCategory;
import mekanism.client.jei.machine.ItemStackToItemStackRecipeCategory;
import mekanism.generators.client.jei.GeneratorsJEIRecipeType;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.generators.common.registries.GeneratorsItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

@JeiPlugin

public class AMMMJEIPlugin implements IModPlugin {
    private static IJeiRuntime runtime;
    private static IRecipesGui recipesGui;

    @Override
    public ResourceLocation getPluginUid() {
        return AMEConstants.rl("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {

        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ASTRAL_CRAFTING,
                AMMMachines.ASTRAL_CRAFTER);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        recipesGui = runtime.getRecipesGui();
    }

    public static @Nullable IJeiRuntime getRuntime() {
        return runtime;
    }

    public static @Nullable IRecipesGui getRecipesGui() {
        return recipesGui;
    }
}
