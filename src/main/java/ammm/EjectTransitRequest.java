package ammm;

import mekanism.common.lib.inventory.TileTransitRequest;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EjectTransitRequest extends TileTransitRequest {

    public Direction side;

    public EjectTransitRequest(BlockEntity tile, Direction side) {
        super(tile, side);
        this.side = side;
    }

    @Override
    public Direction getSide() {
        return side;
    }
}
