package ammm.block.container.machine;

import ammm.block.blockentity.machine.basemachine.BMCrafter;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CMCrafter<BE extends BMCrafter> extends MekanismTileContainer<BE> {

    public CMCrafter(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
                     @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 36;
    }

    @Override
    protected int getInventoryXOffset() {
        return super.getInventoryXOffset() + 18;
    }

}
