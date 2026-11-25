package ammm.block.blockentity.machine.enchantedmachine;

import ammm.block.blockentity.machine.basemachine.BMPigmentMixer;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EMPigmentMixer extends BMPigmentMixer {
    private int baselineMaxOperations = 200;
    private long gasTankCapacity = 200 * 10000;

    public EMPigmentMixer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected IPigmentTank createLeftPigmentTank(IContentsListener recipeCacheListener) {
        return ChemicalTankBuilder.PIGMENT.input(gasTankCapacity, pigment -> containsRecipe(pigment, rightInputTank.getStack()),
                this::containsRecipe, recipeCacheListener);
    }

    @Override
    protected IPigmentTank createRightPigmentTank(IContentsListener recipeCacheListener) {
        return ChemicalTankBuilder.PIGMENT.input(gasTankCapacity, pigment -> containsRecipe(pigment, leftInputTank.getStack()),
                this::containsRecipe, recipeCacheListener);
    }

    @Override
    protected IPigmentTank createCenterPigmentTank(IContentsListener listener) {
        return ChemicalTankBuilder.PIGMENT.output(gasTankCapacity * 2, listener);
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
                baselineMaxOperations = 200 * ((1 << upgradeComponent.getUpgrades(Upgrade.SPEED)) + (2 << AMEEmpowered
                        .getEmpoweredSpeeds(this))) << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
            }
        } else if (upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 200 << (upgradeComponent.getUpgrades(Upgrade.SPEED)
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
        }
        gasTankCapacity = 10000l * baselineMaxOperations;
    }

}
