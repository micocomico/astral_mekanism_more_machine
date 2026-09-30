package ammm.block.blockentity.mashine.astralmachine;

import astral_mekanism.block.blockentity.basemachine.BEAMEAirCompressor;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AMAirCompressor extends BEAMEAirCompressor {

    public AMAirCompressor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        COMPRESSED_AIR_STACK.setAmount(Integer.MAX_VALUE);
    }

}
