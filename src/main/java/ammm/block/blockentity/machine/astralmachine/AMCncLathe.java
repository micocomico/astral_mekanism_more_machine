package ammm.block.blockentity.machine.astralmachine;

import astral_mekanism.block.blockentity.basemachine.BEAMEElectricMachine;
import com.jerry.mekmm.common.recipe.MoreMachineRecipeType;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AMCncLathe extends BEAMEElectricMachine {

    public AMCncLathe(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state,"mekmm:lathe");
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> getRecipeType() {
        return MoreMachineRecipeType.LATHING;
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
    
}
