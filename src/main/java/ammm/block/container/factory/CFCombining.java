package ammm.block.container.factory;

import ammm.block.blockentity.factory.basefactory.BFCombining;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CFCombining<BE extends BFCombining<BE>> extends CFBase<BE> {

    public CFCombining(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
                       @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 14;
    }
}
