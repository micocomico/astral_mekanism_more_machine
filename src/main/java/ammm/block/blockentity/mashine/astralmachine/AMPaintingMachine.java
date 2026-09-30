package ammm.block.blockentity.mashine.astralmachine;

import astral_mekanism.block.blockentity.basemachine.BEAMEPaintingMachine;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class AMPaintingMachine extends BEAMEPaintingMachine {

    public AMPaintingMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    private long getPigmentTankCapacity() {
        return 0x7fffffffffffffffl;
    }

    @Override
    protected IPigmentTank createPigmentTank(Predicate<Pigment> canInsert, Predicate<Pigment> validator,
            @Nullable IContentsListener listener) {
        return VariableCapacityChemicalTankBuilder.PIGMENT.create(this::getPigmentTankCapacity,
                (pigment, type) -> type == AutomationType.MANUAL,
                (pigment, type) -> canInsert.test(pigment), validator, listener);
    }

}
