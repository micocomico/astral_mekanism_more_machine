package ammm.block.blockentity.machine.astralmachine;

import ammm.block.blockentity.machine.basemachine.BMCDM;
import ammm.block.blockentity.machine.basemachine.BMPigmentExtractor;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AMCDM extends BMCDM {

    public AMCDM(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected IGasTank createGasTank() {
        return ChemicalTankBuilder.GAS.create(Long.MAX_VALUE, allowExtractingChemical() ? ConstantPredicates.alwaysTrueBi() : ConstantPredicates.notExternal(),
                (chemical, automationType) -> containsRecipeBA(inputSlot.getStack(), chemical), this::containsRecipeB, recipeCacheLookupMonitor);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

}
