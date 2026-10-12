package ammm.block.blockentity.mashine.enchantedmachine;

import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.block.blockentity.basemachine.BEAbstractTransformer;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import astral_mekanism.integration.AMEEmpowered;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EMTransformer extends BEAbstractTransformer {
    private int baselineMaxOperations = 1;

    public EMTransformer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int fluidTankCapacity() {
        return 2000 * 200;
    }

    @Override
    protected GeneralCachedRecipe<TransformRecipe> operateCachedRecipe(
            GeneralCachedRecipe<TransformRecipe> cachedRecipe) {
        return cachedRecipe.setBaselineMaxOperations(() -> baselineMaxOperations);
    }

    @Override
    protected CachedRecipe<MekanicalTransformRecipe> operateCachedRecipe(
            CachedRecipe<MekanicalTransformRecipe> cachedRecipe) {
        return cachedRecipe.setBaselineMaxOperations(() -> baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            if (AMEEmpowered.isEmpoweredSpeed(upgrade) || upgrade == ExtraUpgrade.STACK) {
                baselineMaxOperations = 200 * (1 << (AMEEmpowered.getEmpoweredSpeeds(this)
                        + upgradeComponent.getUpgrades(ExtraUpgrade.STACK)));
            }
        } else if (upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 200 * (1 << upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
        }
    }

}
