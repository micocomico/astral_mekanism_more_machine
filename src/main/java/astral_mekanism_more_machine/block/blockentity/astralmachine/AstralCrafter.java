package astral_mekanism_more_machine.block.blockentity.astralmachine;

import java.util.Arrays;

import astral_mekanism.block.blockentity.normalmachine.BEAstralCrafter;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import net.minecraft.world.item.ItemStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.IContentsListener;

public class AstralCrafter extends BEAstralCrafter{

    private InputInventorySlot[] inputSlots;
    private IGasTank gasTank;
    private IContentsListener recipeCacheListener;

    public AstralCrafter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public int getOperatingTicks() { return 0x7fffffff; }

    public int getTicksRequired() { return 0x00000001; }

    public BasicFluidTank getFluidTank() {
        return BasicFluidTank.input(0x7fffffff,
                stack -> containsInputFluidOther(stack,
                        Arrays.stream(inputSlots).map(IInventorySlot::getStack).toArray(ItemStack[]::new),
                        gasTank.getStack()),this::containsInputFluid, recipeCacheListener);
    }
}
