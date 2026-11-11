package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedRecycler;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import com.jerry.mekmm.client.jei.MoreMachineJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedRecycler extends GuiAppliedSingleToSingleEnergizedMachine<AppliedRecycler> {

    public GuiAppliedRecycler(MekanismTileContainer<AppliedRecycler> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MoreMachineJEIRecipeType.RECYCLING };
    }

}
