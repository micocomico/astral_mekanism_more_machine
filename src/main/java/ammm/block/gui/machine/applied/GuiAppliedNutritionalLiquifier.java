package ammm.block.gui.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedNutritionalLiquifier;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedNutritionalLiquifier extends GuiAppliedSingleToSingleEnergizedMachine<AppliedNutritionalLiquifier> {

    public GuiAppliedNutritionalLiquifier(MekanismTileContainer<AppliedNutritionalLiquifier> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.NUTRITIONAL_LIQUIFICATION };
    }

}
