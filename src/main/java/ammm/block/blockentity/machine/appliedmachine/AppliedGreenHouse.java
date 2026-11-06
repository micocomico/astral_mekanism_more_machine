package ammm.block.blockentity.machine.appliedmachine;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
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
import net.darkhax.botanypots.data.recipes.crop.HarvestEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class AppliedGreenHouse extends BEAppliedEnergizedMachine {

    private BasicInventorySlot cardSlotA;
    private BasicInventorySlot cardSlotB;
    private BasicInventorySlot cardSlotC;
    private AEKey inputKeyA;
    private AEKey inputKeyB;
    private AEKey inputKeyC;
    private AEKey[] outputKey = new AEKey[12];
    private long inputAmountA;
    private long inputAmountB;
    private long inputAmountC;
    private long[] outputAmount = new long[12];
    private float[] outputChance = new float[12];

    public AppliedGreenHouse(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, AMEConfig.usage.greenHouse.get().multiply(1200)
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
                }, 8, 53));
        builder.addSlot(
                cardSlotB = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 26, 53));
        builder.addSlot(
                cardSlotC = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 44, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKeyA = null;
        inputKeyB = null;
        inputKeyC = null;
        Arrays.fill(outputKey,null);
        inputAmountA = 0;
        inputAmountB = 0;
        inputAmountC = 0;
        Arrays.fill(outputAmount,0);
    }

    private void recalculateRecipeInfo() {
        if (cardSlotA.isEmpty() || cardSlotB.isEmpty() || cardSlotC.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        outputKey = new AEKey[12];
        outputAmount = new long[12];
        outputChance = new float[12];
        ItemStack isA = cardSlotA.getStack();
        ItemStack isB = cardSlotB.getStack();
        ItemStack isC = cardSlotC.getStack();
        if (isA.getItem() instanceof FluidIngredientCardItem cardItemA && isB.getItem() instanceof ItemIngredientCardItem cardItemB && isC.getItem() instanceof ItemIngredientCardItem cardItemC) {
            AEFluidKey keyA = cardItemA.getKey(isA);
            AEItemKey keyB = cardItemB.getKey(isB);
            AEItemKey keyC = cardItemC.getKey(isC);
            if (keyA != null && keyB != null && keyC != null && hasLevel()) {
                FluidStack stackA = keyA.toStack(0x7fffffff);
                ItemStack stackB = keyB.toStack(0x3fffffff);
                ItemStack stackC = keyC.toStack(0x3fffffff);
                CropSoilRecipe.getAllRecipes(level.getRecipeManager())
                        .stream().filter(r -> r.test(stackB,stackC,stackA)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKeyA = keyA;
                            inputKeyB = keyB;
                            inputKeyC = keyC;
                            inputAmountA = r.getWater().getNeededAmount(stackA);
                            inputAmountB = r.getCrop().getNeededAmount(stackB);
                            inputAmountC = r.getSoil().getNeededAmount(stackC);
                            for (HarvestEntry entry : r.getOutput()) {
                                int i = r.getOutput().indexOf(entry);
                                outputKey[i] = AEItemKey.of(entry.getItem());
                                outputAmount[i] = Math.round(entry.getItem().getCount() * ((entry.getMaxRolls() + entry.getMinRolls()) / 2f));
                                outputChance[i] = entry.getChance();
                            }
                        }, this::removeRecipeInfo);
                return;
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
        if (MekanismUtils.canFunction(this) && inputAmountA > 0 && inputAmountB > 0 && inputAmountC > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(
                    Math.min(storage.extract(inputKeyA, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountA,
                             storage.extract(inputKeyB, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountB),
                             storage.extract(inputKeyC, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmountC);
            for (int i = 0; i < 12; i++){
                if(outputKey != null && outputAmount[i] != 0) operations = Math.min(operations, storage.insert(outputKey[i], Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmount[i]);
            }
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKeyA, operations * inputAmountA, Actionable.MODULATE, source);
                for (int i = 0; i < 12; i++){
                    if(outputKey != null && outputAmount[i] != 0) storage.insert(outputKey[i], Math.round(operations * outputAmount[i] * outputChance[i] + new Random().nextFloat(-0.5f, 0.5f)), Actionable.MODULATE, source);
                }
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

    public AEKey getOutputKey0() {
        return outputKey[0];
    }
    public AEKey getOutputKey1() {
        return outputKey[1];
    }
    public AEKey getOutputKey2() {
        return outputKey[2];
    }
    public AEKey getOutputKey3() {
        return outputKey[3];
    }
    public AEKey getOutputKey4() {
        return outputKey[4];
    }
    public AEKey getOutputKey5() {
        return outputKey[5];
    }
    public AEKey getOutputKey6() {
        return outputKey[6];
    }
    public AEKey getOutputKey7() {
        return outputKey[7];
    }
    public AEKey getOutputKey8() {
        return outputKey[8];
    }
    public AEKey getOutputKey9() {
        return outputKey[9];
    }
    public AEKey getOutputKey10() {
        return outputKey[10];
    }
    public AEKey getOutputKey11() {
        return outputKey[11];
    }

}
