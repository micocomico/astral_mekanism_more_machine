package ammm.block.blockentity.machine.appliedmachine;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.IContentsListener;
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

public class AppliedTransformer extends BEAppliedEnergizedMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private BasicInventorySlot cardSlotC;
    private BasicInventorySlot cardSlotD;
    private BasicInventorySlot cardSlotE;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey inputKeyC;
    private AEKey inputKeyD;
    private AEKey inputKeyE;
    private AEKey outputKeyA;
    private AEKey outputKeyB;
    private long inputAmountA;
    private long inputAmountB;
    private long inputAmountC;
    private long inputAmountD;
    private long inputAmountE;
    private long outputAmountA;
    private long outputAmountB;
    private boolean isCatalystA;
    private boolean isCatalystB;
    private boolean isCatalystC;
    private boolean isCatalystD;
    private boolean isCatalystE;

    public AppliedTransformer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, AMEConfig.usage.transformer.get().multiply(10)
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
                }, 30, 53));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 48, 53));
        builder.addSlot(
                cardSlotC = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 66, 53));
        builder.addSlot(
                cardSlotD = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 84, 53));
        builder.addSlot(
                cardSlotE = BasicInventorySlot.at(stack -> stack.getItem() instanceof FluidIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 102, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        inputKeyB = null;
        inputKeyC = null;
        inputKeyD = null;
        inputKeyE = null;
        outputKeyA = null;
        outputKeyB = null;
        inputAmountA = 0;
        inputAmountB = 0;
        inputAmountC = 0;
        inputAmountD = 0;
        inputAmountE = 0;
        outputAmountA = 0;
        outputAmountB = 0;
        isCatalystA = false;
        isCatalystB = false;
        isCatalystC = false;
        isCatalystD = false;
        isCatalystE = false;
    }

    private void recalculateRecipeInfo() {
        if (cardSlotA.isEmpty() || cardSlotB.isEmpty() || cardSlotC.isEmpty() || cardSlotD.isEmpty() || cardSlotE.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        ItemStack isC = cardSlotC.getStack();
        ItemStack isD = cardSlotD.getStack();
        ItemStack isE = cardSlotE.getStack();
        if (isA.getItem() instanceof FluidIngredientCardItem cardItemA && isB.getItem() instanceof ItemIngredientCardItem cardItemB && isC.getItem() instanceof ItemIngredientCardItem cardItemC && isD.getItem() instanceof ItemIngredientCardItem cardItemD && isE.getItem() instanceof FluidIngredientCardItem cardItemE) {
            AEFluidKey keyA = cardItemA.getKey(isA);
            AEItemKey keyB = cardItemB.getKey(isB);
            AEItemKey keyC = cardItemC.getKey(isC);
            AEItemKey keyD = cardItemD.getKey(isD);
            AEFluidKey keyE = cardItemE.getKey(isE);
            if (keyA != null && keyB != null && keyC != null && keyD != null && keyE != null && hasLevel()) {
                FluidStack stackA = keyA.toStack(0x3fffffff);
                ItemStack stackB = keyB.toStack(0x3fffffff);
                ItemStack stackC = keyC.toStack(0x3fffffff);
                ItemStack stackD = keyD.toStack(0x3fffffff);
                FluidStack stackE = keyE.toStack(0x3fffffff);
                level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                        .stream().filter(r -> r.test(stackB,stackC,stackD,stackA,stackE)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputKeyC = keyC;
                            inputKeyD = keyD;
                            inputKeyE = keyE;
                            inputAmountA = r.getInputFluidA().getNeededAmount(stackA);
                            inputAmountB = r.getInputItemA().getNeededAmount(stackB);
                            inputAmountC = r.getInputItemB().getNeededAmount(stackC);
                            inputAmountD = r.getInputItemC().getNeededAmount(stackD);
                            inputAmountE = r.getInputFluidB().getNeededAmount(stackE);
                            isCatalystA = r.isFluidACatalyst();
                            isCatalystB = r.isItemACatalyst();
                            isCatalystC = r.isItemBCatalyst();
                            isCatalystD = r.isItemCCatalyst();
                            isCatalystE = r.isFluidBCatalyst();
                            ItemStack outputA = r.getOutput().item();
                            FluidStack outputB = r.getOutput().fluid();
                            outputKeyA = AEItemKey.of(outputA);
                            outputKeyB = AEFluidKey.of(outputB);
                            outputAmountA = outputA.getCount();
                            outputAmountB = outputB.getAmount();
                        }, this::removeRecipeInfo);
                return;
            } else if (keyA != null){
                FluidStack stackA = keyA.toStack(0x3fffffff);
                ItemStack stackB = keyB != null ? keyB.toStack(0x3fffffff) : ItemStack.EMPTY;
                ItemStack stackC = keyC != null ? keyC.toStack(0x3fffffff) : ItemStack.EMPTY;
                ItemStack stackD = keyD != null ? keyD.toStack(0x3fffffff) : ItemStack.EMPTY;
                for (TransformRecipe r : level.getRecipeManager().getAllRecipesFor(TransformRecipe.TYPE)){
                    boolean match = true;
                    int size = r.getIngredients().size();
                    match = r.circumstance.isFluid(stackA.getFluid());
                    match = 0 < size ? r.getIngredients().get(0).test(stackB) : stackB == ItemStack.EMPTY;
                    match = 1 < size ? r.getIngredients().get(1).test(stackC) : stackC == ItemStack.EMPTY;
                    match = 2 < size ? r.getIngredients().get(2).test(stackD) : stackD == ItemStack.EMPTY;
                    if (match){
                        inputKeyA = keyA;
                        inputKeyB = keyB;
                        inputKeyC = keyC;
                        inputKeyD = keyD;
                        inputAmountA = 1000;
                        inputAmountB = 1;
                        inputAmountC = 1;
                        inputAmountD = 1;
                        isCatalystA = true;
                        ItemStack output = r.getResultItem();
                        outputKeyA = AEItemKey.of(output);
                        outputAmountA = output.getCount();
                        return;
                    }
                }
            }
        }
        removeRecipeInfo();
        return;
    }

    public IMekanismRecipeTypeProvider<MekanicalTransformRecipe, ?> getRecipeType() {
        return AMERecipeTypes.MEKANICAL_TRAMSFORM;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmountA + inputAmountB + inputAmountC + inputAmountD + inputAmountE > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Long.MAX_VALUE;
            operations = Math.min(operations,!isCatalystA ? storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA : storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA >= 1 ? Long.MAX_VALUE : 0);
            operations = Math.min(operations,inputKeyB != null ? !isCatalystB ? storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB : storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB >= 1 ? Long.MAX_VALUE : 0 : Long.MAX_VALUE);
            operations = Math.min(operations,inputKeyC != null ? !isCatalystC ? storage.extract(inputKeyC, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountC : storage.extract(inputKeyC, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountC >= 1 ? Long.MAX_VALUE : 0 : Long.MAX_VALUE);
            operations = Math.min(operations,inputKeyD != null ? !isCatalystD ? storage.extract(inputKeyD, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountD : storage.extract(inputKeyD, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountD >= 1 ? Long.MAX_VALUE : 0 : Long.MAX_VALUE);
            operations = Math.min(operations,inputKeyE != null ? !isCatalystE ? storage.extract(inputKeyE, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountE : storage.extract(inputKeyE, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountE >= 1 ? Long.MAX_VALUE : 0 : Long.MAX_VALUE);
            operations = Math.min(operations,outputKeyA != null ? storage.insert(outputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountA : Long.MAX_VALUE);
            operations = Math.min(operations,outputKeyB != null ? storage.insert(outputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountB : Long.MAX_VALUE);
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                if (!isCatalystA) storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                if (!isCatalystB) storage.extract(inputKeyB, operations * inputAmountB, Actionable.MODULATE, source);
                if (!isCatalystC) storage.extract(inputKeyC, operations * inputAmountC, Actionable.MODULATE, source);
                if (!isCatalystD) storage.extract(inputKeyD, operations * inputAmountD, Actionable.MODULATE, source);
                if (!isCatalystE) storage.extract(inputKeyE, operations * inputAmountE, Actionable.MODULATE, source);
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

    public AEKey getInputKeyD() {
        return inputKeyD;
    }

    public AEKey getInputKeyE() {
        return inputKeyE;
    }

    public AEKey getOutputKey() {
        return outputKeyA;
    }

    public AEKey getOutputKeyB() {
        return outputKeyB;
    }

}
