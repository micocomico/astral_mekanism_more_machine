package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedCDM;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToDoubleEnergizedMachine;
import fixdol.mekanismelements.client.MSJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fml.ModList;

public class GuiAppliedCDM extends GuiAppliedDoubleToDoubleEnergizedMachine<AppliedCDM> {

    public GuiAppliedCDM(MekanismTileContainer<AppliedCDM> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return ModList.get().isLoaded("emi") ? new MekanismJEIRecipeType[] { } : new MekanismJEIRecipeType[] { MSJEIRecipeType.CHEMICAL_DEMOLITION_MACHINE };
    }

}
