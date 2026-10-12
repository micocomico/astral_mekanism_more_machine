package ammm.block.blockentity.mashine.enchantedmachine;

import astral_mekanism.block.blockentity.basemachine.BEAbstractMekanicalComposter;
import astral_mekanism.integration.AMEEmpowered;
import com.jerry.mekanism_extras.api.ExtraUpgrade;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EMComposter extends BEAbstractMekanicalComposter {

    public EMComposter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getDefaltBaseline() {
        return 200;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            setBaselineMaxOperations(200 * (1 << AMEEmpowered.getAllSpeeds(this)));
        } else if (upgrade == Upgrade.SPEED) {
            setBaselineMaxOperations(200 * (1 << upgradeComponent.getUpgrades(Upgrade.SPEED)));
        }
    }
}
