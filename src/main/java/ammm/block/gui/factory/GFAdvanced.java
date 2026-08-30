package ammm.block.gui.factory;

import ammm.block.blockentity.base.MekanismRecipeFactory;
import ammm.block.blockentity.basefactory.BFAdvanced;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.gui.element.PagedGuiProgress;
import astral_mekanism.block.gui.factory.GuiAstralMekanismFactory;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tier.FactoryTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GFAdvanced<BE extends BFAdvanced<ItemStackGasToItemStackRecipe, BE, InputRecipeCache.ItemChemical<Gas, GasStack,ItemStackGasToItemStackRecipe>>>
        extends GuiAstralMekanismFactory<BE> {

    public GFAdvanced(ContainerAstralMekanismFactory<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getEnergyUsage));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY, 0)));
        addRenderableWidget(new GuiChemicalBar<>(this, GuiChemicalBar.getProvider(tile.getGasTank(), tile.getGasTanks(null)),7, 76,138, 4, true))
                .warning(WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
        addRenderableWidget(new GuiDumpButton<>(this, tile,148, 76));
        for (int index = 0; index < tile.tier.processes; index++) {
            int page = tile.getPageByIndex(index);
            int x = tile.getXByIndex(index) + 4;
            int y = tile.getY() + 20;
            int cacheIndex = index;
            addRenderableWidget(
                    new PagedGuiProgress(() -> tile.getProgressScaled(cacheIndex), ProgressType.DOWN, this, x, y, page))
                    .jeiCategories(MekanismJEIRecipeType.COMPRESSING);
        }
    }
}
