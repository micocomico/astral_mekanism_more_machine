package ammm.block.blockentity.astralfactory;

import ammm.block.blockentity.basefactory.BFCombining;
import ammm.block.blockentity.basefactory.BFElectric;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AFCombining extends BFCombining<AFCombining> {

    public AFCombining(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }

    public AFCombining getSelf() {
        return this;
    }
    public MachineEnergyContainer<AFCombining> getEnergyContainer() {
        return energyContainer;
    }
}
