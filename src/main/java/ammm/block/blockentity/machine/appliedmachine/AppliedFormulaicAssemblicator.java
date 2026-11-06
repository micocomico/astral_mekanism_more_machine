package ammm.block.blockentity.machine.appliedmachine;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class AppliedFormulaicAssemblicator extends BEAppliedEnergizedMachine {

    private BasicInventorySlot[] cardSlot;
    private AEKey[] inputKey = new AEKey[9];
    private AEKey outputKey;
    private AEKey[] remaindKey = new AEKey[9];
    private long[] inputAmount = new long[9];
    private long outputAmount;
    private long[] remaindAmount = new long[9];

    public AppliedFormulaicAssemblicator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.formulaicAssemblicator.getOrDefault()
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        cardSlot = new BasicInventorySlot[9];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                builder.addSlot(
                cardSlot[x + y * 3] = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 20 + x * 18, 17 + y * 18));
            }
        }
        return builder.build();
    }

    private void removeRecipeInfo() {
        Arrays.fill(inputKey,null);
        outputKey = null;
        Arrays.fill(inputAmount,0);
        outputAmount = 0;
    }

    private void recalculateRecipeInfo() {
        for (BasicInventorySlot basicInventorySlot : cardSlot) {
            if (basicInventorySlot.isEmpty()) {
                removeRecipeInfo();
                return;
            }
        }
        inputKey = new AEKey[9];
        remaindKey = new AEKey[9];
        inputAmount = new long[9];
        remaindAmount = new long[9];
        ItemStack[] is = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            is[i] = cardSlot[i].getStack();
        }
        ItemIngredientCardItem[] cardItem = new ItemIngredientCardItem[9];
        boolean allItemCard = true;
        for (int i = 0; i < 9; i++) {
            if (is[i].getItem() instanceof ItemIngredientCardItem CardItem) {
                cardItem[i] = CardItem;
            } else  {
                allItemCard = false;
            }
        }
        if (allItemCard) {
            AEItemKey[] key = new AEItemKey[9];
            for (int i = 0; i < 9; i++) {
                key[i] = cardItem[i].getKey(is[i]);
            }
            if (hasLevel()) {
                ItemStack[] stack = new ItemStack[9];
                for (int i = 0; i < 9; i++) {
                    stack[i] = key[i] != null ? key[i].toStack(0x7fffffff) : ItemStack.EMPTY;
                }
                for(CraftingRecipe r : level.getRecipeManager().getAllRecipesFor(getRecipeType())){
                    boolean match = true;
                    if (r instanceof ShapedRecipe shapedRecipe) {
                        int width = shapedRecipe.getWidth();
                        int height = shapedRecipe.getHeight();
                        for (int y = 0; y < 3; y++) {
                            for (int x = 0; x < 3; x++) {
                                match = match && (x < width && y < height ? shapedRecipe.getIngredients().get(x + y * width).test(stack[x + y * 3]) : stack[x + y * 3] == ItemStack.EMPTY);
                            }
                        }
                    } else {
                        int size = r.getIngredients().size();
                        for (int i = 0; i < 9; i++) {
                            match = match && (i < size ? r.getIngredients().get(i).test(stack[i]) : stack[i] == ItemStack.EMPTY);
                        }
                    }
                    if (match) {
                        inputKey = key;
                        for (int i = 0; i < 9; i++) {
                            inputAmount[i] = Collections.frequency(Arrays.asList(inputKey), inputKey[i]);
                            remaindKey[i] = key[i] != null ? AEItemKey.of(key[i].toStack().getCraftingRemainingItem()) : null;
                            remaindAmount[i] = Collections.frequency(Arrays.asList(remaindKey), remaindKey[i]);
                        }
                        ItemStack output = r.getResultItem(level.registryAccess());
                        outputKey = AEItemKey.of(output);
                        outputAmount = output.getCount();
                        return;
                    }
                }
            }
        }
        removeRecipeInfo();
        return;
    }

    protected RecipeType<CraftingRecipe> getRecipeType(){
        return MekanismRecipeType.CRAFTING;
    };

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && Collections.frequency(Arrays.asList(inputKey),null) < 9 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Long.MAX_VALUE;
            for (int i = 0; i < 9; i++){
                operations = Math.min(operations,inputKey[i] != null ? storage.extract(inputKey[i], Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmount[i] : Long.MAX_VALUE);
                operations = Math.min(operations,remaindKey[i] != null ? storage.insert(remaindKey[i], Long.MAX_VALUE, Actionable.SIMULATE, source) / remaindAmount[i] : Long.MAX_VALUE);
            }
            operations = Math.min(operations,storage.insert(outputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmount);
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                for (int i = 0; i < 9; i++){
                    storage.extract(inputKey[i], operations, Actionable.MODULATE, source);
                    if(remaindKey[i] != null) {storage.insert(remaindKey[i], operations, Actionable.MODULATE, source);}
                }
                storage.insert(outputKey, operations * outputAmount, Actionable.MODULATE, source);
                consumeEnergy(storage, source, operations);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKey0() {
        return inputKey[0];
    }
    public AEKey getInputKey1() {
        return inputKey[1];
    }
    public AEKey getInputKey2() {
        return inputKey[2];
    }
    public AEKey getInputKey3() {
        return inputKey[3];
    }
    public AEKey getInputKey4() {
        return inputKey[4];
    }
    public AEKey getInputKey5() {
        return inputKey[5];
    }
    public AEKey getInputKey6() {
        return inputKey[6];
    }
    public AEKey getInputKey7() {
        return inputKey[7];
    }
    public AEKey getInputKey8() {
        return inputKey[8];
    }

    public AEKey getOutputKey() {
        return outputKey;
    }

}
