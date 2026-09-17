package ammm.block.gui.machine.applied;

import ammm.block.blockentity.appliedmachine.AppliedOsmiumCompressor;
import ammm.block.gui.machine.applied.prefab.GuiAppliedDoubleToSingleEnergizedMachine;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedCrusher;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedOsmiumCompressor extends GuiAppliedDoubleToSingleEnergizedMachine<AppliedOsmiumCompressor> {

    public GuiAppliedOsmiumCompressor(MekanismTileContainer<AppliedOsmiumCompressor> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.COMPRESSING };
    }

}
