package ammm.block.blockentity.machine.appliedmachine;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
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
import net.minecraftforge.fluids.FluidStack;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class AppliedReactionChamber extends BEAppliedEnergizedMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot[] cardSlotB;
    private AEKey inputKeyA;
    private AEKey[] inputKeyB = new AEKey[9];
    private AEKey outputKeyA;
    private AEKey outputKeyB;
    private long inputAmountA;
    private long[] inputAmountB = new long[9];
    private long outputAmountA;
    private long outputAmountB;

    public AppliedReactionChamber(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, AMEConfig.usage.aaeReactionChamber.getOrDefault().multiply(10)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);builder.addSlot(
        cardSlotA = BasicInventorySlot.at(stack -> stack.getItem() instanceof FluidIngredientCardItem, () -> {
            listener.onContentsChanged();
            recalculateRecipeInfo();
        }, 38, 17));
        cardSlotB = new BasicInventorySlot[9];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                builder.addSlot(
                cardSlotB[x + y * 3] = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 20 + x * 18, 35 + y * 18));
            }
        }
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        Arrays.fill(inputKeyB,null);
        outputKeyA = null;
        outputKeyB = null;
        inputAmountA = 0;
        Arrays.fill(inputAmountB,0);
        outputAmountA = 0;
        outputAmountB = 0;
    }

    private void recalculateRecipeInfo() {
        if(cardSlotA.isEmpty()) return;
        for (BasicInventorySlot basicInventorySlot : cardSlotB) {
            if (basicInventorySlot.isEmpty()) {
                removeRecipeInfo();
                return;
            }
        }
        inputKeyB = new AEKey[9];
        inputAmountB = new long[9];
        ItemStack isA = cardSlotA.getStack();
        ItemStack[] isB = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            isB[i] = cardSlotB[i].getStack();
        }
        ItemIngredientCardItem[] cardItemB = new ItemIngredientCardItem[9];
        boolean allItemCard = true;
        for (int i = 0; i < 9; i++) {
            if (isB[i].getItem() instanceof ItemIngredientCardItem CardItem) {
                cardItemB[i] = CardItem;
            } else  {
                allItemCard = false;
            }
        }
        if (isA.getItem() instanceof FluidIngredientCardItem cardItemA && allItemCard) {
            AEFluidKey keyA = cardItemA.getKey(isA);
            AEItemKey[] keyB = new AEItemKey[9];
            for (int i = 0; i < 9; i++) {
                keyB[i] = cardItemB[i].getKey(isB[i]);
            }
            if (hasLevel() && keyA != null) {
                FluidStack stackA = keyA.toStack(0x7fffffff);
                ItemStack[] stackB = new ItemStack[9];
                for (int i = 0; i < 9; i++) {
                    stackB[i] = keyB[i] != null ? keyB[i].toStack(0x7fffffff) : ItemStack.EMPTY;
                }
                for(ReactionChamberRecipe r : level.getRecipeManager().getAllRecipesFor(ReactionChamberRecipe.TYPE)){
                    boolean match;
                    int size = r.getInputs().size();
                    match = r.containsIngredient(stackA);
                    for (int i = 0; i < 9; i++) {
                        match = match && (i < size ? r.getInputs().get(i).test(stackB[i]) : stackB[i] == ItemStack.EMPTY);
                    }
                    if (match) {
                        inputKeyA = keyA;
                        inputKeyB = keyB;
                        inputAmountA = r.getFluid().getAmount();
                        for (int i = 0; i < 9; i++) {
                            inputAmountB[i] = i < size ? r.getInputs().get(i).getAmount() : 0;
                        }
                        ItemStack outputA = r.getResultItem();
                        FluidStack outputB = r.getResultFluid();
                        outputKeyA = AEItemKey.of(outputA);
                        outputKeyB = AEFluidKey.of(outputB);
                        outputAmountA = outputA.getCount();
                        outputAmountB = outputB.getAmount();
                        return;
                    }
                }
            }
        }
        removeRecipeInfo();
        return;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && Collections.frequency(Arrays.asList(inputKeyB),null) < 9 && inputAmountA > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA;
            for (int i = 0; i < 9; i++){
                operations = Math.min(operations, inputKeyB[i] != null ? storage.extract(inputKeyB[i], Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB[i] : Long.MAX_VALUE);
            }
            operations = Math.min(operations,outputKeyA != null ? storage.insert(outputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountA : Long.MAX_VALUE);
            operations = Math.min(operations,outputKeyB != null ? storage.insert(outputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmountB : Long.MAX_VALUE);
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                for (int i = 0; i < 9; i++){
                    storage.extract(inputKeyB[i], operations * inputAmountB[i], Actionable.MODULATE, source);
                }
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

    public AEKey getInputKey0() {
        return inputKeyB[0];
    }
    public AEKey getInputKey1() {
        return inputKeyB[1];
    }
    public AEKey getInputKey2() {
        return inputKeyB[2];
    }
    public AEKey getInputKey3() {
        return inputKeyB[3];
    }
    public AEKey getInputKey4() {
        return inputKeyB[4];
    }
    public AEKey getInputKey5() {
        return inputKeyB[5];
    }
    public AEKey getInputKey6() {
        return inputKeyB[6];
    }
    public AEKey getInputKey7() {
        return inputKeyB[7];
    }
    public AEKey getInputKey8() {
        return inputKeyB[8];
    }

    public AEKey getOutputKeyA() {
        return outputKeyA;
    }
    public AEKey getOutputKeyB() {
        return outputKeyB;
    }

}
