package ammm.block.blockentity.machine.astralmachine;

import ammm.block.blockentity.machine.basemachine.BMPlantingStation;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AMPlantingStation extends BMPlantingStation {

    public AMPlantingStation(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected IGasTank createGasTank(IContentsListener recipeCacheListener) {
        return ChemicalTankBuilder.GAS.create(Long.MAX_VALUE, ChemicalTankBuilder.GAS.alwaysTrueBi,
                (gas, automationType) -> containsRecipeBA(inputSlot.getStack(), gas), this::containsRecipeB, recipeCacheListener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

}
