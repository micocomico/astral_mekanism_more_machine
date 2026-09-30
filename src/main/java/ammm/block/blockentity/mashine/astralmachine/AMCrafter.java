package ammm.block.blockentity.mashine.astralmachine;

import ammm.block.blockentity.mashine.basemachine.BMCrafter;
import astral_mekanism.recipes.lookup.AstralCraftingRecipeLookUpHandler;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class AMCrafter extends BMCrafter
        implements AstralCraftingRecipeLookUpHandler {

    public AMCrafter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected BasicFluidTank createFluidTank(IContentsListener recipeCacheListener) {
        return BasicFluidTank.input(0x7fffffff,
                stack -> containsInputFluidOther(stack,
                        Arrays.stream(inputSlots).map(IInventorySlot::getStack).toArray(ItemStack[]::new),
                        gasTank.getStack()),
                this::containsInputFluid, recipeCacheListener);
    }

    @Override
    protected IGasTank createGasTank(IContentsListener recipeCacheListener) {
         return ChemicalTankBuilder.GAS.input(Long.MAX_VALUE,
                gas -> containsInputGasOther(gas,
                        Arrays.stream(inputSlots).map(IInventorySlot::getStack).toArray(ItemStack[]::new),
                        fluidTank.getFluid()),
                this::containsInputGas, recipeCacheListener);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return 0x7fffffff;
    }
}