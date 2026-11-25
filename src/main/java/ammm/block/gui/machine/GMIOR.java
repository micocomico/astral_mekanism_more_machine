package ammm.block.gui.machine;

import ammm.block.blockentity.machine.basemachine.BMIOR;
import fixdol.mekanismelements.client.MSJEIRecipeType;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

public class GMIOR<BE extends BMIOR> extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GMIOR(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
        this.titleLabelY = 4;
    }

    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(new GuiGasGauge(() -> this.tile.chemicalInputTank, () -> this.tile.getGasTanks(null), GaugeType.STANDARD, this, 42, 13))
                .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT));
        this.addRenderableWidget(new GuiVerticalPowerBar(this, this.tile.getEnergyContainer(), 164, 15))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY_REDUCED_RATE, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE));
        this.addRenderableWidget(new GuiEnergyTab(this, this.tile.getEnergyContainer(), this.tile::getActive));
        GuiProgress progress = this.addRenderableWidget((new GuiProgress(this.tile::getActive, ProgressType.LARGE_RIGHT, this, 64, 40))
                .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT)));
        if (!ModList.get().isLoaded("emi")) progress.jeiCategories(MSJEIRecipeType.INFINITY_ORE_REPROCESSING);
    }

    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.renderTitleText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
