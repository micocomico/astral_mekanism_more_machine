package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedCrafter;
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

public class GuiAppliedCrafter extends GuiMekanismTile<AppliedCrafter, MekanismTileContainer<AppliedCrafter>> {

    public GuiAppliedCrafter(MekanismTileContainer<AppliedCrafter> container, Inventory inv,
                             Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        inventoryLabelX += 36;
        inventoryLabelY += 54;
        imageWidth += 72;
        imageHeight += 54;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 157, 16, tile::getMeStorage, tile::getInputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 193, 16, tile::getMeStorage, tile::getInputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 0 * 18, 34 + 0 * 18, tile::getMeStorage, tile::getInputKey0));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 1 * 18, 34 + 0 * 18, tile::getMeStorage, tile::getInputKey1));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 2 * 18, 34 + 0 * 18, tile::getMeStorage, tile::getInputKey2));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 3 * 18, 34 + 0 * 18, tile::getMeStorage, tile::getInputKey3));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 4 * 18, 34 + 0 * 18, tile::getMeStorage, tile::getInputKey4));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 0 * 18, 34 + 1 * 18, tile::getMeStorage, tile::getInputKey5));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 1 * 18, 34 + 1 * 18, tile::getMeStorage, tile::getInputKey6));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 2 * 18, 34 + 1 * 18, tile::getMeStorage, tile::getInputKey7));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 3 * 18, 34 + 1 * 18, tile::getMeStorage, tile::getInputKey8));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 4 * 18, 34 + 1 * 18, tile::getMeStorage, tile::getInputKey9));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 0 * 18, 34 + 2 * 18, tile::getMeStorage, tile::getInputKey10));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 1 * 18, 34 + 2 * 18, tile::getMeStorage, tile::getInputKey11));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 2 * 18, 34 + 2 * 18, tile::getMeStorage, tile::getInputKey12));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 3 * 18, 34 + 2 * 18, tile::getMeStorage, tile::getInputKey13));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 4 * 18, 34 + 2 * 18, tile::getMeStorage, tile::getInputKey14));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 0 * 18, 34 + 3 * 18, tile::getMeStorage, tile::getInputKey15));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 1 * 18, 34 + 3 * 18, tile::getMeStorage, tile::getInputKey16));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 2 * 18, 34 + 3 * 18, tile::getMeStorage, tile::getInputKey17));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 3 * 18, 34 + 3 * 18, tile::getMeStorage, tile::getInputKey18));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 4 * 18, 34 + 3 * 18, tile::getMeStorage, tile::getInputKey19));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 0 * 18, 34 + 4 * 18, tile::getMeStorage, tile::getInputKey20));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 1 * 18, 34 + 4 * 18, tile::getMeStorage, tile::getInputKey21));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 2 * 18, 34 + 4 * 18, tile::getMeStorage, tile::getInputKey22));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 3 * 18, 34 + 4 * 18, tile::getMeStorage, tile::getInputKey23));
        addRenderableWidget(new GuiMEKeySlot(this, 139 + 4 * 18, 34 + 4 * 18, tile::getMeStorage, tile::getInputKey24));
        addRenderableWidget(new GuiMEKeySlot(this, 115, 88, tile::getMeStorage, tile::getOutputKey));
        addRenderableWidget(new GuiMEKeySlot(this, 115, 34, tile::getMeStorage, () -> tile.feKey));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.UNINSTALLING, this, 116, 68))
                .jeiCategories(MekanismJEIRecipeType.VANILLA_CRAFTING);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
