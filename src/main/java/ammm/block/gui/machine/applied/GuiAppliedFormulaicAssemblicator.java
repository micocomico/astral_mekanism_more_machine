package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedFormulaicAssemblicator;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAppliedFormulaicAssemblicator extends GuiMekanismTile<AppliedFormulaicAssemblicator, MekanismTileContainer<AppliedFormulaicAssemblicator>> {

    public GuiAppliedFormulaicAssemblicator(MekanismTileContainer<AppliedFormulaicAssemblicator> container, Inventory inv,
                                            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 0 * 18, 16 + 0 * 18, tile::getMeStorage, tile::getInputKey0));
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 1 * 18, 16 + 0 * 18, tile::getMeStorage, tile::getInputKey1));
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 2 * 18, 16 + 0 * 18, tile::getMeStorage, tile::getInputKey2));
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 0 * 18, 16 + 1 * 18, tile::getMeStorage, tile::getInputKey3));
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 1 * 18, 16 + 1 * 18, tile::getMeStorage, tile::getInputKey4));
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 2 * 18, 16 + 1 * 18, tile::getMeStorage, tile::getInputKey5));
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 0 * 18, 16 + 2 * 18, tile::getMeStorage, tile::getInputKey6));
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 1 * 18, 16 + 2 * 18, tile::getMeStorage, tile::getInputKey7));
        addRenderableWidget(new GuiMEKeySlot(this, 103 + 2 * 18, 16 + 2 * 18, tile::getMeStorage, tile::getInputKey8));
        addRenderableWidget(new GuiMEKeySlot(this, 79, 52, tile::getMeStorage, tile::getOutputKey));
        addRenderableWidget(new GuiMEKeySlot(this, 79, 16, tile::getMeStorage, () -> tile.feKey));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.UNINSTALLING, this, 80, 41))
                .jeiCategories(MekanismJEIRecipeType.VANILLA_CRAFTING);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
