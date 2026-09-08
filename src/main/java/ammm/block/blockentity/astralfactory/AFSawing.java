package ammm.block.blockentity.astralfactory;

import ammm.block.blockentity.basefactory.BFCombining;
import ammm.block.blockentity.basefactory.BFSawing;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AFSawing extends BFSawing<AFSawing> {

    public AFSawing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    public AFSawing getSelf() {
        return this;
    }
    public MachineEnergyContainer<AFSawing> getEnergyContainer() {
        return energyContainer;
    }
}
