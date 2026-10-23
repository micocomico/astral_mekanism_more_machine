package ammm.block.gui.machine.applied;

import ammm.block.blockentity.mashine.appliedmachine.AppliedRadiationIrradiator;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import fixdol.mekanismelements.client.MSJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedRadiationIrradiator extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedRadiationIrradiator> {

    public GuiAppliedRadiationIrradiator(MekanismTileContainer<AppliedRadiationIrradiator> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] {MSJEIRecipeType.RADIATION_IRRADIATOR};
    }

}
