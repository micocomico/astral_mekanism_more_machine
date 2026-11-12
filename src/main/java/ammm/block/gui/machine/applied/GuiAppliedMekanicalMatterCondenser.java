package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedMekanicalMatterCondenser;
import appeng.api.config.CondenserOutput;
import appeng.core.AppEng;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.network.to_server.PacketGuiInteract;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAppliedMekanicalMatterCondenser extends GuiMekanismTile<AppliedMekanicalMatterCondenser, MekanismTileContainer<AppliedMekanicalMatterCondenser>> {

    public GuiAppliedMekanicalMatterCondenser(MekanismTileContainer<AppliedMekanicalMatterCondenser> container, Inventory inv,
                                                       Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 54, 16, tile::getMeStorage, tile::getInputKeyA));
        addRenderableWidget(new GuiMEKeySlot(this, 72, 16, tile::getMeStorage, tile::getInputKeyB));
        addRenderableWidget(new GuiMEKeySlot(this, 124, 34, tile::getMeStorage, tile::getOutputKey));
        addRenderableWidget(new CondenserToggleButton(this));
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.BAR, this, 95, 38))
                .jeiCategories(AMEJEIRecipeType.MATTER_CONDENSER);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawString(guiGraphics, title, titleLabelX, titleLabelY, titleTextColor());
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private static class CondenserToggleButton extends MekanismButton {

        private final GuiAppliedMekanicalMatterCondenser gui;

        public CondenserToggleButton(GuiAppliedMekanicalMatterCondenser gui) {
            super(gui, 30, 34, 18, 18, Component.empty(),
                    () -> Mekanism.packetHandler()
                            .sendToServer(new PacketGuiInteract(PacketGuiInteract.GuiInteraction.NEXT_MODE, gui.tile)),
                    () -> Mekanism.packetHandler()
                            .sendToServer(new PacketGuiInteract(PacketGuiInteract.GuiInteraction.PREVIOUS_MODE, gui.tile)),
                    gui.getOnHover(() -> {
                        CondenserOutput t = gui.tile.getMode();
                        if (t == CondenserOutput.MATTER_BALLS) {
                            return Component.translatable("gui.tooltips.ae2.MatterBalls",
                                    CondenserOutput.MATTER_BALLS.requiredPower);
                        }
                        if (t == CondenserOutput.SINGULARITY) {
                            return Component.translatable("gui.tooltips.ae2.Singularity",
                                    CondenserOutput.SINGULARITY.requiredPower);
                        }
                        return Component.translatable("gui.tooltips.ae2.Trash");
                    }));
            this.gui = gui;
        }

        @Override
        public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.drawBackground(guiGraphics, mouseX, mouseY, partialTicks);
            guiGraphics.blit(AppEng.makeId("textures/guis/states.png"),
                    30, 34, // position in gui
                    18, 18, // size in gui
                    gui.tile.getMode().ordinal() * 16, 112, // position in texture
                    16, 16, // size in texture
                    256, 256);// size of texture
        }

    }
}