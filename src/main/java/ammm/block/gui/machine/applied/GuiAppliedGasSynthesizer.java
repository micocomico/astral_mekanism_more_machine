package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedGasSynthesizer;
import ammm.block.gui.machine.applied.prefab.GuiAppliedSingleToSingleMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedGasSynthesizer extends GuiAppliedSingleToSingleMachine<AppliedGasSynthesizer> {

    public GuiAppliedGasSynthesizer(MekanismTileContainer<AppliedGasSynthesizer> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.GAS_CONVERSION };
    }

}
