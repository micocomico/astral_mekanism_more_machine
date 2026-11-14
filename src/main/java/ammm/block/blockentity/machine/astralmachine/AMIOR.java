package ammm.block.blockentity.machine.astralmachine;

import ammm.block.blockentity.machine.basemachine.BMCDM;
import ammm.block.blockentity.machine.basemachine.BMIOR;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AMIOR extends BMIOR {

    public AMIOR(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected IGasTank createGasTank() {
        return ChemicalTankBuilder.GAS.input(Long.MAX_VALUE,
                this::containsRecipe,
                recipeCacheLookupMonitor);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

}
