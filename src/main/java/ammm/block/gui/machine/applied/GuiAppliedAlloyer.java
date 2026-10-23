package ammm.block.gui.machine.applied;

import ammm.block.blockentity.mashine.appliedmachine.AppliedAlloyer;
import ammm.block.gui.machine.applied.prefab.GuiAppliedTripleToSingleEnergizedMachine;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedAlloyer extends GuiAppliedTripleToSingleEnergizedMachine<AppliedAlloyer> {

    public GuiAppliedAlloyer(MekanismTileContainer<AppliedAlloyer> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { EMJEI.ALLOYING };
    }

}

