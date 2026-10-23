package ammm.block.blockentity.mashine.appliedmachine;

import ammm.block.blockentity.interf.applied.IAppliedDoubleToSingleMachine;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.item.recipecard.ChemicalIngredientCardItem;
import astral_mekanism.recipes.recipe.GasInfusionToFluidRecipe;
import astral_mekanism.registries.AMERecipeTypes;
import astral_mekanism.util.AMEKeyUtils;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class AppliedInfusingCondensentrator extends BEAppliedEnergizedMachine
        implements IAppliedDoubleToSingleMachine {

    private int duration;
    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey outputKey;
    private long inputAmountA;
    private long inputAmountB;
    private long outputAmount;

    public AppliedInfusingCondensentrator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, AMEConfig.usage.greenHouse.get()
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlotA = BasicInventorySlot.at(stack -> stack.getItem() instanceof ChemicalIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 55, 53));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ChemicalIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 74, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        inputKeyB = null;
        outputKey = null;
        inputAmountA = 0;
        inputAmountB = 0;
        outputAmount = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlotA.isEmpty() || cardSlotB.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        if (isA.getItem() instanceof ChemicalIngredientCardItem cardItemA && isB.getItem() instanceof ChemicalIngredientCardItem cardItemB) {
            MekanismKey keyA = cardItemA.getKey(isA);
            MekanismKey keyB = cardItemB.getKey(isB);
            if (keyA != null && keyB != null && hasLevel()) {
                if(AMEKeyUtils.getInfusion(keyA) != InfusionStack.EMPTY && AMEKeyUtils.getGas(keyB) != GasStack.EMPTY){
                    InfusionStack stackA = AMEKeyUtils.getInfusion(keyA);
                    GasStack stackB = AMEKeyUtils.getGas(keyB);
                    stackA.setAmount(Long.MAX_VALUE);
                    stackB.setAmount(Long.MAX_VALUE);
                    level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                            .stream().filter(r -> r.test(stackB,stackA)).findFirst()
                            .ifPresentOrElse(r -> {
                                inputKeyA = keyA;
                                inputKeyB = keyB;
                                inputAmountA = r.getInfusionInput().getNeededAmount(stackA);
                                inputAmountB = r.getGasInput().getNeededAmount(stackB);
                                FluidStack output = r.getOutput();
                                outputKey = AEFluidKey.of(output);
                                outputAmount = output.getAmount();
                            }, this::removeRecipeInfo);
                    return;
                } else if(AMEKeyUtils.getGas(keyA) != GasStack.EMPTY && AMEKeyUtils.getInfusion(keyB) != InfusionStack.EMPTY){
                    GasStack stackA = AMEKeyUtils.getGas(keyA);
                    InfusionStack stackB = AMEKeyUtils.getInfusion(keyB);
                    stackA.setAmount(Long.MAX_VALUE);
                    stackB.setAmount(Long.MAX_VALUE);
                    level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                            .stream().filter(r -> r.test(stackA,stackB)).findFirst()
                            .ifPresentOrElse(r -> {
                                inputKeyA = keyA;
                                inputKeyB = keyB;
                                inputAmountB = r.getGasInput().getNeededAmount(stackA);
                                inputAmountA = r.getInfusionInput().getNeededAmount(stackB);
                                FluidStack output = r.getOutput();
                                outputKey = AEFluidKey.of(output);
                                outputAmount = output.getAmount();
                            }, this::removeRecipeInfo);
                    return;
                }
            }
        }
        removeRecipeInfo();
        return;
    }

    protected IMekanismRecipeTypeProvider<GasInfusionToFluidRecipe, ?> getRecipeType(){
        return AMERecipeTypes.INFUSING_CONDENSE;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmountA > 0 && inputAmountB > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(Math.min(
                    storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA,
                    storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB),
                    storage.insert(outputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmount);
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                storage.extract(inputKeyB, operations * inputAmountB, Actionable.MODULATE, source);
                storage.insert(outputKey, operations * outputAmount, Actionable.MODULATE, source);
                consumeEnergy(storage, source, operations);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKeyA() {
        return inputKeyA;
    }

    public AEKey getInputKeyB() {
        return inputKeyB;
    }

    public AEKey getOutputKey() {
        return outputKey;
    }
}
