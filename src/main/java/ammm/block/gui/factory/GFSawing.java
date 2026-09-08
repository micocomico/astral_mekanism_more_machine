package ammm.block.gui.factory;

import ammm.block.blockentity.basefactory.BFElectric;
import ammm.block.blockentity.basefactory.BFSawing;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.gui.element.PagedGuiProgress;
import astral_mekanism.block.gui.factory.GuiAstralMekanismFactory;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GFSawing<BE extends BFSawing<BE>> extends GuiAstralMekanismFactory<BE> {

    public GFSawing(ContainerAstralMekanismFactory<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        imageHeight += 21;
        inventoryLabelY = 95;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getEnergyUsage));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY, 0)));
        for (int index = 0; index < tile.tier.processes; index++) {
            int page = tile.getPageByIndex(index);
            int x = tile.getXByIndex(index) + 4;
            int y = tile.getY() + 20;
            int cacheIndex = index;
            addRenderableWidget(
                    new PagedGuiProgress(() -> tile.getProgressScaled(cacheIndex), ProgressType.DOWN, this, x, y, page))
                    .jeiCategories(MekanismJEIRecipeType.SAWING);
        }
    }
}
