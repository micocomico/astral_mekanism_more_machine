package ammm.block.blockentity.mashine.appliedmachine;

import ammm.block.blockentity.interf.applied.IAppliedTripleToSingleMachine;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
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
import org.jetbrains.annotations.NotNull;

public class AppliedAlloyer extends BEAppliedEnergizedMachine
        implements IAppliedTripleToSingleMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private BasicInventorySlot cardSlotC;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey inputKeyC;
    private AEKey outputKey;
    private long inputAmountA;
    private long inputAmountB;
    private long inputAmountC;
    private long outputAmount;

    public AppliedAlloyer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.combiner.get().multiply(200)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlotA = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 45, 53));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
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
        outputKey = null;
        inputAmountA = 0;
        inputAmountB = 0;
        inputAmountC = 0;
        outputAmount = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlotA.isEmpty() || cardSlotB.isEmpty() || cardSlotC.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        ItemStack isC = cardSlotC.getStack();
        if (isA.getItem() instanceof ItemIngredientCardItem cardItemA && isB.getItem() instanceof ItemIngredientCardItem cardItemB && isC.getItem() instanceof ItemIngredientCardItem cardItemC) {
            AEItemKey keyA = cardItemA.getKey(isA);
            AEItemKey keyB = cardItemB.getKey(isB);
            AEItemKey keyC = cardItemC.getKey(isC);
            if (keyA != null && keyB != null && keyC != null && hasLevel()) {
                ItemStack stackA = keyA.toStack(0x3fffffff);
                ItemStack stackB = keyB.toStack(0x3fffffff);
                ItemStack stackC = keyC.toStack(0x3fffffff);
                level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                        .stream().filter(r -> r.test(stackA,stackB,stackC)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputKeyC = keyC;
                            inputAmountA = r.getMainInput().getNeededAmount(stackA);
                            inputAmountB = r.getExtraInput().getNeededAmount(stackB);
                            inputAmountC = r.getTertiaryExtraInput().getNeededAmount(stackC);
                            ItemStack output = r.getOutput(stackA,stackB,stackC);
                            outputKey = AEItemKey.of(output);
                            outputAmount = output.getCount();
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    public IMekanismRecipeTypeProvider<AlloyerRecipe, ?> getRecipeType() {
        return EMRecipeType.ALLOYING;
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
                             storage.insert(outputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmount));
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                storage.extract(inputKeyB, operations * inputAmountB, Actionable.MODULATE, source);
                storage.extract(inputKeyC, operations * inputAmountC, Actionable.MODULATE, source);
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

    public AEKey getInputKeyC() {
        return inputKeyC;
    }

    public AEKey getOutputKey() {
        return outputKey;
    }

}
