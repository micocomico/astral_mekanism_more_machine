package ammm.block.gui.machine.applied;

import ammm.block.blockentity.mashine.appliedmachine.AppliedPaintingMachine;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedPaintingMachine extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedPaintingMachine> {

    public GuiAppliedPaintingMachine(MekanismTileContainer<AppliedPaintingMachine> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.PAINTING };
    }

}
