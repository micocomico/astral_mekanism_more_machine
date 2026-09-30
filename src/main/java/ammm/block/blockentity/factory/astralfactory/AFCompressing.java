package ammm.block.blockentity.factory.astralfactory;

import ammm.block.blockentity.factory.basefactory.BFAdvanced;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AFCompressing extends BFAdvanced<AFCompressing> {

    public AFCompressing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> getRecipeType() {
        return MekanismRecipeType.COMPRESSING;
    }

    @Override
    protected IGasTank createGasTank(IContentsListener listener) {
        return ChemicalTankBuilder.GAS.create(Long.MAX_VALUE, this::containsRecipeB, markAllMonitorsChanged(listener));
    }

    public AFCompressing getSelf() {
        return this;
    }
    public MachineEnergyContainer<AFCompressing> getEnergyContainer() {
        return energyContainer;
    }
}
