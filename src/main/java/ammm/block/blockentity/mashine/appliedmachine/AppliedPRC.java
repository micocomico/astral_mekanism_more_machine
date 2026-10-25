package ammm.block.blockentity.mashine.appliedmachine;

import ammm.block.blockentity.interf.applied.IAppliedTripleToDoubleMachine;
import ammm.block.blockentity.interf.applied.IAppliedTripleToSingleMachine;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.item.recipecard.ChemicalIngredientCardItem;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import astral_mekanism.util.AMEKeyUtils;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class AppliedPRC extends BEAppliedEnergizedMachine
        implements IAppliedTripleToDoubleMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private BasicInventorySlot cardSlotC;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey inputKeyC;
    private AEKey outputKeyA;
    private AEKey outputKeyB;
    private long inputAmountA;
    private long inputAmountB;
    private long inputAmountC;
    private long outputAmountA;
    private long outputAmountB;

    public AppliedPRC(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.pressurizedReactionBase.get().multiply(100)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlotA = BasicInventorySlot.at(stack -> stack.getItem() instanceof FluidIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 45, 53));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ChemicalIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 64, 53));
        builder.addSlot(
                cardSlotC = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 83, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        inputKeyB = null;
        inputKeyC = null;
        outputKeyA = null;
        outputKeyB = null;
        inputAmountA = 0;
        inputAmountB = 0;
        inputAmountC = 0;
        outputAmountA = 0;
        outputAmountB = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlotA.isEmpty() || cardSlotB.isEmpty() || cardSlotC.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        ItemStack isC = cardSlotC.getStack();
        if (isA.getItem() instanceof FluidIngredientCardItem cardItemA && isB.getItem() instanceof ChemicalIngredientCardItem cardItemB && isC.getItem() instanceof ItemIngredientCardItem cardItemC) {
            AEFluidKey keyA = cardItemA.getKey(isA);
            MekanismKey keyB = cardItemB.getKey(isB);
            AEItemKey keyC = cardItemC.getKey(isC);
            if (keyA != null && keyB != null && keyC != null && hasLevel()) {
                FluidStack stackA = keyA.toStack(0x7fffffff);
                GasStack stackB = AMEKeyUtils.getGas(keyB);
                stackB.setAmount(Long.MAX_VALUE);
                ItemStack stackC = keyC.toStack(0x3fffffff);
                level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                        .stream().filter(r -> r.test(stackC,stackA,stackB)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputKeyC = keyC;
                            inputAmountA = r.getInputFluid().getNeededAmount(stackA);
                            inputAmountB = r.getInputGas().getNeededAmount(stackB);
                            inputAmountC = r.getInputSolid().getNeededAmount(stackC);
                            ItemStack itemOutput = r.getOutput(stackC,stackA,stackB).item();
                            outputKeyA = AEItemKey.of(itemOutput);
                            outputAmountA = itemOutput.getCount();
                            GasStack gasOutput = r.getOutput(stackC,stackA,stackB).gas();
                            outputKeyB = MekanismKey.of(gasOutput);
                            outputAmountB = gasOutput.getAmount();
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    public IMekanismRecipeTypeProvider<PressurizedReactionRecipe, ?> getRecipeType() {
        return MekanismRecipeType.REACTION;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmountA > 0 && inputAmountB > 0 && inputAmountC > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(
                    Math.min(storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA,
                             storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB),
                    Math.min(storage.extract(inputKeyC, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountC,
                    Math.min(outputKeyA != null ? storage.insert(outputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountA : Long.MAX_VALUE,
                             outputKeyB != null ? storage.insert(outputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountB : Long.MAX_VALUE)));
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                storage.extract(inputKeyB, operations * inputAmountB, Actionable.MODULATE, source);
                storage.extract(inputKeyC, operations * inputAmountC, Actionable.MODULATE, source);
                storage.insert(outputKeyA, operations * outputAmountA, Actionable.MODULATE, source);
                storage.insert(outputKeyB, operations * outputAmountB, Actionable.MODULATE, source);
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

    public AEKey getInputKeyC() {
        return inputKeyC;
    }

    public AEKey getOutputKeyA() {
        return outputKeyA;
    }

    public AEKey getOutputKeyB() {
        return outputKeyB;
    }

}
