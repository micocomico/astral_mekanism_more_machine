package ammm.block.blockentity.machine.astralmachine;

import ammm.block.blockentity.machine.basemachine.BMFluidCombiner;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AMFluidCombiner extends BMFluidCombiner {

    public AMFluidCombiner(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected BasicFluidTank createInputFluidTank(IContentsListener recipeCacheListener) {
        return BasicFluidTank.input(Integer.MAX_VALUE, this::containsRecipeA, this::containsRecipeA, recipeCacheListener);
    }

    @Override
    protected BasicFluidTank createExtraFluidTank(IContentsListener recipeCacheListener) {
        return BasicFluidTank.input(Integer.MAX_VALUE, this::containsRecipeB, this::containsRecipeB, recipeCacheListener);
    }

    @Override
    protected BasicFluidTank createOutputFluidTank(IContentsListener recipeCacheListener) {
        return BasicFluidTank.output(Integer.MAX_VALUE, recipeCacheListener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

}
