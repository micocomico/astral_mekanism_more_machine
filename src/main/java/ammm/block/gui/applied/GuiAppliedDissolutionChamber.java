package ammm.block.gui.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedDissolutionChamber;
import ammm.block.gui.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedDissolutionChamber extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedDissolutionChamber> {

    public GuiAppliedDissolutionChamber(MekanismTileContainer<AppliedDissolutionChamber> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.DISSOLUTION };
    }

}
