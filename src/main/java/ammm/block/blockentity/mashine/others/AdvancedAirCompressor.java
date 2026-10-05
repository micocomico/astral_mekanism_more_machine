package ammm.block.blockentity.mashine.others;

import astral_mekanism.block.blockentity.basemachine.BEAMEAirCompressor;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class AdvancedAirCompressor extends BEAMEAirCompressor {

    @Nonnull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSide(this::getDirection);
        builder.addTank(this.gasTank = (IGasTank) ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    public AdvancedAirCompressor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        COMPRESSED_AIR_STACK.setAmount(Integer.MAX_VALUE);
    }

}
