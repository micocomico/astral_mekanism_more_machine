package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedPRC;
import ammm.block.gui.machine.applied.prefab.GuiAppliedTripleToDoubleEnergizedMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedPRC extends GuiAppliedTripleToDoubleEnergizedMachine<AppliedPRC> {

    public GuiAppliedPRC(MekanismTileContainer<AppliedPRC> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.REACTION };
    }

}

