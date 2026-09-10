package ammm.block.blockentity.enchantedfactory;

import ammm.block.blockentity.basefactory.BFElectric;
import ammm.block.blockentity.basefactory.BFSawing;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EFSawing extends BFSawing<EFSawing> {

    public EFSawing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    private int baselineMaxOperations = 1;

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    public EFSawing getSelf() {
        return this;
    }
    public MachineEnergyContainer<EFSawing> getEnergyContainer() {
        return energyContainer;
    }

    @Override
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
    }
}
