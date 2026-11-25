package ammm.block.gui.machine;

import ammm.block.blockentity.machine.basemachine.BMCDM;
import fixdol.mekanismelements.client.MSJEIRecipeType;
import fixdol.mekanismelements.common.tile.machine.TileEntityChemicalDemolitionMachine;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory
                .warning.WarningTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

public class GMCDM<BE extends BMCDM> extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GMCDM(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
        this.titleLabelY = 4;
    }

    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(new GuiVerticalPowerBar(this, this.tile.getEnergyContainer(), 164, 5))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY_REDUCED_RATE, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE));
        this.addRenderableWidget(new GuiEnergyTab(this, this.tile.getEnergyContainer(), this.tile::getActive));
        this.addRenderableWidget(new GuiGasGauge(() -> this.tile.injectTank, () -> this.tile.getGasTanks(null), GaugeType.STANDARD, this, 7, 4));
        this.addRenderableWidget(new GuiSlot(SlotType.OUTPUT_WIDE, this, 111, 30))
                .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE))
                .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, this.tile.getWarningCheck(TileEntityChemicalDemolitionMachine.NOT_ENOUGH_SPACE_SECOND_OUTPUT_ERROR));
        GuiProgress progress = this.addRenderableWidget((new GuiProgress(this.tile::getActive, ProgressType.LARGE_RIGHT, this, 54, 40)))
                .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        if (!ModList.get().isLoaded("emi")) progress.jeiCategories(MSJEIRecipeType.CHEMICAL_DEMOLITION_MACHINE);
    }

    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.renderTitleText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
