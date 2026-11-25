package ammm.block.container.machine;

import ammm.block.blockentity.machine.basemachine.BMFluidCombiner;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CMFluidCombiner<BE extends BMFluidCombiner> extends MekanismTileContainer<BE> {

    public CMFluidCombiner(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
                           @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 32;
    }

}
