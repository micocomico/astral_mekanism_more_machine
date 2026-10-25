package ammm.block.gui.machine.applied;

import ammm.block.blockentity.mashine.appliedmachine.AppliedAPT;
import ammm.block.blockentity.mashine.appliedmachine.AppliedAdsorptionSeparator;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import fixdol.mekanismelements.client.MSJEIRecipeType;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedAdsorptionSeparator extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedAdsorptionSeparator> {

    public GuiAppliedAdsorptionSeparator(MekanismTileContainer<AppliedAdsorptionSeparator> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MSJEIRecipeType.ADSORPTION_SEPARATOR };
    }

}
