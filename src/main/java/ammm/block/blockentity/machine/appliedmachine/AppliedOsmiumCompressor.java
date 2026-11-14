package ammm.block.blockentity.machine.appliedmachine;

import ammm.block.blockentity.machine.appliedmachine.prefab.AppliedItemStackGasToItemStackMachine;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AppliedOsmiumCompressor extends AppliedItemStackGasToItemStackMachine<ItemStackGasToItemStackRecipe> {

    public AppliedOsmiumCompressor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.osmiumCompressor.get().multiply(200)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()), 200);
    }

    @Override
    protected IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, ?> getRecipeType() {
        return MekanismRecipeType.COMPRESSING;
    }

}
