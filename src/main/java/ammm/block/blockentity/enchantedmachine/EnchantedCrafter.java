package ammm.block.blockentity.enchantedmachine;

import ammm.block.blockentity.bacemachine.Crafter;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class EnchantedCrafter extends Crafter {

    private int baselineMaxOperations = 1;
    private int inputTankCapacity = 1 * 10000;

    public EnchantedCrafter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
                                                    IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank = BasicFluidTank.input(inputTankCapacity,
                stack -> containsInputFluidOther(stack,
                        Arrays.stream(inputSlots).map(IInventorySlot::getStack).toArray(ItemStack[]::new),
                        gasTank.getStack()),
                this::containsInputFluid, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
                                                                              IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(gasTank = ChemicalTankBuilder.GAS.input(inputTankCapacity,
                gas -> containsInputGasOther(gas,
                        Arrays.stream(inputSlots).map(IInventorySlot::getStack).toArray(ItemStack[]::new),
                        fluidTank.getFluid()),
                this::containsInputGas, recipeCacheListener));
        return builder.build();
    }

    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

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
        inputTankCapacity = MathUtils.clampToInt(10000l * baselineMaxOperations);
    }

    public double getScaledProgress() {
        return getActive() ? 1 : 0;
    }
}
