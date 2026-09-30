package ammm.block.blockentity.mashine.astralmachine;

import astral_mekanism.block.blockentity.basemachine.BEAMEAdsorptionSeparator;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.fluid.VariableCapacityFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AMAdsorptionSeparator extends BEAMEAdsorptionSeparator {

    public AMAdsorptionSeparator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    protected BasicFluidTank createInputTank(IContentsListener recipeCacheListener) {
        return VariableCapacityFluidTank.create(0x7fffffff,
                (f, a) -> a == AutomationType.MANUAL,
                (stack, type) -> this.containsRecipeBA(inputSlot.getStack(), stack),
                this::containsRecipeB, recipeCacheListener);
    }

}
