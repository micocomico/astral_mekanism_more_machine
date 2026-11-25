package ammm.block.blockentity.machine.enchantedmachine;

import ammm.block.blockentity.machine.basemachine.BMFluidCombiner;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EMFluidCombiner extends BMFluidCombiner {
    private int baselineMaxOperations = 2;
    private int fluidTankCapacity = 2 * 16000;

    public EMFluidCombiner(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected BasicFluidTank createInputFluidTank(IContentsListener recipeCacheListener) {
        return BasicFluidTank.input(fluidTankCapacity, this::containsRecipeA, this::containsRecipeA, recipeCacheListener);
    }

    @Override
    protected BasicFluidTank createExtraFluidTank(IContentsListener recipeCacheListener) {
        return BasicFluidTank.input(fluidTankCapacity, this::containsRecipeB, this::containsRecipeB, recipeCacheListener);
    }

    @Override
    protected BasicFluidTank createOutputFluidTank(IContentsListener recipeCacheListener) {
        return BasicFluidTank.output(fluidTankCapacity, recipeCacheListener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            if (AMEEmpowered.isEmpoweredSpeed(upgrade) || upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
                baselineMaxOperations = 2 * ((1 << upgradeComponent.getUpgrades(Upgrade.SPEED)) + (2 << AMEEmpowered
                        .getEmpoweredSpeeds(this))) << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
            }
        } else if (upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 2 << (upgradeComponent.getUpgrades(Upgrade.SPEED)
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
        }
        fluidTankCapacity = 16000 * baselineMaxOperations;
    }

}
