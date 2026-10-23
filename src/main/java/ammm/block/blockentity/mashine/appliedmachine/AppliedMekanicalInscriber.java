package ammm.block.blockentity.mashine.appliedmachine;

import ammm.block.blockentity.interf.applied.IAppliedTripleToSingleMachine;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AppliedMekanicalInscriber extends BEAppliedEnergizedMachine
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

    public AppliedMekanicalInscriber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, AMEConfig.usage.mekanicalInscriber.get().multiply(80)
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
            if (keyB != null && hasLevel()) {
                ItemStack stackA = keyA != null ? keyA.toStack(0x3fffffff) : ItemStack.EMPTY;
                ItemStack stackB = keyB.toStack(0x3fffffff);
                ItemStack stackC = keyC != null ? keyC.toStack(0x3fffffff) : ItemStack.EMPTY;
                level.getRecipeManager().getAllRecipesFor(InscriberRecipe.TYPE)
                        .stream().filter(r -> r.getTopOptional().test(stackA) && r.getMiddleInput().test(stackB) && r.getBottomOptional().test(stackC)).findFirst().ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputKeyC = keyC;
                            inputAmountA = r.getProcessType() == InscriberProcessType.INSCRIBE ? 0 : 1;
                            inputAmountB = 1;
                            inputAmountC = r.getProcessType() == InscriberProcessType.INSCRIBE ? 0 : 1;
                            outputKey = AEItemKey.of(r.getResultItem());
                            outputAmount = 1;
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    public IUnifiedRecipeTypeProvider<InscriberRecipe, ?> getRecipeType() {
        return GeneralRecipeType.INSCRIBE;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmountB > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(
                    Math.min(inputAmountA != 0 ? storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA : Long.MAX_VALUE,
                             storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB),
                    Math.min(inputAmountC != 0 ? storage.extract(inputKeyC, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountC : Long.MAX_VALUE,
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
