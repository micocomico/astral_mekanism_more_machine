package ammm.block.gui.machine.applied;

import ammm.block.blockentity.mashine.appliedmachine.AppliedMekanicalInscriber;
import ammm.block.gui.machine.applied.prefab.GuiAppliedTripleToSingleEnergizedMachine;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedMekanicalInscriber extends GuiAppliedTripleToSingleEnergizedMachine<AppliedMekanicalInscriber> {

    public GuiAppliedMekanicalInscriber(MekanismTileContainer<AppliedMekanicalInscriber> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { AMEJEIRecipeType.INSCRIBE };
    }

}

