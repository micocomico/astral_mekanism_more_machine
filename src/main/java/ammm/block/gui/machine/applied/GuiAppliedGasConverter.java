package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedGasConverter;
import ammm.block.gui.machine.applied.prefab.GuiAppliedSingleToSingleMachine;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedGasConverter extends GuiAppliedSingleToSingleMachine<AppliedGasConverter> {

    public GuiAppliedGasConverter(MekanismTileContainer<AppliedGasConverter> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { AMEJEIRecipeType.GAS_CONVERSION };
    }

}
