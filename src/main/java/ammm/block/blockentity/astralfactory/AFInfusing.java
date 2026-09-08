package ammm.block.blockentity.astralfactory;

import ammm.block.blockentity.basefactory.BFAdvanced;
import ammm.block.blockentity.basefactory.BFInfusing;
import mekanism.api.IContentsListener;
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

public class AFInfusing extends BFInfusing<AFInfusing> {

    public AFInfusing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    public @NotNull IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper.forSideInfusionWithConfig(this::getDirection, this::getConfig);
        infusionTank = ChemicalTankBuilder.INFUSION.create(Long.MAX_VALUE, this::containsRecipeB, markAllMonitorsChanged(listener));
        builder.addTank(infusionTank);
        return builder.build();
    }

    public AFInfusing getSelf() {
        return this;
    }
    public MachineEnergyContainer<AFInfusing> getEnergyContainer() {
        return energyContainer;
    }
}
