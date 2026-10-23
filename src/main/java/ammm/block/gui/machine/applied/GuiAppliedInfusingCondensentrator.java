package ammm.block.gui.machine.applied;

import ammm.block.blockentity.mashine.appliedmachine.AppliedInfusingCondensentrator;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedInfusingCondensentrator extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedInfusingCondensentrator> {

    public GuiAppliedInfusingCondensentrator(MekanismTileContainer<AppliedInfusingCondensentrator> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { AMEJEIRecipeType.INFUSING_CONDENSE };
    }

}
