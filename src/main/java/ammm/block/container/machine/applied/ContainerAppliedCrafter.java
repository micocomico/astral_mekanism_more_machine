package ammm.block.container.machine.applied;

import ammm.block.blockentity.machine.appliedmachine.AppliedCrafter;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ContainerAppliedCrafter extends MekanismTileContainer<AppliedCrafter> {
    public ContainerAppliedCrafter(ContainerTypeRegistryObject<?> type, int id, Inventory inv, @NotNull AppliedCrafter appliedCrafter) {
        super(type, id, inv, appliedCrafter);
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 54;
    }

    @Override
    protected int getInventoryXOffset() {
        return super.getInventoryXOffset() + 36;
    }
}
