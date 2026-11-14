package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedIOR;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import fixdol.mekanismelements.client.MSJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fml.ModList;

public class GuiAppliedIOR extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedIOR> {

    public GuiAppliedIOR(MekanismTileContainer<AppliedIOR> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return ModList.get().isLoaded("emi") ? new MekanismJEIRecipeType[] { } : new MekanismJEIRecipeType[] { MSJEIRecipeType.INFINITY_ORE_REPROCESSING };
    }

}
