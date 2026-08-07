package ammm.block.container;

import ammm.block.blockentity.enchantedmachine.EnchantedCrafter;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ContainerEnchantedCrafter extends MekanismTileContainer<EnchantedCrafter> {

    public ContainerEnchantedCrafter(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
                                     @NotNull EnchantedCrafter tile) {
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
