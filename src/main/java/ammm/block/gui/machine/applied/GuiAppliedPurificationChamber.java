package ammm.block.gui.machine.applied;

import ammm.block.blockentity.appliedmachine.AppliedPurificationChamber;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedPurificationChamber extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedPurificationChamber> {

    public GuiAppliedPurificationChamber(MekanismTileContainer<AppliedPurificationChamber> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.PURIFYING };
    }

}
