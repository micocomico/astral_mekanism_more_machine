package ammm.block.container.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedReactionChamber;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ContainerAppliedReactionChamber extends MekanismTileContainer<AppliedReactionChamber> {
    public ContainerAppliedReactionChamber(ContainerTypeRegistryObject<?> type, int id, Inventory inv, @NotNull AppliedReactionChamber appliedReactionChamber) {
        super(type, id, inv, appliedReactionChamber);
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 20;
    }
}
