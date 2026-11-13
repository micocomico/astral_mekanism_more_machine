package ammm.block.blockentity.machine.appliedmachine;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.item.recipecard.ChemicalIngredientCardItem;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import astral_mekanism.registries.AMERecipeTypes;
import astral_mekanism.util.AMEKeyUtils;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
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

import java.util.Arrays;
import java.util.Collections;

public class AppliedCrafter extends BEAppliedEnergizedMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private BasicInventorySlot[] cardSlotC;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey[] inputKeyC = new AEKey[25];
    private AEKey outputKey;
    private long inputAmountA;
    private long inputAmountB;
    private long[] inputAmountC = new long[25];
    private long outputAmount;

    public AppliedCrafter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, AMEConfig.usage.essentialCrafter.get().multiply(100)
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
                }, 38, 17));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ChemicalIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 74, 17));
        cardSlotC = new BasicInventorySlot[25];
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                builder.addSlot(
                cardSlotC[x + y * 5] = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 20 + x * 18, 35 + y * 18));
            }
        }
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        inputKeyB = null;
        Arrays.fill(inputKeyC,null);
        outputKey = null;
        inputAmountA = 0;
        inputAmountB = 0;
        Arrays.fill(inputAmountC,0);
        outputAmount = 0;
    }

    private void recalculateRecipeInfo() {
        if(cardSlotA.isEmpty() || cardSlotB.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        for (BasicInventorySlot basicInventorySlot : cardSlotC) {
            if (basicInventorySlot.isEmpty()) {
                removeRecipeInfo();
                return;
            }
        }
        inputKeyC = new AEKey[25];
        inputAmountC = new long[25];
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        ItemStack[] isC = new ItemStack[25];
        for (int i = 0; i < 25; i++) {isC[i] = cardSlotC[i].getStack();}
        ItemIngredientCardItem[] cardItemC = new ItemIngredientCardItem[25];
        boolean allItemCard = true;
        for (int i = 0; i < 25; i++) {
            if (isC[i].getItem() instanceof ItemIngredientCardItem CardItem) {cardItemC[i] = CardItem;}
            else  {allItemCard = false;}
        }
        if (isA.getItem() instanceof FluidIngredientCardItem cardItemA && isB.getItem() instanceof ChemicalIngredientCardItem cardItemB && allItemCard) {
            AEFluidKey keyA = cardItemA.getKey(isA);
            MekanismKey keyB = cardItemB.getKey(isB);
            AEItemKey[] keyC = new AEItemKey[25];
            for (int i = 0; i < 25; i++) {keyC[i] = cardItemC[i].getKey(isC[i]);}
            if (keyA != null && keyB != null && Collections.frequency(Arrays.asList(keyC),null) == 0 && hasLevel()) {
                FluidStack stackA = keyA.toStack(0x7fffffff);
                GasStack stackB = AMEKeyUtils.getGas(keyB);
                stackB.setAmount(Long.MAX_VALUE);
                ItemStack[] stackC = new ItemStack[25];
                for (int i = 0; i < 25; i++) {stackC[i] = keyC[i].toStack(0x3fffffff);}
                level.getRecipeManager().getAllRecipesFor(getRecipeType().getRecipeType())
                        .stream().filter(r -> r.test(stackC,stackA,stackB)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputKeyC = keyC;
                            inputAmountA = r.getInputFluid().getNeededAmount(stackA);
                            inputAmountB = r.getInputGas().getNeededAmount(stackB);
                            for (int i = 0; i < 25; i++) {
                                inputAmountC[i] = r.getInputItem(i).getNeededAmount(stackC[i]);
                            }
                            ItemStack output = r.getOutput(stackC,stackA,stackB);
                            outputKey = AEItemKey.of(output);
                            outputAmount = output.getCount();
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    protected IMekanismRecipeTypeProvider<AstralCraftingRecipe, ?> getRecipeType(){
        return AMERecipeTypes.ASTRAL_CRAFTING;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && Collections.frequency(Arrays.asList(inputKeyC),null) == 0 && inputAmountA > 0 && inputAmountB > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA,
                                       storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB);
            for (int i = 0; i < 25; i++){
                operations = Math.min(operations, storage.extract(inputKeyC[i], Long.MAX_VALUE, Actionable.SIMULATE, source) / Collections.frequency(Arrays.asList(inputKeyC),inputKeyC[i]) * inputAmountC[i]);
            }
            operations = Math.min(operations, storage.insert(outputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmount);
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                storage.extract(inputKeyB, operations * inputAmountB, Actionable.MODULATE, source);
                for (int i = 0; i < 25; i++){
                    storage.extract(inputKeyC[i], operations * inputAmountC[i], Actionable.MODULATE, source);
                }
                storage.insert(outputKey, operations * outputAmount, Actionable.MODULATE, source);
                consumeEnergy(storage, source, operations);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKeyA(){return inputKeyA;}
    public AEKey getInputKeyB(){return inputKeyB;}

    public AEKey getInputKey0(){return inputKeyC[0];}
    public AEKey getInputKey1(){return inputKeyC[1];}
    public AEKey getInputKey2(){return inputKeyC[2];}
    public AEKey getInputKey3(){return inputKeyC[3];}
    public AEKey getInputKey4(){return inputKeyC[4];}
    public AEKey getInputKey5(){return inputKeyC[5];}
    public AEKey getInputKey6(){return inputKeyC[6];}
    public AEKey getInputKey7(){return inputKeyC[7];}
    public AEKey getInputKey8(){return inputKeyC[8];}
    public AEKey getInputKey9(){return inputKeyC[9];}
    public AEKey getInputKey10(){return inputKeyC[10];}
    public AEKey getInputKey11(){return inputKeyC[11];}
    public AEKey getInputKey12(){return inputKeyC[12];}
    public AEKey getInputKey13(){return inputKeyC[13];}
    public AEKey getInputKey14(){return inputKeyC[14];}
    public AEKey getInputKey15(){return inputKeyC[15];}
    public AEKey getInputKey16(){return inputKeyC[16];}
    public AEKey getInputKey17(){return inputKeyC[17];}
    public AEKey getInputKey18(){return inputKeyC[18];}
    public AEKey getInputKey19(){return inputKeyC[19];}
    public AEKey getInputKey20(){return inputKeyC[20];}
    public AEKey getInputKey21(){return inputKeyC[21];}
    public AEKey getInputKey22(){return inputKeyC[22];}
    public AEKey getInputKey23(){return inputKeyC[23];}
    public AEKey getInputKey24(){return inputKeyC[24];}

    public AEKey getOutputKey() {
        return outputKey;
    }

}
