package ammm.block.blockentity.appliedmachine;

import ammm.block.blockentity.appliedmachine.prefab.AppliedItemStackGasToItemStackMachine;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AppliedPurificationChamber extends AppliedItemStackGasToItemStackMachine {

    public AppliedPurificationChamber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.purificationChamber.get().multiply(200)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @Override
    protected IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, ?> getRecipeType() {
        return MekanismRecipeType.PURIFYING;
    }

}
