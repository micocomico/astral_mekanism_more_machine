package ammm.block.gui.machine.applied;

import ame_the_valine.jei.AMETheValineJEIPlugin;
import ammm.block.blockentity.machine.appliedmachine.AppliedValineReactor;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAppliedValineReactor extends GuiMekanismTile<AppliedValineReactor, MekanismTileContainer<AppliedValineReactor>> {

    public GuiAppliedValineReactor(MekanismTileContainer<AppliedValineReactor> container, Inventory inv,
                                      Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 54, 16, tile::getMeStorage, tile::getInputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 72, 16, tile::getMeStorage, tile::getInputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 124, 16, tile::getMeStorage, tile::getOutputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 124, 34, tile::getMeStorage, tile::getOutputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 124, 52, tile::getMeStorage, () -> tile.feKey));
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 95, 38))
                .jeiCategories(AMETheValineJEIPlugin.VALINE_REACTOR_JEI_TYPE);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
