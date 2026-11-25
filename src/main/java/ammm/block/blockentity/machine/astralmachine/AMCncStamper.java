package ammm.block.blockentity.machine.astralmachine;

import ammm.block.blockentity.machine.basemachine.BMCncStamper;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AMCncStamper extends BMCncStamper {

    public AMCncStamper(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
    
}
