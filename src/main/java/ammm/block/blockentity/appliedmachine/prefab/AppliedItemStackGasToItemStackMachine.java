package ammm.block.blockentity.appliedmachine.prefab;

import ammm.block.blockentity.interf.applied.IAppliedDoubleToSingleMachine;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.block.blockentity.interf.applied.IAppliedSingleToSingleMachine;
import astral_mekanism.item.recipecard.ChemicalIngredientCardItem;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import astral_mekanism.util.AMEKeyUtils;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AppliedItemStackGasToItemStackMachine extends BEAppliedEnergizedMachine
        implements IAppliedDoubleToSingleMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey outputKey;
    private long inputAmountA;
    private long inputAmountB;
    private long outputAmount;

    public AppliedItemStackGasToItemStackMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state,
                                                 long fePerProcess) {
        super(blockProvider, pos, state, fePerProcess);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlotA = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
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
        if (isA.getItem() instanceof ItemIngredientCardItem cardItemA && isB.getItem() instanceof ChemicalIngredientCardItem cardItemB) {
            AEItemKey keyA = cardItemA.getKey(isA);
            MekanismKey keyB = cardItemB.getKey(isB);
            if (keyA != null && keyB != null && hasLevel()) {
                ItemStack stackA = keyA.toStack(0x3fffffff);
                GasStack stackB = AMEKeyUtils.getGas(keyB);
                level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                        .stream().filter(r -> r.test(stackA,stackB)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputAmountA = r.getItemInput().getNeededAmount(stackA);
                            inputAmountB = r.getChemicalInput().getNeededAmount(stackB);
                            ItemStack output = r.getOutput(stackA,stackB);
                            outputKey = AEItemKey.of(output);
                            outputAmount = output.getCount();
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    protected abstract IMekanismRecipeTypeProvider<ItemStackGasToItemStackRecipe, ?> getRecipeType();

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

    public AEKey getInputAKey() {
        return inputKeyA;
    }

    public AEKey getInputBKey() {
        return inputKeyB;
    }

    public AEKey getOutputKey() {
        return outputKey;
    }

}
