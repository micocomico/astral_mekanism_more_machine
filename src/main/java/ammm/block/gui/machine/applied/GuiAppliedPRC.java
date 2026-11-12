package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedCncLathe;
import ammm.block.blockentity.machine.appliedmachine.AppliedPRC;
import ammm.block.gui.machine.applied.prefab.GuiAppliedTripleToDoubleEnergizedMachine;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import com.jerry.mekmm.client.jei.MoreMachineJEIRecipeType;
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

public class GuiAppliedPRC extends GuiAppliedTripleToDoubleEnergizedMachine<AppliedPRC> {

    public GuiAppliedPRC(MekanismTileContainer<AppliedPRC> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.REACTION };
    }

}

