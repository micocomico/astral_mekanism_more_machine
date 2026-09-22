package ammm.block.gui.machine.applied;

import ammm.block.blockentity.appliedmachine.AppliedPrecisionSawmill;
import ammm.block.blockentity.interf.applied.IAppliedDoubleToSingleMachine;
import ammm.block.blockentity.interf.applied.IAppliedSingleToDoubleMachine;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAppliedPrecisionSawmill extends GuiMekanismTile<AppliedPrecisionSawmill, MekanismTileContainer<AppliedPrecisionSawmill>> {

    public GuiAppliedPrecisionSawmill(MekanismTileContainer<AppliedPrecisionSawmill> container, Inventory inv,
                                         Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 63, 16, tile::getMeStorage, tile::getInputKey));
        addRenderableWidget(new GuiMEKeySlot(this, 115, 24, tile::getMeStorage, tile::getOutputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 115, 43, tile::getMeStorage, tile::getOutputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 38, 34, tile::getMeStorage, () -> tile.feKey));
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 86, 38))
                .jeiCategories(MekanismJEIRecipeType.SAWING);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
