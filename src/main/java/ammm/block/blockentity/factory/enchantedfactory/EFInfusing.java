package ammm.block.blockentity.factory.enchantedfactory;

import ammm.block.blockentity.factory.basefactory.BFInfusing;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EFInfusing extends BFInfusing<EFInfusing> {

    public EFInfusing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    private int baselineMaxOperations = 1;
    private long infusionTankCapacity = 1 * 10000;

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    protected IInfusionTank createInfusionTank(IContentsListener listener) {
        return ChemicalTankBuilder.INFUSION.create(infusionTankCapacity * tier.processes, this::containsRecipeB, markAllMonitorsChanged(listener));
    }

    public EFInfusing getSelf() {
        return this;
    }
    public MachineEnergyContainer<EFInfusing> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            if (AMEEmpowered.isEmpoweredSpeed(upgrade) || upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
                baselineMaxOperations = ((1 << upgradeComponent.getUpgrades(Upgrade.SPEED)) + (2 << AMEEmpowered
                        .getEmpoweredSpeeds(this))) << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
            }
        } else if (upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 1 << (upgradeComponent.getUpgrades(Upgrade.SPEED)
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
        }
        infusionTankCapacity = 10000l * baselineMaxOperations;
    }
}
