package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedPigmentExtractor;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedPigmentExtractor extends GuiAppliedSingleToSingleEnergizedMachine<AppliedPigmentExtractor> {

    public GuiAppliedPigmentExtractor(MekanismTileContainer<AppliedPigmentExtractor> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.PIGMENT_EXTRACTING };
    }

}
