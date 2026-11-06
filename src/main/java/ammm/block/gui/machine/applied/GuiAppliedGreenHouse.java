package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedGreenHouse;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAppliedGreenHouse
        extends GuiMekanismTile<AppliedGreenHouse, MekanismTileContainer<AppliedGreenHouse>> {

    public GuiAppliedGreenHouse(MekanismTileContainer<AppliedGreenHouse> container, Inventory inv,
                                   Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 7, 16, tile::getMeStorage, tile::getInputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 25, 16, tile::getMeStorage, tile::getInputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 43, 16, tile::getMeStorage, tile::getInputKeyC));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 0 * 18, 16 + 0 * 18, tile::getMeStorage, tile::getOutputKey0));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 1 * 18, 16 + 0 * 18, tile::getMeStorage, tile::getOutputKey1));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 2 * 18, 16 + 0 * 18, tile::getMeStorage, tile::getOutputKey2));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 3 * 18, 16 + 0 * 18, tile::getMeStorage, tile::getOutputKey3));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 0 * 18, 16 + 1 * 18, tile::getMeStorage, tile::getOutputKey4));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 1 * 18, 16 + 1 * 18, tile::getMeStorage, tile::getOutputKey5));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 2 * 18, 16 + 1 * 18, tile::getMeStorage, tile::getOutputKey6));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 3 * 18, 16 + 1 * 18, tile::getMeStorage, tile::getOutputKey7));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 0 * 18, 16 + 2 * 18, tile::getMeStorage, tile::getOutputKey8));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 1 * 18, 16 + 2 * 18, tile::getMeStorage, tile::getOutputKey9));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 2 * 18, 16 + 2 * 18, tile::getMeStorage, tile::getOutputKey10));
        addRenderableWidget(new GuiMEKeySlot(this, 97 + 3 * 18, 16 + 2 * 18, tile::getMeStorage, tile::getOutputKey11));
        addRenderableWidget(new GuiMEKeySlot(this, 70, 16, tile::getMeStorage, () -> tile.feKey));
        addRenderableWidget(new GuiUpArrow(this, 30, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 66, 38))
                .jeiCategories(AMEJEIRecipeType.CROP_SOIL);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
