package ammm.block.blockentity.machine.astralmachine;

import ammm.block.blockentity.machine.basemachine.BMPigmentExtractor;
import ammm.block.blockentity.machine.basemachine.BMPigmentMixer;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AMPigmentExtractor extends BMPigmentExtractor {

    public AMPigmentExtractor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected IPigmentTank createPigmentTank(IContentsListener listener) {
        return ChemicalTankBuilder.PIGMENT.output(Long.MAX_VALUE, listener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

}
