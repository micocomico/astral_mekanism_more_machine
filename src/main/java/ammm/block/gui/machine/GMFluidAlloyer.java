package ammm.block.gui.machine;

import ammm.block.blockentity.machine.basemachine.BMFluidAlloyer;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityFluidAlloyer;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeInfo;
import mekanism.client.gui.element.gauge.GaugeOverlay;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory

                .warning.WarningTracker;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GMFluidAlloyer<BE extends BMFluidAlloyer> extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GMFluidAlloyer(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.imageHeight += 32;
        this.inventoryLabelY = this.imageHeight - 94;
        this.dynamicSlots = true;
    }

    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(new GuiVerticalPowerBar(this, this.tile.getEnergyContainer(), 164, 15))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY));
        this.addRenderableWidget(new GuiEnergyTab(this, this.tile.getEnergyContainer(), this.tile::getActive));
        this.addRenderableWidget(new GuiFluidGauge(() -> this.tile.inputTank, () -> this.tile.getFluidTanks(null), GaugeType.STANDARD.with(DataType.INPUT), this, 7, 16))
                .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT));
        this.addRenderableWidget(new GuiFluidGauge(() -> this.tile.extraTank, () -> this.tile.getFluidTanks(null), GaugeType.get(GaugeInfo.YELLOW, GaugeOverlay.STANDARD), this, 29, 16))
                .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT));
        this.addRenderableWidget(new GuiFluidGauge(() -> this.tile.tertiaryTank, () -> this.tile.getFluidTanks(null), GaugeType.STANDARD.with(DataType.INPUT_2), this, 51, 16))
                .warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.tile.getWarningCheck(TileEntityFluidAlloyer.NOT_ENOUGH_TERTIARY_INPUT));
        this.addRenderableWidget(new GuiFluidGauge(() -> this.tile.outputTank, () -> this.tile.getFluidTanks(null), GaugeType.STANDARD.with(DataType.OUTPUT), this, 131, 16))
                .warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        this.addRenderableWidget((new GuiProgress(this.tile::getActive, ProgressType.LARGE_RIGHT, this, 76, 42)).jeiCategories(EMJEI.FLUID_ALLOYING))
                .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
    }

    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.renderTitleText(guiGraphics);
        this.drawString(guiGraphics, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, this.titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
