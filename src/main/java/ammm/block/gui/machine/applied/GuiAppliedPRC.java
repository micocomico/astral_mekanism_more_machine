package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedPRC;
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

public class GuiAppliedPRC extends GuiMekanismTile<AppliedPRC, MekanismTileContainer<AppliedPRC>> {

    public GuiAppliedPRC(MekanismTileContainer<AppliedPRC> container, Inventory inv,
                            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 43, 16, tile::getMeStorage, tile::getInputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 62, 16, tile::getMeStorage, tile::getInputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 81, 16, tile::getMeStorage, tile::getInputKeyC));
        addRenderableWidget(new GuiMEKeySlot(this, 133, 24, tile::getMeStorage, tile::getOutputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 133, 42, tile::getMeStorage, tile::getOutputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 22, 34, tile::getMeStorage, () -> tile.feKey));
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 106, 38))
                .jeiCategories(MekanismJEIRecipeType.REACTION);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
