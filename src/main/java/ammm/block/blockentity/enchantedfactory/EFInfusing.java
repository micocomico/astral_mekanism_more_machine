package ammm.block.blockentity.enchantedfactory;

import ammm.block.blockentity.basefactory.BFAdvanced;
import ammm.block.blockentity.basefactory.BFInfusing;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper.forSideInfusionWithConfig(this::getDirection, this::getConfig);
        infusionTank = ChemicalTankBuilder.INFUSION.create(infusionTankCapacity * tier.processes, this::containsRecipeB, markAllMonitorsChanged(listener));
        builder.addTank(infusionTank);
        return builder.build();
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
