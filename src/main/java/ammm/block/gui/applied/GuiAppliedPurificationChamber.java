package ammm.block.gui.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedPurificationChamber;
import ammm.block.gui.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
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
