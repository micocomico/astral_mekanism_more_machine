package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedTransformer;
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

public class GuiAppliedTransformer extends GuiMekanismTile<AppliedTransformer, MekanismTileContainer<AppliedTransformer>> {

    public GuiAppliedTransformer(MekanismTileContainer<AppliedTransformer> container, Inventory inv,
                                 Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 29, 16, tile::getMeStorage, tile::getInputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 47, 16, tile::getMeStorage, tile::getInputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 65, 16, tile::getMeStorage, tile::getInputKeyC));
        addRenderableWidget(new GuiMEKeySlot(this, 83, 16, tile::getMeStorage, tile::getInputKeyD));
        addRenderableWidget(new GuiMEKeySlot(this, 101, 16, tile::getMeStorage, tile::getInputKeyE));
        addRenderableWidget(new GuiMEKeySlot(this, 153, 24, tile::getMeStorage, tile::getOutputKey));
        addRenderableWidget(new GuiMEKeySlot(this, 153, 42, tile::getMeStorage, tile::getOutputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 5, 34, tile::getMeStorage, () -> tile.feKey));
        addRenderableWidget(new GuiUpArrow(this, 70, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 124, 38))
                .jeiCategories(AMEJEIRecipeType.TRANSFORM,AMEJEIRecipeType.MEKANICAL_TRANSFORM);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
