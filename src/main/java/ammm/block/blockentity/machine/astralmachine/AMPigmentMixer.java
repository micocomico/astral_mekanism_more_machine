package ammm.block.blockentity.machine.astralmachine;

import ammm.block.blockentity.machine.basemachine.BMPigmentMixer;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class AMPigmentMixer extends BMPigmentMixer {

    public AMPigmentMixer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected IPigmentTank createLeftPigmentTank(IContentsListener recipeCacheListener) {
        return ChemicalTankBuilder.PIGMENT.input(Long.MAX_VALUE, pigment -> containsRecipe(pigment, rightInputTank.getStack()),
                this::containsRecipe, recipeCacheListener);
    }

    @Override
    protected IPigmentTank createRightPigmentTank(IContentsListener recipeCacheListener) {
        return ChemicalTankBuilder.PIGMENT.input(Long.MAX_VALUE, pigment -> containsRecipe(pigment, leftInputTank.getStack()),
                this::containsRecipe, recipeCacheListener);
    }

    @Override
    protected IPigmentTank createCenterPigmentTank(IContentsListener listener) {
        return ChemicalTankBuilder.PIGMENT.output(Long.MAX_VALUE, listener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

}
