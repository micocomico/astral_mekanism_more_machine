package ammm.block.blockentity.machine.appliedmachine;

import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedItemStackToItemStackMachine;
import com.jerry.mekmm.common.config.MoreMachineConfig;
import com.jerry.mekmm.common.recipe.MoreMachineRecipeType;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AppliedCncRollingMill extends BEAppliedItemStackToItemStackMachine {

    public AppliedCncRollingMill(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MoreMachineConfig.usage.cnc_rollingMill.get().multiply(200)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @Override
    protected IMekanismRecipeTypeProvider<ItemStackToItemStackRecipe, ?> getRecipeType() {
        return MoreMachineRecipeType.ROLLING_MILL;
    }

}
