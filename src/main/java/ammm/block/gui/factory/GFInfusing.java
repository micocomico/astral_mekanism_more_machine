package ammm.block.gui.factory;

import ammm.block.blockentity.basefactory.BFAdvanced;
import ammm.block.blockentity.basefactory.BFInfusing;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.gui.element.PagedGuiProgress;
import astral_mekanism.block.gui.factory.GuiAstralMekanismFactory;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GFInfusing<BE extends BFInfusing<BE>> extends GuiAstralMekanismFactory<BE> {

    public GFInfusing(ContainerAstralMekanismFactory<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        inventoryLabelY = 87;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getEnergyUsage));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY, 0)));
        addRenderableWidget(new GuiChemicalBar<>(this, GuiChemicalBar.getProvider(tile.getInfusionTank(), tile.getInfusionTanks(null)),36, 81,299, 4, true))
                .warning(WarningType.NO_MATCHING_RECIPE, tile.getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
        addRenderableWidget(new GuiDumpButton<>(this, tile,338, 81));
        for (int index = 0; index < tile.tier.processes; index++) {
            int page = tile.getPageByIndex(index);
            int x = tile.getXByIndex(index) + 4;
            int y = tile.getY() + 20;
            int cacheIndex = index;
            addRenderableWidget(
                    new PagedGuiProgress(() -> tile.getProgressScaled(cacheIndex), ProgressType.DOWN, this, x, y, page))
                    .jeiCategories(MekanismJEIRecipeType.METALLURGIC_INFUSING);
        }
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
