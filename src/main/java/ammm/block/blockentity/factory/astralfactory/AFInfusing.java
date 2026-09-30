package ammm.block.blockentity.factory.astralfactory;

import ammm.block.blockentity.factory.basefactory.BFInfusing;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AFInfusing extends BFInfusing<AFInfusing> {

    public AFInfusing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected IInfusionTank createInfusionTank(IContentsListener listener) {
        return ChemicalTankBuilder.INFUSION.create(Long.MAX_VALUE, this::containsRecipeB, markAllMonitorsChanged(listener));
    }

    public AFInfusing getSelf() {
        return this;
    }
    public MachineEnergyContainer<AFInfusing> getEnergyContainer() {
        return energyContainer;
    }
}
