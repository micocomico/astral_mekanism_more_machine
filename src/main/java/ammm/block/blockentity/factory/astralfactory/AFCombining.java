package ammm.block.blockentity.factory.astralfactory;

import ammm.block.blockentity.factory.basefactory.BFCombining;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AFCombining extends BFCombining<AFCombining> {

    public AFCombining(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    public AFCombining getSelf() {
        return this;
    }
    public MachineEnergyContainer<AFCombining> getEnergyContainer() {
        return energyContainer;
    }
}
