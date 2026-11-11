package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedCncStamper;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import com.jerry.mekmm.client.jei.MoreMachineJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedCncStamper extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedCncStamper> {

    public GuiAppliedCncStamper(MekanismTileContainer<AppliedCncStamper> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MoreMachineJEIRecipeType.CNC_STAMPING };
    }

}
