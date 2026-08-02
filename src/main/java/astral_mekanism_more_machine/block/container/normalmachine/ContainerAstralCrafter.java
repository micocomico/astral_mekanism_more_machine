package astral_mekanism_more_machine.block.container.normalmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism_more_machine.block.blockentity.normalmachine.AstralCrafter;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

public class ContainerAstralCrafter extends MekanismTileContainer<AstralCrafter> {

    public ContainerAstralCrafter(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
                                  @NotNull AstralCrafter tile) {
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
