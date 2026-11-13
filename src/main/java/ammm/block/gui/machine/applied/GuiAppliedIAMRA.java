package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedIAMRA;
import ammm.block.gui.machine.applied.prefab.GuiAppliedTripleToDoubleEnergizedMachine;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedIAMRA extends GuiAppliedTripleToDoubleEnergizedMachine<AppliedIAMRA> {

    public GuiAppliedIAMRA(MekanismTileContainer<AppliedIAMRA> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { AMEJEIRecipeType.RECONSTRUCTION };
    }

}

