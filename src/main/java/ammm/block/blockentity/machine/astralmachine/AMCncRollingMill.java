package ammm.block.blockentity.machine.astralmachine;

import astral_mekanism.block.blockentity.basemachine.BEAMEElectricMachine;
import com.jerry.mekmm.client.jei.MoreMachineJEIRecipeType;
import com.jerry.mekmm.common.recipe.MoreMachineRecipeType;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AMCncRollingMill extends BEAMEElectricMachine {

    public AMCncRollingMill(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state,"mekmm:rolling_mill");
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> getRecipeType() {
        return MoreMachineRecipeType.ROLLING_MILL;
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
    
}
